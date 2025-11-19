package br.ufpr.athos.campeonato.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class PartidaRequestDTO {
    @NotBlank(message = "ID do campeonato é obrigatório")
    private String campeonatoId;

    @NotBlank(message = "ID da equipe 1 é obrigatório")
    private String equipe1Id;

    @NotBlank(message = "ID da equipe 2 é obrigatório")
    private String equipe2Id;

    @NotNull(message = "Data e hora da partida são obrigatórias")
    private LocalDateTime dataHora;

    private String local;
    private String fase;
    private Integer rodada;

    // Getters and setters
    public String getCampeonatoId() {
        return campeonatoId;
    }

    public void setCampeonatoId(String campeonatoId) {
        this.campeonatoId = campeonatoId;
    }

    public String getEquipe1Id() {
        return equipe1Id;
    }

    public void setEquipe1Id(String equipe1Id) {
        this.equipe1Id = equipe1Id;
    }

    public String getEquipe2Id() {
        return equipe2Id;
    }

    public void setEquipe2Id(String equipe2Id) {
        this.equipe2Id = equipe2Id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getFase() {
        return fase;
    }

    public void setFase(String fase) {
        this.fase = fase;
    }

    public Integer getRodada() {
        return rodada;
    }

    public void setRodada(Integer rodada) {
        this.rodada = rodada;
    }
}
