package br.ufpr.athos.notificacao.dto;

import br.ufpr.athos.notificacao.model.Notificacao;
import java.time.LocalDateTime;

public class NotificacaoResponseDTO {

    private String id;
    private String titulo;
    private String mensagem;
    private String tipo;
    private Boolean lida;
    private LocalDateTime dataCriacao;
    private String eventoRelacionadoId;

    public NotificacaoResponseDTO() {}

    public NotificacaoResponseDTO(Notificacao notificacao) {
        this.id = notificacao.getId();
        this.titulo = notificacao.getTitulo();
        this.mensagem = notificacao.getMensagem();
        this.tipo = notificacao.getTipo().name();
        this.lida = notificacao.getLida();
        this.dataCriacao = notificacao.getDataCriacao();
        this.eventoRelacionadoId = notificacao.getEventoRelacionadoId();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Boolean getLida() {
        return lida;
    }

    public void setLida(Boolean lida) {
        this.lida = lida;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getEventoRelacionadoId() {
        return eventoRelacionadoId;
    }

    public void setEventoRelacionadoId(String eventoRelacionadoId) {
        this.eventoRelacionadoId = eventoRelacionadoId;
    }
}
