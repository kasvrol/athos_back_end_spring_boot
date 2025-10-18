package br.ufpr.athos.notificacao.event;

import java.io.Serializable;
import java.math.BigDecimal;

public class PagamentoEvent implements Serializable {

    private String pagamentoId;
    private String usuarioId;
    private String eventoId;
    private String eventoNome;
    private BigDecimal valor;
    private String status; // CONFIRMADO, PENDENTE, FALHOU

    public PagamentoEvent() {}

    public PagamentoEvent(String pagamentoId, String usuarioId, String eventoId,
                          String eventoNome, BigDecimal valor, String status) {
        this.pagamentoId = pagamentoId;
        this.usuarioId = usuarioId;
        this.eventoId = eventoId;
        this.eventoNome = eventoNome;
        this.valor = valor;
        this.status = status;
    }

    // Getters and Setters
    public String getPagamentoId() {
        return pagamentoId;
    }

    public void setPagamentoId(String pagamentoId) {
        this.pagamentoId = pagamentoId;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

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

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
