package br.ufpr.athos.autenticacao.dto;

import java.time.LocalDate;
import java.util.List;

public class PerfilResponseDTO {
    private String id;
    private String nome;
    private String email;
    private String cpf;
    private String foto;
    private LocalDate dtCadastro;
    private String cep;
    private List<String> bairros;
    private List<String> esportes;

    public PerfilResponseDTO() {
    }

    public PerfilResponseDTO(String id, String nome, String email, String cpf, String foto, 
                            LocalDate dtCadastro, String cep, List<String> bairros, List<String> esportes) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.foto = foto;
        this.dtCadastro = dtCadastro;
        this.cep = cep;
        this.bairros = bairros;
        this.esportes = esportes;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public LocalDate getDtCadastro() {
        return dtCadastro;
    }

    public void setDtCadastro(LocalDate dtCadastro) {
        this.dtCadastro = dtCadastro;
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