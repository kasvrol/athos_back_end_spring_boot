package br.ufpr.athos.autenticacao.messaging;

import java.io.Serializable;

/**
 * Evento de resposta de consulta de usuário
 * Enviado como resposta para outros microserviços que solicitaram informações de usuário
 */
public class UsuarioResponseEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private String usuarioId;
    private String nome;
    private String email;

    public UsuarioResponseEvent() {
    }

    public UsuarioResponseEvent(String usuarioId, String nome, String email) {
        this.usuarioId = usuarioId;
        this.nome = nome;
        this.email = email;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UsuarioResponseEvent{" +
                "usuarioId='" + usuarioId + '\'' +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
