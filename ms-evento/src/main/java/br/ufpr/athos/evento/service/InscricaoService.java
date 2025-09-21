package br.ufpr.athos.evento.service;

import br.ufpr.athos.evento.dto.InscricaoRequestDTO;
import br.ufpr.athos.evento.dto.ParticipacaoResponseDTO;
import br.ufpr.athos.evento.model.ParticipacaoEvento;
import br.ufpr.athos.evento.repository.ParticipacaoEventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class InscricaoService {

    @Autowired
    private ParticipacaoEventoRepository participacaoRepository;

    @Autowired
    private EventoService eventoService;

    public ParticipacaoResponseDTO inscreverUsuarioEmEvento(String usuarioId, String eventoId,
                                                          InscricaoRequestDTO requestDTO) {

        validateInscricao(usuarioId, eventoId);

        ParticipacaoEvento participacao = new ParticipacaoEvento(usuarioId, eventoId);
        if (requestDTO != null && requestDTO.getObservacoes() != null) {
            participacao.setObservacoes(requestDTO.getObservacoes());
        }

        ParticipacaoEvento participacaoSalva = participacaoRepository.save(participacao);
        return new ParticipacaoResponseDTO(participacaoSalva);
    }

    public ParticipacaoResponseDTO confirmarParticipacao(String usuarioId, String eventoId) {
        ParticipacaoEvento participacao = buscarParticipacaoOuFalhar(usuarioId, eventoId);

        if (participacao.getConfirmado()) {
            throw new RuntimeException("Participação já confirmada");
        }

        if (eventoService.eventoJaOcorreu(eventoId)) {
            throw new RuntimeException("Não é possível confirmar participação em evento que já ocorreu");
        }

        if (!eventoService.eventoPossuiVagas(eventoId)) {
            throw new RuntimeException("Evento não possui mais vagas disponíveis");
        }

        participacao.confirmarParticipacao();
        ParticipacaoEvento participacaoAtualizada = participacaoRepository.save(participacao);
        return new ParticipacaoResponseDTO(participacaoAtualizada);
    }

    public ParticipacaoResponseDTO confirmarPagamento(String usuarioId, String eventoId) {
        ParticipacaoEvento participacao = buscarParticipacaoOuFalhar(usuarioId, eventoId);

        if (participacao.getPago()) {
            throw new RuntimeException("Pagamento já confirmado");
        }

        if (!participacao.getConfirmado()) {
            throw new RuntimeException("Participação deve ser confirmada antes do pagamento");
        }

        participacao.confirmarPagamento();
        ParticipacaoEvento participacaoAtualizada = participacaoRepository.save(participacao);
        return new ParticipacaoResponseDTO(participacaoAtualizada);
    }

    public void cancelarInscricao(String usuarioId, String eventoId) {
        ParticipacaoEvento participacao = buscarParticipacaoOuFalhar(usuarioId, eventoId);

        if (eventoService.eventoJaOcorreu(eventoId)) {
            throw new RuntimeException("Não é possível cancelar inscrição em evento que já ocorreu");
        }

        participacaoRepository.deleteByUsuarioIdAndEventoId(usuarioId, eventoId);
    }

    @Transactional(readOnly = true)
    public Optional<ParticipacaoResponseDTO> buscarParticipacao(String usuarioId, String eventoId) {
        Optional<ParticipacaoEvento> participacaoOpt =
            participacaoRepository.findByUsuarioIdAndEventoId(usuarioId, eventoId);

        return participacaoOpt.map(ParticipacaoResponseDTO::new);
    }

    @Transactional(readOnly = true)
    public List<ParticipacaoResponseDTO> listarParticipantesDoEvento(String eventoId) {
        List<ParticipacaoEvento> participacoes =
            participacaoRepository.findByEventoIdWithEvento(eventoId);

        return participacoes.stream()
                .map(ParticipacaoResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ParticipacaoResponseDTO> listarParticipantesConfirmados(String eventoId) {
        List<ParticipacaoEvento> participacoes =
            participacaoRepository.findByEventoIdAndConfirmadoTrue(eventoId);

        return participacoes.stream()
                .map(ParticipacaoResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ParticipacaoResponseDTO> listarEventosDoUsuario(String usuarioId) {
        List<ParticipacaoEvento> participacoes =
            participacaoRepository.findEventosConfirmadosDoUsuario(usuarioId);

        return participacoes.stream()
                .map(ParticipacaoResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ParticipacaoResponseDTO> listarEventosFuturosDoUsuario(String usuarioId) {
        List<ParticipacaoEvento> participacoes =
            participacaoRepository.findEventosFuturosDoUsuario(usuarioId);

        return participacoes.stream()
                .map(ParticipacaoResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ParticipacaoResponseDTO> listarEventosPassadosDoUsuario(String usuarioId) {
        List<ParticipacaoEvento> participacoes =
            participacaoRepository.findEventosPassadosDoUsuario(usuarioId);

        return participacoes.stream()
                .map(ParticipacaoResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public boolean usuarioEstaInscrito(String usuarioId, String eventoId) {
        return participacaoRepository.existsByUsuarioIdAndEventoId(usuarioId, eventoId);
    }

    @Transactional(readOnly = true)
    public boolean usuarioPodeAvaliar(String usuarioId, String eventoId) {
        return participacaoRepository.podeAvaliarParticipantes(usuarioId, eventoId);
    }

    @Transactional(readOnly = true)
    public Long contarParticipantes(String eventoId) {
        return participacaoRepository.countParticipantesByEvento(eventoId);
    }

    @Transactional(readOnly = true)
    public Long contarParticipantesConfirmados(String eventoId) {
        return participacaoRepository.countParticipantesConfirmadosByEvento(eventoId);
    }

    private void validateInscricao(String usuarioId, String eventoId) {
        if (!eventoService.eventoExiste(eventoId)) {
            throw new RuntimeException("Evento não encontrado");
        }

        if (usuarioEstaInscrito(usuarioId, eventoId)) {
            throw new RuntimeException("Usuário já está inscrito neste evento");
        }

        if (eventoService.eventoJaOcorreu(eventoId)) {
            throw new RuntimeException("Não é possível se inscrever em evento que já ocorreu");
        }

        if (!eventoService.eventoPossuiVagas(eventoId)) {
            throw new RuntimeException("Evento não possui mais vagas disponíveis");
        }

        if (!eventoService.eventoEPublico(eventoId)) {
            throw new RuntimeException("Evento privado requer convite");
        }
    }

    private ParticipacaoEvento buscarParticipacaoOuFalhar(String usuarioId, String eventoId) {
        return participacaoRepository.findByUsuarioIdAndEventoId(usuarioId, eventoId)
                .orElseThrow(() -> new RuntimeException("Participação não encontrada"));
    }
}