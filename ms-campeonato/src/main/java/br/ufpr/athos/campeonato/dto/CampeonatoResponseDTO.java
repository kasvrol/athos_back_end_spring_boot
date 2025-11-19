package br.ufpr.athos.campeonato.dto;

import br.ufpr.athos.campeonato.model.Campeonato;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CampeonatoResponseDTO {

    private String id;
    private String nome;
    private String esporte;
    private String dataInscricaoInicio;
    private String dataInscricaoFim;
    private String dataInicio;
    private String dataFim;
    private String formato;
    private String organizadorId;
    private String descricao;
    private Integer maxEquipes;
    private String status;

    @JsonProperty("equipesInscritas")
    private Integer totalEquipes;

    private String createdAt;

    public CampeonatoResponseDTO() {}

    public CampeonatoResponseDTO(Campeonato campeonato) {
        this.id = campeonato.getId();
        this.nome = campeonato.getNome();
        this.esporte = campeonato.getEsporte();
        this.dataInscricaoInicio = campeonato.getDataInscricaoInicio() != null
            ? campeonato.getDataInscricaoInicio().toString() : null;
        this.dataInscricaoFim = campeonato.getDataInscricaoFim() != null
            ? campeonato.getDataInscricaoFim().toString() : null;
        this.dataInicio = campeonato.getDataInicio() != null
            ? campeonato.getDataInicio().toString() : null;
        this.dataFim = campeonato.getDataFim() != null
            ? campeonato.getDataFim().toString() : null;
        this.formato = campeonato.getFormato().name();
        this.organizadorId = campeonato.getOrganizadorId();
        this.descricao = campeonato.getDescricao();
        this.maxEquipes = campeonato.getMaxEquipes();
        this.status = campeonato.getStatus().name();
        this.totalEquipes = campeonato.getEquipes() != null ? campeonato.getEquipes().size() : 0;
        this.createdAt = campeonato.getDataCriacao() != null
            ? campeonato.getDataCriacao().toString() : null;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getDataInscricaoInicio() {
        return dataInscricaoInicio;
    }

    public void setDataInscricaoInicio(String dataInscricaoInicio) {
        this.dataInscricaoInicio = dataInscricaoInicio;
    }

    public String getDataInscricaoFim() {
        return dataInscricaoFim;
    }

    public void setDataInscricaoFim(String dataInscricaoFim) {
        this.dataInscricaoFim = dataInscricaoFim;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDataFim() {
        return dataFim;
    }

    public void setDataFim(String dataFim) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTotalEquipes() {
        return totalEquipes;
    }

    public void setTotalEquipes(Integer totalEquipes) {
        this.totalEquipes = totalEquipes;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
