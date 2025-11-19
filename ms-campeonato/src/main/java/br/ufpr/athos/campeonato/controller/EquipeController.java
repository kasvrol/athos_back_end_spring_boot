package br.ufpr.athos.campeonato.controller;

import br.ufpr.athos.campeonato.dto.AdicionarMembroDTO;
import br.ufpr.athos.campeonato.dto.EquipeRequestDTO;
import br.ufpr.athos.campeonato.dto.EquipeResponseDTO;
import br.ufpr.athos.campeonato.dto.MembroEquipeResponseDTO;
import br.ufpr.athos.campeonato.service.EquipeService;
import br.ufpr.athos.campeonato.service.MembroEquipeService;
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

    @Autowired
    private MembroEquipeService membroService;

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

    @PostMapping("/{id}/membros")
    public ResponseEntity<MembroEquipeResponseDTO> adicionarMembro(
            @PathVariable String id,
            @Valid @RequestBody AdicionarMembroDTO dto
    ) {
        MembroEquipeResponseDTO response = membroService.adicionarMembro(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}/membros")
    public ResponseEntity<List<MembroEquipeResponseDTO>> listarMembros(@PathVariable String id) {
        return ResponseEntity.ok(membroService.listarMembros(id));
    }

    @DeleteMapping("/{id}/membros/{membroId}")
    public ResponseEntity<Void> removerMembro(
            @PathVariable String id,
            @PathVariable String membroId
    ) {
        membroService.removerMembro(id, membroId);
        return ResponseEntity.noContent().build();
    }
}
