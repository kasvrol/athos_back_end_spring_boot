package br.ufpr.athos.campeonato.dto;

import br.ufpr.athos.campeonato.model.Campeonato;
import java.time.LocalDate;

public class CampeonatoResponseDTO {

    private String id;
    private String nome;
    private String esporte;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String formato;
    private String organizadorId;
    private String descricao;
    private Integer maxEquipes;
    private String status;
    private Integer totalEquipes;

    public CampeonatoResponseDTO() {}

    public CampeonatoResponseDTO(Campeonato campeonato) {
        this.id = campeonato.getId();
        this.nome = campeonato.getNome();
        this.esporte = campeonato.getEsporte();
        this.dataInicio = campeonato.getDataInicio();
        this.dataFim = campeonato.getDataFim();
        this.formato = campeonato.getFormato().name();
        this.organizadorId = campeonato.getOrganizadorId();
        this.descricao = campeonato.getDescricao();
        this.maxEquipes = campeonato.getMaxEquipes();
        this.status = campeonato.getStatus().name();
        this.totalEquipes = campeonato.getEquipes() != null ? campeonato.getEquipes().size() : 0;
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
}
