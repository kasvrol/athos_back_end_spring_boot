package br.ufpr.athos.avaliacao.dto;

import jakarta.validation.constraints.*;

public class AvaliacaoRequestDTO {

    @NotBlank(message = "ID do avaliador é obrigatório")
    private String avaliadorId;

    @NotBlank(message = "Nome do avaliador é obrigatório")
    private String avaliadorNome;

    @NotBlank(message = "ID do avaliado é obrigatório")
    private String avaliadoId;

    @NotBlank(message = "ID do evento é obrigatório")
    private String eventoId;

    @NotBlank(message = "Nome do evento é obrigatório")
    private String eventoNome;

    @NotNull(message = "Nota é obrigatória")
    @Min(value = 1, message = "Nota mínima é 1")
    @Max(value = 5, message = "Nota máxima é 5")
    private Integer nota;

    @Size(max = 500, message = "Comentário deve ter no máximo 500 caracteres")
    private String comentario;

    public AvaliacaoRequestDTO() {}

    // Getters and Setters
    public String getAvaliadorId() {
        return avaliadorId;
    }

    public void setAvaliadorId(String avaliadorId) {
        this.avaliadorId = avaliadorId;
    }

    public String getAvaliadorNome() {
        return avaliadorNome;
    }

    public void setAvaliadorNome(String avaliadorNome) {
        this.avaliadorNome = avaliadorNome;
    }

    public String getAvaliadoId() {
        return avaliadoId;
    }

    public void setAvaliadoId(String avaliadoId) {
        this.avaliadoId = avaliadoId;
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

    public Integer getNota() {
        return nota;
    }

    public void setNota(Integer nota) {
        this.nota = nota;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
