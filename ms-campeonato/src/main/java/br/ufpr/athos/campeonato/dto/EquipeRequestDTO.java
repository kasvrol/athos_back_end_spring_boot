package br.ufpr.athos.campeonato.dto;

import jakarta.validation.constraints.NotBlank;

public class EquipeRequestDTO {

    @NotBlank(message = "Nome da equipe é obrigatório")
    private String nome;

    @NotBlank(message = "ID do campeonato é obrigatório")
    private String campeonatoId;

    @NotBlank(message = "ID do capitão é obrigatório")
    private String capitaoId;

    // Getters and Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCampeonatoId() {
        return campeonatoId;
    }

    public void setCampeonatoId(String campeonatoId) {
        this.campeonatoId = campeonatoId;
    }

    public String getCapitaoId() {
        return capitaoId;
    }

    public void setCapitaoId(String capitaoId) {
        this.capitaoId = capitaoId;
    }
}
