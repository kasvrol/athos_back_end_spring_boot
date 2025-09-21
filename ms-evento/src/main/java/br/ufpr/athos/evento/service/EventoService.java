package br.ufpr.athos.evento.service;

import br.ufpr.athos.evento.dto.EventoRequestDTO;
import br.ufpr.athos.evento.dto.EventoResponseDTO;
import br.ufpr.athos.evento.model.Evento;
import br.ufpr.athos.evento.repository.EventoRepository;
import br.ufpr.athos.evento.repository.ParticipacaoEventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private ParticipacaoEventoRepository participacaoRepository;

    public EventoResponseDTO criarEvento(EventoRequestDTO requestDTO, String criadorId) {
        validateEventoRequest(requestDTO);

        Evento evento = new Evento();
        evento.setNome(requestDTO.getNome());
        evento.setData(requestDTO.getData());
        evento.setHorario(requestDTO.getHorario());
        evento.setEsporte(requestDTO.getEsporte());
        evento.setCep(requestDTO.getCep());
        evento.setEndereco(requestDTO.getEndereco());
        evento.setPublico(requestDTO.getPublico());
        evento.setValor(requestDTO.getValor());
        evento.setDescricao(requestDTO.getDescricao());
        evento.setMaxParticipantes(requestDTO.getMaxParticipantes());
        evento.setCriadorId(criadorId);

        Evento eventoSalvo = eventoRepository.save(evento);
        return criarEventoResponseDTO(eventoSalvo, null);
    }

    @Transactional(readOnly = true)
    public Optional<EventoResponseDTO> buscarEventoPorId(String eventoId, String usuarioId) {
        Optional<Evento> eventoOpt = eventoRepository.findById(eventoId);

        if (eventoOpt.isEmpty()) {
            return Optional.empty();
        }

        Evento evento = eventoOpt.get();

        if (!evento.getPublico() && !evento.getCriadorId().equals(usuarioId)) {
            return Optional.empty();
        }

        return Optional.of(criarEventoResponseDTO(evento, usuarioId));
    }

    @Transactional(readOnly = true)
    public List<EventoResponseDTO> listarEventos(String usuarioId) {
        List<Evento> eventos = eventoRepository.findEventosVisivelParaUsuario(usuarioId);
        return eventos.stream()
                .map(evento -> criarEventoResponseDTO(evento, usuarioId))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<EventoResponseDTO> listarEventosComFiltros(String esporte, Boolean publico,
                                                          LocalDate dataInicio, LocalDate dataFim,
                                                          String cep, int page, int size,
                                                          String usuarioId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Evento> eventosPage = eventoRepository.findEventosComFiltros(
                esporte, publico, dataInicio, dataFim, cep, pageable);

        return eventosPage.map(evento -> criarEventoResponseDTO(evento, usuarioId));
    }

    @Transactional(readOnly = true)
    public List<EventoResponseDTO> buscarEventosPorNome(String nome, String usuarioId) {
        List<Evento> eventos = eventoRepository.findByNomeContainingIgnoreCase(nome);
        return eventos.stream()
                .filter(evento -> evento.getPublico() || evento.getCriadorId().equals(usuarioId))
                .map(evento -> criarEventoResponseDTO(evento, usuarioId))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EventoResponseDTO> listarEventosFuturos(String usuarioId) {
        List<Evento> eventos = eventoRepository.findEventosFuturos(LocalDate.now());
        return eventos.stream()
                .filter(evento -> evento.getPublico() || evento.getCriadorId().equals(usuarioId))
                .map(evento -> criarEventoResponseDTO(evento, usuarioId))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EventoResponseDTO> listarEventosCriadosPeloUsuario(String usuarioId) {
        List<Evento> eventos = eventoRepository.findByCriadorId(usuarioId);
        return eventos.stream()
                .map(evento -> criarEventoResponseDTO(evento, usuarioId))
                .collect(Collectors.toList());
    }

    public EventoResponseDTO atualizarEvento(String eventoId, EventoRequestDTO requestDTO, String usuarioId) {
        Optional<Evento> eventoOpt = eventoRepository.findById(eventoId);

        if (eventoOpt.isEmpty()) {
            throw new RuntimeException("Evento não encontrado");
        }

        Evento evento = eventoOpt.get();

        if (!evento.getCriadorId().equals(usuarioId)) {
            throw new RuntimeException("Usuário não tem permissão para editar este evento");
        }

        if (evento.getData().isBefore(LocalDate.now())) {
            throw new RuntimeException("Não é possível editar eventos que já ocorreram");
        }

        validateEventoRequest(requestDTO);

        evento.setNome(requestDTO.getNome());
        evento.setData(requestDTO.getData());
        evento.setHorario(requestDTO.getHorario());
        evento.setEsporte(requestDTO.getEsporte());
        evento.setCep(requestDTO.getCep());
        evento.setEndereco(requestDTO.getEndereco());
        evento.setPublico(requestDTO.getPublico());
        evento.setValor(requestDTO.getValor());
        evento.setDescricao(requestDTO.getDescricao());
        evento.setMaxParticipantes(requestDTO.getMaxParticipantes());

        Evento eventoAtualizado = eventoRepository.save(evento);
        return criarEventoResponseDTO(eventoAtualizado, usuarioId);
    }

    public void deletarEvento(String eventoId, String usuarioId) {
        Optional<Evento> eventoOpt = eventoRepository.findById(eventoId);

        if (eventoOpt.isEmpty()) {
            throw new RuntimeException("Evento não encontrado");
        }

        Evento evento = eventoOpt.get();

        if (!evento.getCriadorId().equals(usuarioId)) {
            throw new RuntimeException("Usuário não tem permissão para deletar este evento");
        }

        if (evento.getData().isBefore(LocalDate.now())) {
            throw new RuntimeException("Não é possível deletar eventos que já ocorreram");
        }

        Long participantes = participacaoRepository.countParticipantesByEvento(eventoId);
        if (participantes > 0) {
            throw new RuntimeException("Não é possível deletar evento com participantes inscritos");
        }

        eventoRepository.deleteById(eventoId);
    }

    @Transactional(readOnly = true)
    public boolean verificarPermissaoEdicao(String eventoId, String usuarioId) {
        return eventoRepository.isUsuarioCriadorDoEvento(eventoId, usuarioId);
    }

    @Transactional(readOnly = true)
    public boolean eventoExiste(String eventoId) {
        return eventoRepository.existsById(eventoId);
    }

    @Transactional(readOnly = true)
    public boolean eventoEPublico(String eventoId) {
        Optional<Evento> eventoOpt = eventoRepository.findById(eventoId);
        return eventoOpt.map(Evento::getPublico).orElse(false);
    }

    @Transactional(readOnly = true)
    public boolean eventoJaOcorreu(String eventoId) {
        Optional<Evento> eventoOpt = eventoRepository.findById(eventoId);
        return eventoOpt.map(evento -> evento.getData().isBefore(LocalDate.now())).orElse(false);
    }

    @Transactional(readOnly = true)
    public boolean eventoPossuiVagas(String eventoId) {
        Optional<Evento> eventoOpt = eventoRepository.findById(eventoId);
        if (eventoOpt.isEmpty()) {
            return false;
        }

        Evento evento = eventoOpt.get();
        if (evento.getMaxParticipantes() == null) {
            return true;
        }

        Long participantesConfirmados = participacaoRepository.countParticipantesConfirmadosByEvento(eventoId);
        return participantesConfirmados < evento.getMaxParticipantes();
    }

    private EventoResponseDTO criarEventoResponseDTO(Evento evento, String usuarioId) {
        Long totalParticipantes = participacaoRepository.countParticipantesByEvento(evento.getId());
        Long participantesConfirmados = participacaoRepository.countParticipantesConfirmadosByEvento(evento.getId());

        Boolean usuarioInscrito = false;
        Boolean usuarioECriador = false;

        if (usuarioId != null) {
            usuarioInscrito = participacaoRepository.existsByUsuarioIdAndEventoId(usuarioId, evento.getId());
            usuarioECriador = evento.getCriadorId().equals(usuarioId);
        }

        return new EventoResponseDTO(evento, totalParticipantes, participantesConfirmados,
                                   usuarioInscrito, usuarioECriador);
    }

    private void validateEventoRequest(EventoRequestDTO requestDTO) {
        if (requestDTO.getData().isBefore(LocalDate.now())) {
            throw new RuntimeException("Data do evento deve ser futura");
        }

        if (requestDTO.getMaxParticipantes() != null && requestDTO.getMaxParticipantes() < 2) {
            throw new RuntimeException("Evento deve permitir pelo menos 2 participantes");
        }
    }
}