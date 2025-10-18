package br.ufpr.athos.notificacao.service;

import br.ufpr.athos.notificacao.dto.NotificacaoRequestDTO;
import br.ufpr.athos.notificacao.dto.NotificacaoResponseDTO;
import br.ufpr.athos.notificacao.model.Notificacao;
import br.ufpr.athos.notificacao.repository.NotificacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificacaoService {

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    public NotificacaoResponseDTO criarNotificacao(NotificacaoRequestDTO request) {
        Notificacao notificacao = new Notificacao();
        notificacao.setUsuarioId(request.getUsuarioId());
        notificacao.setTitulo(request.getTitulo());
        notificacao.setMensagem(request.getMensagem());
        notificacao.setTipo(Notificacao.TipoNotificacao.valueOf(request.getTipo()));
        notificacao.setEventoRelacionadoId(request.getEventoRelacionadoId());

        Notificacao salva = notificacaoRepository.save(notificacao);
        return new NotificacaoResponseDTO(salva);
    }

    public void criarNotificacao(String usuarioId, String titulo, String mensagem,
                                  Notificacao.TipoNotificacao tipo, String eventoRelacionadoId) {
        Notificacao notificacao = new Notificacao(usuarioId, titulo, mensagem, tipo, eventoRelacionadoId);
        notificacaoRepository.save(notificacao);
    }

    public List<NotificacaoResponseDTO> listarNotificacoesPorUsuario(String usuarioId) {
        return notificacaoRepository.findByUsuarioIdOrderByDataCriacaoDesc(usuarioId)
                .stream()
                .map(NotificacaoResponseDTO::new)
                .collect(Collectors.toList());
    }

    public List<NotificacaoResponseDTO> listarNotificacoesNaoLidas(String usuarioId) {
        return notificacaoRepository.findByUsuarioIdAndLidaOrderByDataCriacaoDesc(usuarioId, false)
                .stream()
                .map(NotificacaoResponseDTO::new)
                .collect(Collectors.toList());
    }

    public Long contarNotificacoesNaoLidas(String usuarioId) {
        return notificacaoRepository.countByUsuarioIdAndLida(usuarioId, false);
    }

    public NotificacaoResponseDTO marcarComoLida(String notificacaoId) {
        Notificacao notificacao = notificacaoRepository.findById(notificacaoId)
                .orElseThrow(() -> new RuntimeException("Notificação não encontrada"));

        notificacao.setLida(true);
        Notificacao atualizada = notificacaoRepository.save(notificacao);
        return new NotificacaoResponseDTO(atualizada);
    }

    public void marcarTodasComoLidas(String usuarioId) {
        List<Notificacao> naoLidas = notificacaoRepository.findByUsuarioIdAndLidaOrderByDataCriacaoDesc(usuarioId, false);
        naoLidas.forEach(n -> n.setLida(true));
        notificacaoRepository.saveAll(naoLidas);
    }

    public void deletarNotificacao(String notificacaoId) {
        notificacaoRepository.deleteById(notificacaoId);
    }
}
