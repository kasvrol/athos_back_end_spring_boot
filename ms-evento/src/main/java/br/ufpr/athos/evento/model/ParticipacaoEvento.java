package br.ufpr.athos.evento.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "participacoes_eventos",
       uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "evento_id"}))
public class ParticipacaoEvento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank(message = "ID do usuário é obrigatório")
    @Column(nullable = false, name = "usuario_id")
    private String usuarioId;

    @NotBlank(message = "ID do evento é obrigatório")
    @Column(nullable = false, name = "evento_id")
    private String eventoId;

    @NotNull(message = "Data de inscrição é obrigatória")
    @Column(nullable = false, name = "data_inscricao")
    private LocalDateTime dataInscricao;

    @Column(nullable = false)
    private Boolean confirmado = false;

    @Column(nullable = false)
    private Boolean pago = false;

    @Column(name = "data_confirmacao")
    private LocalDateTime dataConfirmacao;

    @Column(name = "data_pagamento")
    private LocalDateTime dataPagamento;

    @Column(name = "observacoes", length = 500)
    private String observacoes;

    // Relacionamento com Evento (opcional para consultas)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evento_id", insertable = false, updatable = false)
    private Evento evento;

    @PrePersist
    protected void onCreate() {
        if (dataInscricao == null) {
            dataInscricao = LocalDateTime.now();
        }
    }

    public ParticipacaoEvento() {}

    public ParticipacaoEvento(String usuarioId, String eventoId) {
        this.usuarioId = usuarioId;
        this.eventoId = eventoId;
        this.dataInscricao = LocalDateTime.now();
        this.confirmado = false;
        this.pago = false;
    }

    public void confirmarParticipacao() {
        this.confirmado = true;
        this.dataConfirmacao = LocalDateTime.now();
    }

    public void confirmarPagamento() {
        this.pago = true;
        this.dataPagamento = LocalDateTime.now();
    }

    public void cancelarParticipacao() {
        this.confirmado = false;
        this.pago = false;
        this.dataConfirmacao = null;
        this.dataPagamento = null;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getEventoId() {
        return eventoId;
    }

    public void setEventoId(String eventoId) {
        this.eventoId = eventoId;
    }

    public LocalDateTime getDataInscricao() {
        return dataInscricao;
    }

    public void setDataInscricao(LocalDateTime dataInscricao) {
        this.dataInscricao = dataInscricao;
    }

    public Boolean getConfirmado() {
        return confirmado;
    }

    public void setConfirmado(Boolean confirmado) {
        this.confirmado = confirmado;
    }

    public Boolean getPago() {
        return pago;
    }

    public void setPago(Boolean pago) {
        this.pago = pago;
    }

    public LocalDateTime getDataConfirmacao() {
        return dataConfirmacao;
    }

    public void setDataConfirmacao(LocalDateTime dataConfirmacao) {
        this.dataConfirmacao = dataConfirmacao;
    }

    public LocalDateTime getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDateTime dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }
}