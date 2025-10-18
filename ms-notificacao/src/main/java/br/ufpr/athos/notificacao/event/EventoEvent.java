package br.ufpr.athos.notificacao.event;

import java.io.Serializable;

public class EventoEvent implements Serializable {

    private String eventoId;
    private String eventoNome;
    private String usuarioId;
    private String tipo; // CRIADO, ATUALIZADO, CANCELADO, INSCRICAO_CONFIRMADA, INSCRICAO_CANCELADA, VAGA_DISPONIVEL
    private String mensagemAdicional;

    public EventoEvent() {}

    public EventoEvent(String eventoId, String eventoNome, String usuarioId, String tipo) {
        this.eventoId = eventoId;
        this.eventoNome = eventoNome;
        this.usuarioId = usuarioId;
        this.tipo = tipo;
    }

    public EventoEvent(String eventoId, String eventoNome, String usuarioId, String tipo, String mensagemAdicional) {
        this.eventoId = eventoId;
        this.eventoNome = eventoNome;
        this.usuarioId = usuarioId;
        this.tipo = tipo;
        this.mensagemAdicional = mensagemAdicional;
    }

    // Getters and Setters
    public String getEventoId() {
        return eventoId;
    }

    public void setEventoId(String eventoId) {
        this.eventoId = eventoId;
    }

    public String getEventoNome() {
        return eventoNome;
    }

    public void setEventoNome(String eventoNome) {
        this.eventoNome = eventoNome;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMensagemAdicional() {
        return mensagemAdicional;
    }

    public void setMensagemAdicional(String mensagemAdicional) {
        this.mensagemAdicional = mensagemAdicional;
    }
}
