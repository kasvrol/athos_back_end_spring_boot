package br.ufpr.athos.campeonato.service;

import br.ufpr.athos.campeonato.config.RabbitMQConfig;
import br.ufpr.athos.campeonato.dto.AtualizarPlacarDTO;
import br.ufpr.athos.campeonato.dto.EquipeResponseDTO;
import br.ufpr.athos.campeonato.dto.PartidaRequestDTO;
import br.ufpr.athos.campeonato.dto.PartidaResponseDTO;
import br.ufpr.athos.campeonato.event.PartidaEvent;
import br.ufpr.athos.campeonato.exception.BusinessRuleException;
import br.ufpr.athos.campeonato.exception.ResourceNotFoundException;
import br.ufpr.athos.campeonato.exception.ValidationException;
import br.ufpr.athos.campeonato.model.Campeonato;
import br.ufpr.athos.campeonato.model.Equipe;
import br.ufpr.athos.campeonato.model.Partida;
import br.ufpr.athos.campeonato.model.Partida.StatusPartida;
import br.ufpr.athos.campeonato.repository.CampeonatoRepository;
import br.ufpr.athos.campeonato.repository.EquipeRepository;
import br.ufpr.athos.campeonato.repository.PartidaRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PartidaService {

    @Autowired
    private PartidaRepository partidaRepository;

    @Autowired
    private CampeonatoRepository campeonatoRepository;

    @Autowired
    private EquipeRepository equipeRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private EquipeService equipeService;

    @Transactional
    public PartidaResponseDTO criarPartida(PartidaRequestDTO dto) {
        // Validar que o campeonato existe
        Campeonato campeonato = campeonatoRepository.findById(dto.getCampeonatoId())
                .orElseThrow(() -> new ResourceNotFoundException("Campeonato", dto.getCampeonatoId()));

        // Validar que as equipes existem
        Equipe equipe1 = equipeRepository.findById(dto.getEquipe1Id())
                .orElseThrow(() -> new ResourceNotFoundException("Equipe", dto.getEquipe1Id()));

        Equipe equipe2 = equipeRepository.findById(dto.getEquipe2Id())
                .orElseThrow(() -> new ResourceNotFoundException("Equipe", dto.getEquipe2Id()));

        // Validar que as equipes são diferentes
        if (dto.getEquipe1Id().equals(dto.getEquipe2Id())) {
            throw new ValidationException("Uma equipe não pode jogar contra si mesma");
        }

        // Validar que as equipes pertencem ao campeonato
        if (!equipe1.getCampeonato().getId().equals(dto.getCampeonatoId())) {
            throw new ValidationException("Equipe 1 não pertence ao campeonato especificado");
        }

        if (!equipe2.getCampeonato().getId().equals(dto.getCampeonatoId())) {
            throw new ValidationException("Equipe 2 não pertence ao campeonato especificado");
        }

        // Criar a partida
        Partida partida = new Partida();
        partida.setCampeonato(campeonato);
        partida.setEquipe1(equipe1);
        partida.setEquipe2(equipe2);
        partida.setDataHora(dto.getDataHora());
        partida.setLocal(dto.getLocal());
        partida.setFase(dto.getFase());
        partida.setRodada(dto.getRodada());

        Partida partidaSalva = partidaRepository.save(partida);
        return converterParaDTO(partidaSalva);
    }

    @Transactional(readOnly = true)
    public PartidaResponseDTO buscarPorId(String id) {
        Partida partida = partidaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Partida", id));
        return converterParaDTO(partida);
    }

    @Transactional(readOnly = true)
    public List<PartidaResponseDTO> listarPorCampeonato(String campeonatoId) {
        // Validar que o campeonato existe
        campeonatoRepository.findById(campeonatoId)
                .orElseThrow(() -> new ResourceNotFoundException("Campeonato", campeonatoId));

        List<Partida> partidas = partidaRepository.findByCampeonatoIdOrderByDataHoraAsc(campeonatoId);
        return partidas.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PartidaResponseDTO atualizarPlacar(String id, AtualizarPlacarDTO dto) {
        Partida partida = partidaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Partida", id));

        // Validar que a partida não está finalizada
        if (partida.getStatus() == StatusPartida.FINALIZADA) {
            throw new BusinessRuleException("Não é possível atualizar o placar de uma partida já finalizada");
        }

        // Atualizar placar
        partida.setPlacarEquipe1(dto.getPlacarEquipe1());
        partida.setPlacarEquipe2(dto.getPlacarEquipe2());
        partida.setStatus(StatusPartida.FINALIZADA);

        Partida partidaAtualizada = partidaRepository.save(partida);

        // Publish event
        PartidaEvent event = new PartidaEvent();
        event.setPartidaId(partidaAtualizada.getId());
        event.setCampeonatoId(partidaAtualizada.getCampeonato().getId());
        event.setCampeonatoNome(partidaAtualizada.getCampeonato().getNome());
        event.setEquipe1Nome(partidaAtualizada.getEquipe1().getNome());
        event.setEquipe2Nome(partidaAtualizada.getEquipe2().getNome());
        event.setPlacarEquipe1(partidaAtualizada.getPlacarEquipe1());
        event.setPlacarEquipe2(partidaAtualizada.getPlacarEquipe2());
        event.setTipoEvento("partida.finalizada");

        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE_NAME,
            RabbitMQConfig.ROUTING_KEY_PARTIDA_FINALIZADA,
            event
        );

        return converterParaDTO(partidaAtualizada);
    }

    @Transactional
    public PartidaResponseDTO atualizarStatus(String id, StatusPartida status) {
        Partida partida = partidaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Partida", id));

        partida.setStatus(status);
        Partida partidaAtualizada = partidaRepository.save(partida);
        return converterParaDTO(partidaAtualizada);
    }

    @Transactional
    public void deletar(String id) {
        Partida partida = partidaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Partida", id));

        partidaRepository.delete(partida);
    }

    @Transactional
    public List<PartidaResponseDTO> gerarTabelaPontosCorridos(String campeonatoId) {
        // Validar que o campeonato existe
        Campeonato campeonato = campeonatoRepository.findById(campeonatoId)
                .orElseThrow(() -> new ResourceNotFoundException("Campeonato", campeonatoId));

        // Validar que o formato é PONTOS_CORRIDOS
        if (campeonato.getFormato() != Campeonato.FormatoCampeonato.PONTOS_CORRIDOS) {
            throw new ValidationException("Só é possível gerar tabela para campeonatos no formato PONTOS_CORRIDOS");
        }

        // Buscar todas as equipes do campeonato
        List<Equipe> equipes = equipeRepository.findByCampeonatoIdOrderByNome(campeonatoId);

        // Validar mínimo de equipes
        if (equipes.size() < 2) {
            throw new ValidationException("É necessário ter pelo menos 2 equipes para gerar a tabela");
        }

        // Verificar se já existem partidas para este campeonato
        List<Partida> partidasExistentes = partidaRepository.findByCampeonatoIdOrderByDataHoraAsc(campeonatoId);
        if (!partidasExistentes.isEmpty()) {
            throw new BusinessRuleException("Já existem partidas criadas para este campeonato");
        }

        // Gerar partidas usando algoritmo round-robin
        List<Partida> todasPartidas = new ArrayList<>();
        List<Equipe> equipesRotacao = new ArrayList<>(equipes);

        // Se o número de equipes for ímpar, adicionar equipe "fantasma" para folga
        boolean numeroImpar = equipes.size() % 2 != 0;
        if (numeroImpar) {
            equipesRotacao.add(null); // null representa folga
        }

        int numeroEquipes = equipesRotacao.size();
        int numeroRodadas = numeroEquipes - 1;
        int partidasPorRodada = numeroEquipes / 2;

        // Data base para as partidas (início do campeonato)
        LocalDateTime dataBase = campeonato.getDataInicio().atStartOfDay();

        for (int rodada = 1; rodada <= numeroRodadas; rodada++) {
            // Calcular data da rodada (intervalo de 7 dias entre rodadas)
            LocalDateTime dataRodada = dataBase.plusDays((rodada - 1) * 7L);

            for (int i = 0; i < partidasPorRodada; i++) {
                Equipe equipe1 = equipesRotacao.get(i);
                Equipe equipe2 = equipesRotacao.get(numeroEquipes - 1 - i);

                // Pular se uma das equipes for null (folga)
                if (equipe1 != null && equipe2 != null) {
                    Partida partida = new Partida();
                    partida.setCampeonato(campeonato);
                    partida.setEquipe1(equipe1);
                    partida.setEquipe2(equipe2);
                    partida.setDataHora(dataRodada.plusHours(i * 2L)); // Distribuir partidas ao longo do dia
                    partida.setFase("GRUPO");
                    partida.setRodada(rodada);
                    partida.setStatus(StatusPartida.AGENDADA);

                    todasPartidas.add(partida);
                }
            }

            // Rotacionar equipes (manter primeira fixa)
            if (numeroEquipes > 2) {
                Equipe ultima = equipesRotacao.remove(numeroEquipes - 1);
                equipesRotacao.add(1, ultima);
            }
        }

        // Salvar todas as partidas
        List<Partida> partidasSalvas = partidaRepository.saveAll(todasPartidas);

        // Publish event
        PartidaEvent event = new PartidaEvent();
        event.setCampeonatoId(campeonatoId);
        event.setCampeonatoNome(campeonato.getNome());
        event.setQuantidadePartidas(partidasSalvas.size());
        event.setTipoEvento("partidas.geradas");

        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE_NAME,
            RabbitMQConfig.ROUTING_KEY_PARTIDAS_GERADAS,
            event
        );

        return partidasSalvas.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    // Método auxiliar para converter Partida em PartidaResponseDTO
    private PartidaResponseDTO converterParaDTO(Partida partida) {
        PartidaResponseDTO dto = new PartidaResponseDTO();
        dto.setId(partida.getId());
        dto.setCampeonatoId(partida.getCampeonato().getId());
        dto.setCampeonatoNome(partida.getCampeonato().getNome());

        // Convert full Equipe entities to EquipeResponseDTO (with membros)
        EquipeResponseDTO equipe1DTO = equipeService.buscarPorId(partida.getEquipe1().getId());
        dto.setEquipe1(equipe1DTO);

        EquipeResponseDTO equipe2DTO = equipeService.buscarPorId(partida.getEquipe2().getId());
        dto.setEquipe2(equipe2DTO);

        dto.setPlacarEquipe1(partida.getPlacarEquipe1());
        dto.setPlacarEquipe2(partida.getPlacarEquipe2());

        // Set dataHora which automatically populates data and horario fields
        dto.setDataHora(partida.getDataHora());

        dto.setLocal(partida.getLocal());
        dto.setFase(partida.getFase());
        dto.setRodada(partida.getRodada());
        dto.setStatus(partida.getStatus());
        dto.setDataCriacao(partida.getDataCriacao());

        return dto;
    }
}
