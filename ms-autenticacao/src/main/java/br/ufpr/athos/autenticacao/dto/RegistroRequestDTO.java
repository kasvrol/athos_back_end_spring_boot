package br.ufpr.athos.autenticacao.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public class RegistroRequestDTO {

    @NotBlank(message = "O nome não pode estar em branco")
    private String nome;

    @NotBlank(message = "O CPF não pode estar em branco")
    private String cpf;

    @NotBlank(message = "O e-mail não pode estar em branco")
    @Email(message = "Formato de e-mail inválido")
    private String email;

    @NotBlank(message = "A senha não pode estar em branco")
    @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
    private String senha;

    @NotBlank(message = "A confirmação de senha não pode estar em branco")
    private String confirmacaoSenha;

    @NotBlank(message = "O CEP não pode estar em branco")
    private String cep;

    @NotEmpty(message = "Selecione ao menos um bairro de interesse")
    private List<String> bairros;

    @NotEmpty(message = "Selecione ao menos um esporte de interesse")
    private List<String> esportes;


    // Construtores
    public RegistroRequestDTO() {
    }

    public RegistroRequestDTO(String nome, String cpf, String email, String senha, String confirmacaoSenha, String cep, List<String> bairros, List<String> esportes) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.senha = senha;
        this.confirmacaoSenha = confirmacaoSenha;
        this.cep = cep;
        this.bairros = bairros;
        this.esportes = esportes;
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getConfirmacaoSenha() {
        return confirmacaoSenha;
    }

    public void setConfirmacaoSenha(String confirmacaoSenha) {
        this.confirmacaoSenha = confirmacaoSenha;
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