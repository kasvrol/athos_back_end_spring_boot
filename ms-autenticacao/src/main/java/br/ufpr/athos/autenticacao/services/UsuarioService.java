package br.ufpr.athos.autenticacao.services;

import br.ufpr.athos.autenticacao.dto.PerfilResponseDTO;
import br.ufpr.athos.autenticacao.dto.PerfilUpdateRequestDTO;
import br.ufpr.athos.autenticacao.model.Usuario;
import br.ufpr.athos.autenticacao.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario registrarUsuario(Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new IllegalStateException("Email já cadastrado.");
        }

        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);
        usuario.setDtCadastro(LocalDate.now());

        return usuarioRepository.save(usuario);
    }

    public boolean validarCredenciais(String email, String senha) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isEmpty()) {
            return false;
        }

        Usuario usuario = usuarioOpt.get();
        return passwordEncoder.matches(senha, usuario.getSenha());
    }

    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public PerfilResponseDTO getPerfil(String email) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isEmpty()) {
            return null;
        }

        Usuario usuario = usuarioOpt.get();
        return new PerfilResponseDTO(
            usuario.getId(),
            usuario.getNome(),
            usuario.getEmail(),
            usuario.getCpf(),
            usuario.getFoto(),
            usuario.getDtCadastro(),
            usuario.getCep(),
            usuario.getBairros(),
            usuario.getEsportes()
        );
    }

    public PerfilResponseDTO updatePerfil(String email, PerfilUpdateRequestDTO dto) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isEmpty()) {
            return null;
        }

        Usuario usuario = usuarioOpt.get();

        usuario.setNome(dto.getNome());
        usuario.setCep(dto.getCep());
        usuario.setBairros(dto.getBairros());
        usuario.setEsportes(dto.getEsportes());

        Usuario usuarioAtualizado = usuarioRepository.save(usuario);

        return new PerfilResponseDTO(
            usuarioAtualizado.getId(),
            usuarioAtualizado.getNome(),
            usuarioAtualizado.getEmail(),
            usuarioAtualizado.getCpf(),
            usuarioAtualizado.getFoto(),
            usuarioAtualizado.getDtCadastro(),
            usuarioAtualizado.getCep(),
            usuarioAtualizado.getBairros(),
            usuarioAtualizado.getEsportes()
        );
    }
}
