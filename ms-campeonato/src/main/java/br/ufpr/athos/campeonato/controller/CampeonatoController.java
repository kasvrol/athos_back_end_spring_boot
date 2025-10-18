package br.ufpr.athos.campeonato.controller;

import br.ufpr.athos.campeonato.dto.CampeonatoRequestDTO;
import br.ufpr.athos.campeonato.dto.CampeonatoResponseDTO;
import br.ufpr.athos.campeonato.service.CampeonatoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/campeonatos")
public class CampeonatoController {

    @Autowired
    private CampeonatoService campeonatoService;

    @PostMapping
    public ResponseEntity<CampeonatoResponseDTO> criarCampeonato(@Valid @RequestBody CampeonatoRequestDTO request) {
        CampeonatoResponseDTO campeonato = campeonatoService.criarCampeonato(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(campeonato);
    }

    @GetMapping
    public ResponseEntity<List<CampeonatoResponseDTO>> listarTodos() {
        List<CampeonatoResponseDTO> campeonatos = campeonatoService.listarTodos();
        return ResponseEntity.ok(campeonatos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CampeonatoResponseDTO> buscarPorId(@PathVariable String id) {
        try {
            CampeonatoResponseDTO campeonato = campeonatoService.buscarPorId(id);
            return ResponseEntity.ok(campeonato);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/organizador/{organizadorId}")
    public ResponseEntity<List<CampeonatoResponseDTO>> listarPorOrganizador(@PathVariable String organizadorId) {
        List<CampeonatoResponseDTO> campeonatos = campeonatoService.listarPorOrganizador(organizadorId);
        return ResponseEntity.ok(campeonatos);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<CampeonatoResponseDTO> atualizarStatus(
            @PathVariable String id,
            @RequestBody Map<String, String> body) {
        try {
            String novoStatus = body.get("status");
            CampeonatoResponseDTO campeonato = campeonatoService.atualizarStatus(id, novoStatus);
            return ResponseEntity.ok(campeonato);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCampeonato(@PathVariable String id) {
        campeonatoService.deletarCampeonato(id);
        return ResponseEntity.noContent().build();
    }
}
