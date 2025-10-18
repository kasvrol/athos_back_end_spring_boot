package br.ufpr.athos.campeonato.controller;

import br.ufpr.athos.campeonato.dto.EquipeRequestDTO;
import br.ufpr.athos.campeonato.dto.EquipeResponseDTO;
import br.ufpr.athos.campeonato.service.EquipeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipes")
public class EquipeController {

    @Autowired
    private EquipeService equipeService;

    @PostMapping
    public ResponseEntity<EquipeResponseDTO> criarEquipe(@Valid @RequestBody EquipeRequestDTO request) {
        try {
            EquipeResponseDTO equipe = equipeService.criarEquipe(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(equipe);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/campeonato/{campeonatoId}")
    public ResponseEntity<List<EquipeResponseDTO>> listarPorCampeonato(@PathVariable String campeonatoId) {
        List<EquipeResponseDTO> equipes = equipeService.listarPorCampeonato(campeonatoId);
        return ResponseEntity.ok(equipes);
    }

    @GetMapping("/capitao/{capitaoId}")
    public ResponseEntity<List<EquipeResponseDTO>> listarPorCapitao(@PathVariable String capitaoId) {
        List<EquipeResponseDTO> equipes = equipeService.listarPorCapitao(capitaoId);
        return ResponseEntity.ok(equipes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipeResponseDTO> buscarPorId(@PathVariable String id) {
        try {
            EquipeResponseDTO equipe = equipeService.buscarPorId(id);
            return ResponseEntity.ok(equipe);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEquipe(@PathVariable String id) {
        equipeService.deletarEquipe(id);
        return ResponseEntity.noContent().build();
    }
}
