package br.ufpr.athos.evento.controller;

import br.ufpr.athos.evento.dto.InscricaoRequestDTO;
import br.ufpr.athos.evento.dto.ParticipacaoResponseDTO;
import br.ufpr.athos.evento.service.InscricaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/inscricoes")
public class InscricaoController {

    @Autowired
    private InscricaoService inscricaoService;

    @PostMapping("/eventos/{eventoId}")
    public ResponseEntity<ParticipacaoResponseDTO> inscreverSeEmEvento(
            @PathVariable String eventoId,
            @RequestBody(required = false) @Valid InscricaoRequestDTO requestDTO,
            @RequestHeader("X-Usuario-Id") String usuarioId) {
        try {
            ParticipacaoResponseDTO participacao = inscricaoService.inscreverUsuarioEmEvento(
                    usuarioId, eventoId, requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(participacao);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/eventos/{eventoId}/confirmar")
    public ResponseEntity<ParticipacaoResponseDTO> confirmarParticipacao(
            @PathVariable String eventoId,
            @RequestHeader("X-Usuario-Id") String usuarioId) {
        try {
            ParticipacaoResponseDTO participacao = inscricaoService.confirmarParticipacao(usuarioId, eventoId);
            return ResponseEntity.ok(participacao);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/eventos/{eventoId}/confirmar-pagamento")
    public ResponseEntity<ParticipacaoResponseDTO> confirmarPagamento(
            @PathVariable String eventoId,
            @RequestHeader("X-Usuario-Id") String usuarioId) {
        try {
            ParticipacaoResponseDTO participacao = inscricaoService.confirmarPagamento(usuarioId, eventoId);
            return ResponseEntity.ok(participacao);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/eventos/{eventoId}")
    public ResponseEntity<Void> cancelarInscricao(
            @PathVariable String eventoId,
            @RequestHeader("X-Usuario-Id") String usuarioId) {
        try {
            inscricaoService.cancelarInscricao(usuarioId, eventoId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/eventos/{eventoId}")
    public ResponseEntity<ParticipacaoResponseDTO> buscarMinhaParticipacao(
            @PathVariable String eventoId,
            @RequestHeader("X-Usuario-Id") String usuarioId) {
        Optional<ParticipacaoResponseDTO> participacao = inscricaoService.buscarParticipacao(usuarioId, eventoId);
        return participacao.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/meus-eventos")
    public ResponseEntity<List<ParticipacaoResponseDTO>> listarMeusEventos(
            @RequestHeader("X-Usuario-Id") String usuarioId) {
        List<ParticipacaoResponseDTO> participacoes = inscricaoService.listarEventosDoUsuario(usuarioId);
        return ResponseEntity.ok(participacoes);
    }

    @GetMapping("/meus-eventos/futuros")
    public ResponseEntity<List<ParticipacaoResponseDTO>> listarMeusEventosFuturos(
            @RequestHeader("X-Usuario-Id") String usuarioId) {
        List<ParticipacaoResponseDTO> participacoes = inscricaoService.listarEventosFuturosDoUsuario(usuarioId);
        return ResponseEntity.ok(participacoes);
    }

    @GetMapping("/meus-eventos/passados")
    public ResponseEntity<List<ParticipacaoResponseDTO>> listarMeusEventosPassados(
            @RequestHeader("X-Usuario-Id") String usuarioId) {
        List<ParticipacaoResponseDTO> participacoes = inscricaoService.listarEventosPassadosDoUsuario(usuarioId);
        return ResponseEntity.ok(participacoes);
    }

    @GetMapping("/eventos/{eventoId}/participantes")
    public ResponseEntity<List<ParticipacaoResponseDTO>> listarParticipantesDoEvento(
            @PathVariable String eventoId) {
        List<ParticipacaoResponseDTO> participantes = inscricaoService.listarParticipantesDoEvento(eventoId);
        return ResponseEntity.ok(participantes);
    }

    @GetMapping("/eventos/{eventoId}/participantes/confirmados")
    public ResponseEntity<List<ParticipacaoResponseDTO>> listarParticipantesConfirmados(
            @PathVariable String eventoId) {
        List<ParticipacaoResponseDTO> participantes = inscricaoService.listarParticipantesConfirmados(eventoId);
        return ResponseEntity.ok(participantes);
    }

    @GetMapping("/eventos/{eventoId}/status")
    public ResponseEntity<StatusInscricaoDTO> verificarStatusInscricao(
            @PathVariable String eventoId,
            @RequestHeader("X-Usuario-Id") String usuarioId) {

        boolean estaInscrito = inscricaoService.usuarioEstaInscrito(usuarioId, eventoId);
        boolean podeAvaliar = inscricaoService.usuarioPodeAvaliar(usuarioId, eventoId);
        Long totalParticipantes = inscricaoService.contarParticipantes(eventoId);
        Long participantesConfirmados = inscricaoService.contarParticipantesConfirmados(eventoId);

        StatusInscricaoDTO status = new StatusInscricaoDTO(
                estaInscrito, podeAvaliar, totalParticipantes, participantesConfirmados);

        return ResponseEntity.ok(status);
    }

    // DTO para resposta de status
    public static class StatusInscricaoDTO {
        private boolean usuarioInscrito;
        private boolean usuarioPodeAvaliar;
        private Long totalParticipantes;
        private Long participantesConfirmados;

        public StatusInscricaoDTO(boolean usuarioInscrito, boolean usuarioPodeAvaliar,
                                Long totalParticipantes, Long participantesConfirmados) {
            this.usuarioInscrito = usuarioInscrito;
            this.usuarioPodeAvaliar = usuarioPodeAvaliar;
            this.totalParticipantes = totalParticipantes;
            this.participantesConfirmados = participantesConfirmados;
        }

        // Getters
        public boolean isUsuarioInscrito() { return usuarioInscrito; }
        public boolean isUsuarioPodeAvaliar() { return usuarioPodeAvaliar; }
        public Long getTotalParticipantes() { return totalParticipantes; }
        public Long getParticipantesConfirmados() { return participantesConfirmados; }
    }
}