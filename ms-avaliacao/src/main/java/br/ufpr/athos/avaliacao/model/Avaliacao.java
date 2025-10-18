package br.ufpr.athos.avaliacao.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "avaliacoes")
public class Avaliacao {

    @Id
    private String id;

    private String avaliadorId;
    private String avaliadorNome;
    private String avaliadoId;
    private String eventoId;
    private String eventoNome;
    private Integer nota; // 1 a 5 estrelas
    private String comentario;
    private LocalDateTime dataCriacao;

    public Avaliacao() {
        this.dataCriacao = LocalDateTime.now();
    }

    public Avaliacao(String avaliadorId, String avaliadorNome, String avaliadoId,
                     String eventoId, String eventoNome, Integer nota, String comentario) {
        this();
        this.avaliadorId = avaliadorId;
        this.avaliadorNome = avaliadorNome;
        this.avaliadoId = avaliadoId;
        this.eventoId = eventoId;
        this.eventoNome = eventoNome;
        this.nota = nota;
        this.comentario = comentario;
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
