package br.ufpr.athos.campeonato.service;

import br.ufpr.athos.campeonato.config.RabbitMQConfig;
import br.ufpr.athos.campeonato.dto.CampeonatoRequestDTO;
import br.ufpr.athos.campeonato.dto.CampeonatoResponseDTO;
import br.ufpr.athos.campeonato.event.CampeonatoEvent;
import br.ufpr.athos.campeonato.exception.ResourceNotFoundException;
import br.ufpr.athos.campeonato.model.Campeonato;
import br.ufpr.athos.campeonato.repository.CampeonatoRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CampeonatoService {

    @Autowired
    private CampeonatoRepository campeonatoRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public CampeonatoResponseDTO criarCampeonato(CampeonatoRequestDTO request) {
        Campeonato campeonato = new Campeonato();
        campeonato.setNome(request.getNome());
        campeonato.setEsporte(request.getEsporte());
        campeonato.setDataInicio(request.getDataInicio());
        campeonato.setDataFim(request.getDataFim());
        campeonato.setFormato(Campeonato.FormatoCampeonato.valueOf(request.getFormato()));
        campeonato.setOrganizadorId(request.getOrganizadorId());
        campeonato.setDescricao(request.getDescricao());
        campeonato.setMaxEquipes(request.getMaxEquipes());

        Campeonato salvo = campeonatoRepository.save(campeonato);

        // Publish event
        CampeonatoEvent event = new CampeonatoEvent();
        event.setCampeonatoId(salvo.getId());
        event.setCampeonatoNome(salvo.getNome());
        event.setOrganizadorId(salvo.getOrganizadorId());
        event.setDataInicio(salvo.getDataInicio());
        event.setTipoEvento("campeonato.criado");

        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE_NAME,
            RabbitMQConfig.ROUTING_KEY_CAMPEONATO_CRIADO,
            event
        );

        return new CampeonatoResponseDTO(salvo);
    }

    public List<CampeonatoResponseDTO> listarTodos() {
        return campeonatoRepository.findAll()
                .stream()
                .map(CampeonatoResponseDTO::new)
                .collect(Collectors.toList());
    }

    public CampeonatoResponseDTO buscarPorId(String id) {
        Campeonato campeonato = campeonatoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Campeonato", id));
        return new CampeonatoResponseDTO(campeonato);
    }

    public List<CampeonatoResponseDTO> listarPorOrganizador(String organizadorId) {
        return campeonatoRepository.findByOrganizadorIdOrderByDataCriacaoDesc(organizadorId)
                .stream()
                .map(CampeonatoResponseDTO::new)
                .collect(Collectors.toList());
    }

    public CampeonatoResponseDTO atualizarStatus(String id, String novoStatus) {
        Campeonato campeonato = campeonatoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Campeonato", id));

        Campeonato.StatusCampeonato statusAntigo = campeonato.getStatus();
        campeonato.setStatus(Campeonato.StatusCampeonato.valueOf(novoStatus));
        Campeonato atualizado = campeonatoRepository.save(campeonato);

        // Publish event
        CampeonatoEvent event = new CampeonatoEvent();
        event.setCampeonatoId(atualizado.getId());
        event.setCampeonatoNome(atualizado.getNome());
        event.setOrganizadorId(atualizado.getOrganizadorId());
        event.setStatusAntigo(statusAntigo.toString());
        event.setStatusNovo(novoStatus);
        event.setTipoEvento("campeonato.status.alterado");

        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE_NAME,
            RabbitMQConfig.ROUTING_KEY_CAMPEONATO_STATUS,
            event
        );

        return new CampeonatoResponseDTO(atualizado);
    }

    public void deletarCampeonato(String id) {
        campeonatoRepository.deleteById(id);
    }
}
