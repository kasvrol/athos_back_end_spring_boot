package br.ufpr.athos.campeonato.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class AtualizarPlacarDTO {
    @NotNull(message = "Placar da equipe 1 é obrigatório")
    @Min(value = 0, message = "Placar deve ser maior ou igual a zero")
    private Integer placarEquipe1;

    @NotNull(message = "Placar da equipe 2 é obrigatório")
    @Min(value = 0, message = "Placar deve ser maior ou igual a zero")
    private Integer placarEquipe2;

    // Getters and setters
    public Integer getPlacarEquipe1() {
        return placarEquipe1;
    }

    public void setPlacarEquipe1(Integer placarEquipe1) {
        this.placarEquipe1 = placarEquipe1;
    }

    public Integer getPlacarEquipe2() {
        return placarEquipe2;
    }

    public void setPlacarEquipe2(Integer placarEquipe2) {
        this.placarEquipe2 = placarEquipe2;
    }
}
