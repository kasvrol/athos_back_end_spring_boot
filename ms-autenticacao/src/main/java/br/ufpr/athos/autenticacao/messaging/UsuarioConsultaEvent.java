package br.ufpr.athos.autenticacao.messaging;

import java.io.Serializable;

/**
 * Evento de requisição de consulta de usuário
 * Usado por outros microserviços para solicitar informações de usuário
 */
public class UsuarioConsultaEvent implements Serializable {

    private static final long serialVersionUID = 1L;

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

    @Override
    public String toString() {
        return "UsuarioConsultaEvent{" +
                "usuarioId='" + usuarioId + '\'' +
                '}';
    }
}
