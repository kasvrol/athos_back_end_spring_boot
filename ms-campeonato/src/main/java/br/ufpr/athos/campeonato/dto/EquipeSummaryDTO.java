package br.ufpr.athos.campeonato.dto;

public class EquipeSummaryDTO {
    private String id;
    private String nome;

    public EquipeSummaryDTO() {
    }

    public EquipeSummaryDTO(String id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    // Getters and setters
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
}
