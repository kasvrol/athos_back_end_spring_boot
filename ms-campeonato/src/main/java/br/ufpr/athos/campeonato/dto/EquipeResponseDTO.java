package br.ufpr.athos.campeonato.dto;

import br.ufpr.athos.campeonato.model.Equipe;

public class EquipeResponseDTO {

    private String id;
    private String nome;
    private String campeonatoId;
    private String campeonatoNome;
    private String capitaoId;
    private Integer totalMembros;

    public EquipeResponseDTO() {}

    public EquipeResponseDTO(Equipe equipe) {
        this.id = equipe.getId();
        this.nome = equipe.getNome();
        this.campeonatoId = equipe.getCampeonato().getId();
        this.campeonatoNome = equipe.getCampeonato().getNome();
        this.capitaoId = equipe.getCapitaoId();
        this.totalMembros = equipe.getMembros() != null ? equipe.getMembros().size() : 0;
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

    public String getCampeonatoId() {
        return campeonatoId;
    }

    public void setCampeonatoId(String campeonatoId) {
        this.campeonatoId = campeonatoId;
    }

    public String getCampeonatoNome() {
        return campeonatoNome;
    }

    public void setCampeonatoNome(String campeonatoNome) {
        this.campeonatoNome = campeonatoNome;
    }

    public String getCapitaoId() {
        return capitaoId;
    }

    public void setCapitaoId(String capitaoId) {
        this.capitaoId = capitaoId;
    }

    public Integer getTotalMembros() {
        return totalMembros;
    }

    public void setTotalMembros(Integer totalMembros) {
        this.totalMembros = totalMembros;
    }
}
