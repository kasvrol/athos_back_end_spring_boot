package br.ufpr.athos.autenticacao.dto;

import java.util.List;

public class PerfilUpdateRequestDTO {
    private String nome;
    private String cep;
    private List<String> bairros;
    private List<String> esportes;

    public PerfilUpdateRequestDTO() {
    }

    public PerfilUpdateRequestDTO(String nome, String cep, List<String> bairros, List<String> esportes) {
        this.nome = nome;
        this.cep = cep;
        this.bairros = bairros;
        this.esportes = esportes;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public List<String> getBairros() {
        return bairros;
    }

    public void setBairros(List<String> bairros) {
        this.bairros = bairros;
    }

    public List<String> getEsportes() {
        return esportes;
    }

    public void setEsportes(List<String> esportes) {
        this.esportes = esportes;
    }
}