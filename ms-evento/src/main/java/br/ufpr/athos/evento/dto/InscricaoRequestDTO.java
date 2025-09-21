package br.ufpr.athos.evento.dto;

import jakarta.validation.constraints.Size;

public class InscricaoRequestDTO {

    @Size(max = 500, message = "Observações devem ter no máximo 500 caracteres")
    private String observacoes;

    public InscricaoRequestDTO() {}

    public InscricaoRequestDTO(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}