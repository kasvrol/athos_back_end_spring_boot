package br.ufpr.athos.campeonato.controller;

import br.ufpr.athos.campeonato.dto.AtualizarPlacarDTO;
import br.ufpr.athos.campeonato.dto.PartidaRequestDTO;
import br.ufpr.athos.campeonato.dto.PartidaResponseDTO;
import br.ufpr.athos.campeonato.service.PartidaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/partidas")
@CrossOrigin(origins = "*")
public class PartidaController {

    @Autowired
    private PartidaService partidaService;

    @PostMapping
    public ResponseEntity<PartidaResponseDTO> criar(@Valid @RequestBody PartidaRequestDTO dto) {
        PartidaResponseDTO partida = partidaService.criarPartida(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(partida);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartidaResponseDTO> buscar(@PathVariable String id) {
        PartidaResponseDTO partida = partidaService.buscarPorId(id);
        return ResponseEntity.ok(partida);
    }

    @GetMapping("/campeonato/{id}")
    public ResponseEntity<List<PartidaResponseDTO>> listar(@PathVariable String id) {
        List<PartidaResponseDTO> partidas = partidaService.listarPorCampeonato(id);
        return ResponseEntity.ok(partidas);
    }

    @PostMapping("/gerar")
    public ResponseEntity<List<PartidaResponseDTO>> gerarTabela(@RequestParam String campeonatoId) {
        List<PartidaResponseDTO> partidas = partidaService.gerarTabelaPontosCorridos(campeonatoId);
        return ResponseEntity.status(HttpStatus.CREATED).body(partidas);
    }

    @PatchMapping("/{id}/placar")
    public ResponseEntity<PartidaResponseDTO> atualizarPlacar(
            @PathVariable String id,
            @Valid @RequestBody AtualizarPlacarDTO dto) {
        PartidaResponseDTO partida = partidaService.atualizarPlacar(id, dto);
        return ResponseEntity.ok(partida);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        partidaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
