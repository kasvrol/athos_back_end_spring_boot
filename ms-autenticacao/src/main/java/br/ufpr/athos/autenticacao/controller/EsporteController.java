package br.ufpr.athos.autenticacao.controller;

import br.ufpr.athos.autenticacao.dto.EsporteResponseDTO;
import br.ufpr.athos.autenticacao.services.EsporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/esportes")
@CrossOrigin(origins = "*")
public class EsporteController {

    @Autowired
    private EsporteService service;

    @GetMapping
    public ResponseEntity<List<EsporteResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/validar")
    public ResponseEntity<Boolean> validarEsporte(@RequestParam String nome) {
        return ResponseEntity.ok(service.validarEsporte(nome));
    }

    @GetMapping("/buscar")
    public ResponseEntity<EsporteResponseDTO> buscarPorNome(@RequestParam String nome) {
        EsporteResponseDTO esporte = service.buscarPorNome(nome);
        if (esporte != null) {
            return ResponseEntity.ok(esporte);
        }
        return ResponseEntity.notFound().build();
    }
}
