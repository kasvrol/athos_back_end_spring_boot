package br.ufpr.athos.campeonato.messaging;

/**
 * Event DTO for receiving user information from ms-autenticacao
 */
public class UsuarioResponseEvent {
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
}
