package br.ufpr.athos.autenticacao.controller;

import br.ufpr.athos.autenticacao.dto.LoginRequestDTO;
import br.ufpr.athos.autenticacao.dto.RegistroRequestDTO;
import br.ufpr.athos.autenticacao.model.Usuario;
import br.ufpr.athos.autenticacao.services.JwtService;
import br.ufpr.athos.autenticacao.services.UsuarioService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UsuarioService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            logger.info("Usuario autenticado com sucesso: {}", authentication.isAuthenticated());

            if (authentication.isAuthenticated()) {
                String token = jwtService.generateToken(loginRequest.getEmail());
                return ResponseEntity.ok(token);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Autenticação falhou");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou senha inválidos");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registrar(@Valid @RequestBody RegistroRequestDTO registroRequest) {
        if (!registroRequest.getSenha().equals(registroRequest.getConfirmacaoSenha())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("As senhas não coincidem.");
        }

        try {
            Usuario novoUsuario = new Usuario();
            novoUsuario.setNome(registroRequest.getNome());
            novoUsuario.setEmail(registroRequest.getEmail());
            novoUsuario.setSenha(registroRequest.getSenha());
            novoUsuario.setCpf(registroRequest.getCpf());
            novoUsuario.setCep(registroRequest.getCep());
            novoUsuario.setBairros(registroRequest.getBairros());
            novoUsuario.setEsportes(registroRequest.getEsportes());

            Usuario usuarioSalvo = userService.registrarUsuario(novoUsuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo);

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<Void> validateToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.substring(7);

        if (jwtService.isTokenValid(token)) {
            String userEmail = jwtService.extractUsername(token);
            if (userService.findByEmail(userEmail).isPresent()) {
                return ResponseEntity.ok().build();
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}