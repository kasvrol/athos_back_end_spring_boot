package br.ufpr.athos.avaliacao.controller;

import br.ufpr.athos.avaliacao.dto.AvaliacaoRequestDTO;
import br.ufpr.athos.avaliacao.dto.AvaliacaoResponseDTO;
import br.ufpr.athos.avaliacao.dto.MediaAvaliacaoDTO;
import br.ufpr.athos.avaliacao.service.AvaliacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/avaliacoes")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService avaliacaoService;

    @PostMapping
    public ResponseEntity<AvaliacaoResponseDTO> criarAvaliacao(@Valid @RequestBody AvaliacaoRequestDTO request) {
        try {
            AvaliacaoResponseDTO avaliacao = avaliacaoService.criarAvaliacao(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(avaliacao);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/recebidas/{usuarioId}")
    public ResponseEntity<List<AvaliacaoResponseDTO>> listarAvaliacoesRecebidas(@PathVariable String usuarioId) {
        List<AvaliacaoResponseDTO> avaliacoes = avaliacaoService.listarAvaliacoesRecebidas(usuarioId);
        return ResponseEntity.ok(avaliacoes);
    }

    @GetMapping("/feitas/{usuarioId}")
    public ResponseEntity<List<AvaliacaoResponseDTO>> listarAvaliacoesFeitas(@PathVariable String usuarioId) {
        List<AvaliacaoResponseDTO> avaliacoes = avaliacaoService.listarAvaliacoesFeitas(usuarioId);
        return ResponseEntity.ok(avaliacoes);
    }

    @GetMapping("/evento/{eventoId}")
    public ResponseEntity<List<AvaliacaoResponseDTO>> listarAvaliacoesPorEvento(@PathVariable String eventoId) {
        List<AvaliacaoResponseDTO> avaliacoes = avaliacaoService.listarAvaliacoesPorEvento(eventoId);
        return ResponseEntity.ok(avaliacoes);
    }

    @GetMapping("/media/{usuarioId}")
    public ResponseEntity<MediaAvaliacaoDTO> calcularMediaAvaliacao(@PathVariable String usuarioId) {
        MediaAvaliacaoDTO media = avaliacaoService.calcularMediaAvaliacao(usuarioId);
        return ResponseEntity.ok(media);
    }

    @DeleteMapping("/{avaliacaoId}")
    public ResponseEntity<Void> deletarAvaliacao(@PathVariable String avaliacaoId) {
        avaliacaoService.deletarAvaliacao(avaliacaoId);
        return ResponseEntity.noContent().build();
    }
}
