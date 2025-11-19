package br.ufpr.athos.campeonato.messaging;

/**
 * Event DTO for requesting user information from ms-autenticacao
 */
public class UsuarioConsultaEvent {
    private String usuarioId;

    public UsuarioConsultaEvent() {
    }

    public UsuarioConsultaEvent(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }
}
