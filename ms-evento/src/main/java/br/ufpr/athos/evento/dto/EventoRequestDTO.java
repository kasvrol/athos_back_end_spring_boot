package br.ufpr.athos.evento.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class EventoRequestDTO {

    @NotBlank(message = "Nome do evento é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nome;

    @NotNull(message = "Data do evento é obrigatória")
    @Future(message = "Data do evento deve ser futura")
    private LocalDate data;

    @NotNull(message = "Horário do evento é obrigatório")
    private LocalTime horario;

    @NotBlank(message = "Esporte é obrigatório")
    @Size(max = 50, message = "Esporte deve ter no máximo 50 caracteres")
    private String esporte;

    @NotBlank(message = "CEP é obrigatório")
    @Pattern(regexp = "\\d{5}-?\\d{3}", message = "CEP deve estar no formato 12345-678")
    private String cep;

    @NotBlank(message = "Endereço é obrigatório")
    @Size(max = 200, message = "Endereço deve ter no máximo 200 caracteres")
    private String endereco;

    @NotNull(message = "Tipo de evento (público/privado) é obrigatório")
    private Boolean publico;

    @NotNull(message = "Valor de participação é obrigatório")
    @DecimalMin(value = "0.0", inclusive = true, message = "Valor deve ser maior ou igual a zero")
    private BigDecimal valor;

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    private String descricao;

    @Min(value = 2, message = "Deve ter pelo menos 2 participantes")
    @Max(value = 1000, message = "Máximo de 1000 participantes")
    private Integer maxParticipantes;

    public EventoRequestDTO() {}

    public EventoRequestDTO(String nome, LocalDate data, LocalTime horario, String esporte,
                           String cep, String endereco, Boolean publico, BigDecimal valor) {
        this.nome = nome;
        this.data = data;
        this.horario = horario;
        this.esporte = esporte;
        this.cep = cep;
        this.endereco = endereco;
        this.publico = publico;
        this.valor = valor;
    }

    // Getters and Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHorario() {
        return horario;
    }

    public void setHorario(LocalTime horario) {
        this.horario = horario;
    }

    public String getEsporte() {
        return esporte;
    }

    public void setEsporte(String esporte) {
        this.esporte = esporte;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Boolean getPublico() {
        return publico;
    }

    public void setPublico(Boolean publico) {
        this.publico = publico;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getMaxParticipantes() {
        return maxParticipantes;
    }

    public void setMaxParticipantes(Integer maxParticipantes) {
        this.maxParticipantes = maxParticipantes;
    }
}