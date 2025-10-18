package br.ufpr.athos.campeonato.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class CampeonatoRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "Esporte é obrigatório")
    private String esporte;

    @NotNull(message = "Data de início é obrigatória")
    private LocalDate dataInicio;

    @NotNull(message = "Data de término é obrigatória")
    private LocalDate dataFim;

    @NotBlank(message = "Formato é obrigatório")
    private String formato;

    @NotBlank(message = "ID do organizador é obrigatório")
    private String organizadorId;

    private String descricao;
    private Integer maxEquipes;

    // Getters and Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEsporte() {
        return esporte;
    }

    public void setEsporte(String esporte) {
        this.esporte = esporte;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public String getOrganizadorId() {
        return organizadorId;
    }

    public void setOrganizadorId(String organizadorId) {
        this.organizadorId = organizadorId;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getMaxEquipes() {
        return maxEquipes;
    }

    public void setMaxEquipes(Integer maxEquipes) {
        this.maxEquipes = maxEquipes;
    }
}
