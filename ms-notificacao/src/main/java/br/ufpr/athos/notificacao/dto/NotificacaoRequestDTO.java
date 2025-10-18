package br.ufpr.athos.notificacao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class NotificacaoRequestDTO {

    @NotBlank(message = "ID do usuário é obrigatório")
    private String usuarioId;

    @NotBlank(message = "Título é obrigatório")
    private String titulo;

    @NotBlank(message = "Mensagem é obrigatória")
    private String mensagem;

    @NotNull(message = "Tipo é obrigatório")
    private String tipo;

    private String eventoRelacionadoId;

    public NotificacaoRequestDTO() {}

    // Getters and Setters
    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
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

    public String getEventoRelacionadoId() {
        return eventoRelacionadoId;
    }

    public void setEventoRelacionadoId(String eventoRelacionadoId) {
        this.eventoRelacionadoId = eventoRelacionadoId;
    }
}
