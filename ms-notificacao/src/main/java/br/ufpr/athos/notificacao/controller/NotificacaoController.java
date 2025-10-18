package br.ufpr.athos.notificacao.controller;

import br.ufpr.athos.notificacao.dto.NotificacaoRequestDTO;
import br.ufpr.athos.notificacao.dto.NotificacaoResponseDTO;
import br.ufpr.athos.notificacao.service.NotificacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notificacoes")
public class NotificacaoController {

    @Autowired
    private NotificacaoService notificacaoService;

    @PostMapping
    public ResponseEntity<NotificacaoResponseDTO> criarNotificacao(@Valid @RequestBody NotificacaoRequestDTO request) {
        NotificacaoResponseDTO notificacao = notificacaoService.criarNotificacao(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(notificacao);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<NotificacaoResponseDTO>> listarNotificacoes(@PathVariable String usuarioId) {
        List<NotificacaoResponseDTO> notificacoes = notificacaoService.listarNotificacoesPorUsuario(usuarioId);
        return ResponseEntity.ok(notificacoes);
    }

    @GetMapping("/usuario/{usuarioId}/nao-lidas")
    public ResponseEntity<List<NotificacaoResponseDTO>> listarNotificacoesNaoLidas(@PathVariable String usuarioId) {
        List<NotificacaoResponseDTO> notificacoes = notificacaoService.listarNotificacoesNaoLidas(usuarioId);
        return ResponseEntity.ok(notificacoes);
    }

    @GetMapping("/usuario/{usuarioId}/nao-lidas/count")
    public ResponseEntity<Map<String, Long>> contarNotificacoesNaoLidas(@PathVariable String usuarioId) {
        Long count = notificacaoService.contarNotificacoesNaoLidas(usuarioId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    @PatchMapping("/{notificacaoId}/lida")
    public ResponseEntity<NotificacaoResponseDTO> marcarComoLida(@PathVariable String notificacaoId) {
        NotificacaoResponseDTO notificacao = notificacaoService.marcarComoLida(notificacaoId);
        return ResponseEntity.ok(notificacao);
    }

    @PatchMapping("/usuario/{usuarioId}/marcar-todas-lidas")
    public ResponseEntity<Void> marcarTodasComoLidas(@PathVariable String usuarioId) {
        notificacaoService.marcarTodasComoLidas(usuarioId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{notificacaoId}")
    public ResponseEntity<Void> deletarNotificacao(@PathVariable String notificacaoId) {
        notificacaoService.deletarNotificacao(notificacaoId);
        return ResponseEntity.noContent().build();
    }
}
