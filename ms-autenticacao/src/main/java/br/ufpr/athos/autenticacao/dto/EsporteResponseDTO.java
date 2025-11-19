package br.ufpr.athos.autenticacao.dto;

public class EsporteResponseDTO {
    private String id;
    private String nome;

    public EsporteResponseDTO() {}

    public EsporteResponseDTO(String id, String nome) {
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
