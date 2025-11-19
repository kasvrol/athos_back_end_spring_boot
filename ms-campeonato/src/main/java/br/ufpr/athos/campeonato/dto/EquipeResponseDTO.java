package br.ufpr.athos.campeonato.dto;

import br.ufpr.athos.campeonato.model.Equipe;
import java.util.List;
import java.util.ArrayList;

public class EquipeResponseDTO {

    private String id;
    private String nome;
    private String campeonatoId;
    private String campeonatoNome;
    private String capitaoId;
    private List<MembroEquipeSimpleDTO> membros = new ArrayList<>();
    private String createdAt;

    public EquipeResponseDTO() {}

    public EquipeResponseDTO(Equipe equipe) {
        this.id = equipe.getId();
        this.nome = equipe.getNome();
        this.campeonatoId = equipe.getCampeonato() != null ? equipe.getCampeonato().getId() : null;
        this.campeonatoNome = equipe.getCampeonato() != null ? equipe.getCampeonato().getNome() : null;
        this.capitaoId = equipe.getCapitaoId();
        this.createdAt = equipe.getDataCriacao() != null ? equipe.getDataCriacao().toString() : null;
        // membros será populado pelo Service com consulta RabbitMQ
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

    public List<MembroEquipeSimpleDTO> getMembros() {
        return membros;
    }

    public void setMembros(List<MembroEquipeSimpleDTO> membros) {
        this.membros = membros;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
