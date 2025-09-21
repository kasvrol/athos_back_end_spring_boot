package br.ufpr.athos.evento.dto;

import br.ufpr.athos.evento.model.Evento;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

public class EventoResponseDTO {

    private String id;
    private String nome;
    private LocalDate data;
    private LocalTime horario;
    private String esporte;
    private String cep;
    private String endereco;
    private Boolean publico;
    private BigDecimal valor;
    private String criadorId;
    private String descricao;
    private Integer maxParticipantes;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private Long totalParticipantes;
    private Long participantesConfirmados;
    private Boolean usuarioInscrito;
    private Boolean usuarioECriador;

    public EventoResponseDTO() {}

    public EventoResponseDTO(Evento evento) {
        this.id = evento.getId();
        this.nome = evento.getNome();
        this.data = evento.getData();
        this.horario = evento.getHorario();
        this.esporte = evento.getEsporte();
        this.cep = evento.getCep();
        this.endereco = evento.getEndereco();
        this.publico = evento.getPublico();
        this.valor = evento.getValor();
        this.criadorId = evento.getCriadorId();
        this.descricao = evento.getDescricao();
        this.maxParticipantes = evento.getMaxParticipantes();
        this.dataCriacao = evento.getDataCriacao();
        this.dataAtualizacao = evento.getDataAtualizacao();
    }

    public EventoResponseDTO(Evento evento, Long totalParticipantes, Long participantesConfirmados,
                            Boolean usuarioInscrito, Boolean usuarioECriador) {
        this(evento);
        this.totalParticipantes = totalParticipantes;
        this.participantesConfirmados = participantesConfirmados;
        this.usuarioInscrito = usuarioInscrito;
        this.usuarioECriador = usuarioECriador;
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

    public String getCriadorId() {
        return criadorId;
    }

    public void setCriadorId(String criadorId) {
        this.criadorId = criadorId;
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

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public Long getTotalParticipantes() {
        return totalParticipantes;
    }

    public void setTotalParticipantes(Long totalParticipantes) {
        this.totalParticipantes = totalParticipantes;
    }

    public Long getParticipantesConfirmados() {
        return participantesConfirmados;
    }

    public void setParticipantesConfirmados(Long participantesConfirmados) {
        this.participantesConfirmados = participantesConfirmados;
    }

    public Boolean getUsuarioInscrito() {
        return usuarioInscrito;
    }

    public void setUsuarioInscrito(Boolean usuarioInscrito) {
        this.usuarioInscrito = usuarioInscrito;
    }

    public Boolean getUsuarioECriador() {
        return usuarioECriador;
    }

    public void setUsuarioECriador(Boolean usuarioECriador) {
        this.usuarioECriador = usuarioECriador;
    }
}