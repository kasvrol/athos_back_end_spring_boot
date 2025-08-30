package br.ufpr.athos.autenticacao.controller;

import br.ufpr.athos.autenticacao.dto.PerfilResponseDTO;
import br.ufpr.athos.autenticacao.dto.PerfilUpdateRequestDTO;
import br.ufpr.athos.autenticacao.services.JwtService;
import br.ufpr.athos.autenticacao.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/perfil")
public class PerfilController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/me")
    public ResponseEntity<?> getPerfil(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token não fornecido ou inválido");
        }

        String token = authHeader.substring(7);
        if (!jwtService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
        }

        String email = jwtService.extractUsername(token);
        PerfilResponseDTO perfil = usuarioService.getPerfil(email);

        if (perfil == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }

        return ResponseEntity.ok(perfil);
    }

    @PutMapping("/me")
    public ResponseEntity<?> updatePerfil(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @Valid @RequestBody PerfilUpdateRequestDTO perfilUpdateRequest) {
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token não fornecido ou inválido");
        }

        String token = authHeader.substring(7);
        if (!jwtService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
        }

        String email = jwtService.extractUsername(token);
        PerfilResponseDTO perfilAtualizado = usuarioService.updatePerfil(email, perfilUpdateRequest);

        if (perfilAtualizado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }

        return ResponseEntity.ok(perfilAtualizado);
    }
}