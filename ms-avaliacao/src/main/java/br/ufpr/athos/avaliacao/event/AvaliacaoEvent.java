package br.ufpr.athos.avaliacao.event;

import java.io.Serializable;

public class AvaliacaoEvent implements Serializable {

    private String avaliacaoId;
    private String avaliadorId;
    private String avaliadorNome;
    private String avaliadoId;
    private String eventoId;
    private String eventoNome;
    private Integer nota;

    public AvaliacaoEvent() {}

    public AvaliacaoEvent(String avaliacaoId, String avaliadorId, String avaliadorNome,
                          String avaliadoId, String eventoId, String eventoNome, Integer nota) {
        this.avaliacaoId = avaliacaoId;
        this.avaliadorId = avaliadorId;
        this.avaliadorNome = avaliadorNome;
        this.avaliadoId = avaliadoId;
        this.eventoId = eventoId;
        this.eventoNome = eventoNome;
        this.nota = nota;
    }

    // Getters and Setters
    public String getAvaliacaoId() {
        return avaliacaoId;
    }

    public void setAvaliacaoId(String avaliacaoId) {
        this.avaliacaoId = avaliacaoId;
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
}
