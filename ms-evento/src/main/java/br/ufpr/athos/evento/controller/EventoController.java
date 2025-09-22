package br.ufpr.athos.evento.controller;

import br.ufpr.athos.evento.dto.EventoRequestDTO;
import br.ufpr.athos.evento.dto.EventoResponseDTO;
import br.ufpr.athos.evento.service.EventoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/eventos")
public class EventoController {

    @Autowired
    private EventoService eventoService;

    @PostMapping
    public ResponseEntity<EventoResponseDTO> criarEvento(
            @Valid @RequestBody EventoRequestDTO requestDTO,
            @RequestHeader("X-User-Id") String usuarioId) {
        try {
            EventoResponseDTO evento = eventoService.criarEvento(requestDTO, usuarioId);
            return ResponseEntity.status(HttpStatus.CREATED).body(evento);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{eventoId}")
    public ResponseEntity<EventoResponseDTO> buscarEventoPorId(
            @PathVariable String eventoId,
            @RequestHeader("X-User-Id") String usuarioId) {
        Optional<EventoResponseDTO> evento = eventoService.buscarEventoPorId(eventoId, usuarioId);
        return evento.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<EventoResponseDTO>> listarEventos(
            @RequestHeader("X-User-Id") String usuarioId) {
        List<EventoResponseDTO> eventos = eventoService.listarEventos(usuarioId);
        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<EventoResponseDTO>> buscarEventosComFiltros(
            @RequestParam(required = false) String esporte,
            @RequestParam(required = false) Boolean publico,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @RequestParam(required = false) String cep,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader("X-User-Id") String usuarioId) {

        Page<EventoResponseDTO> eventos = eventoService.listarEventosComFiltros(
                esporte, publico, dataInicio, dataFim, cep, page, size, usuarioId);
        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/search/nome")
    public ResponseEntity<List<EventoResponseDTO>> buscarEventosPorNome(
            @RequestParam String nome,
            @RequestHeader("X-User-Id") String usuarioId) {
        List<EventoResponseDTO> eventos = eventoService.buscarEventosPorNome(nome, usuarioId);
        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/futuros")
    public ResponseEntity<List<EventoResponseDTO>> listarEventosFuturos(
            @RequestHeader("X-User-Id") String usuarioId) {
        List<EventoResponseDTO> eventos = eventoService.listarEventosFuturos(usuarioId);
        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/meus-eventos")
    public ResponseEntity<List<EventoResponseDTO>> listarMeusEventos(
            @RequestHeader("X-User-Id") String usuarioId) {
        List<EventoResponseDTO> eventos = eventoService.listarEventosCriadosPeloUsuario(usuarioId);
        return ResponseEntity.ok(eventos);
    }

    @PutMapping("/{eventoId}")
    public ResponseEntity<EventoResponseDTO> atualizarEvento(
            @PathVariable String eventoId,
            @Valid @RequestBody EventoRequestDTO requestDTO,
            @RequestHeader("X-User-Id") String usuarioId) {
        try {
            EventoResponseDTO evento = eventoService.atualizarEvento(eventoId, requestDTO, usuarioId);
            return ResponseEntity.ok(evento);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @DeleteMapping("/{eventoId}")
    public ResponseEntity<Void> deletarEvento(
            @PathVariable String eventoId,
            @RequestHeader("X-User-Id") String usuarioId) {
        try {
            eventoService.deletarEvento(eventoId, usuarioId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/{eventoId}/permissao-edicao")
    public ResponseEntity<Boolean> verificarPermissaoEdicao(
            @PathVariable String eventoId,
            @RequestHeader("X-User-Id") String usuarioId) {
        boolean temPermissao = eventoService.verificarPermissaoEdicao(eventoId, usuarioId);
        return ResponseEntity.ok(temPermissao);
    }

    @GetMapping("/{eventoId}/vagas-disponiveis")
    public ResponseEntity<Boolean> verificarVagasDisponiveis(@PathVariable String eventoId) {
        boolean possuiVagas = eventoService.eventoPossuiVagas(eventoId);
        return ResponseEntity.ok(possuiVagas);
    }
}