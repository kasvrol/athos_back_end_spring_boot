package br.ufpr.athos.avaliacao.dto;

import br.ufpr.athos.avaliacao.model.Avaliacao;
import java.time.LocalDateTime;

public class AvaliacaoResponseDTO {

    private String id;
    private String avaliadorId;
    private String avaliadorNome;
    private String avaliadoId;
    private String eventoId;
    private String eventoNome;
    private Integer nota;
    private String comentario;
    private LocalDateTime dataCriacao;

    public AvaliacaoResponseDTO() {}

    public AvaliacaoResponseDTO(Avaliacao avaliacao) {
        this.id = avaliacao.getId();
        this.avaliadorId = avaliacao.getAvaliadorId();
        this.avaliadorNome = avaliacao.getAvaliadorNome();
        this.avaliadoId = avaliacao.getAvaliadoId();
        this.eventoId = avaliacao.getEventoId();
        this.eventoNome = avaliacao.getEventoNome();
        this.nota = avaliacao.getNota();
        this.comentario = avaliacao.getComentario();
        this.dataCriacao = avaliacao.getDataCriacao();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
