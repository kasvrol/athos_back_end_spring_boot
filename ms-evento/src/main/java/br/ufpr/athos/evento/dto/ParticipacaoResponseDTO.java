package br.ufpr.athos.evento.dto;

import br.ufpr.athos.evento.model.ParticipacaoEvento;
import java.time.LocalDateTime;

public class ParticipacaoResponseDTO {

    private String id;
    private String usuarioId;
    private String eventoId;
    private LocalDateTime dataInscricao;
    private Boolean confirmado;
    private Boolean pago;
    private LocalDateTime dataConfirmacao;
    private LocalDateTime dataPagamento;
    private String observacoes;
    private EventoResponseDTO evento;

    public ParticipacaoResponseDTO() {}

    public ParticipacaoResponseDTO(ParticipacaoEvento participacao) {
        this.id = participacao.getId();
        this.usuarioId = participacao.getUsuarioId();
        this.eventoId = participacao.getEventoId();
        this.dataInscricao = participacao.getDataInscricao();
        this.confirmado = participacao.getConfirmado();
        this.pago = participacao.getPago();
        this.dataConfirmacao = participacao.getDataConfirmacao();
        this.dataPagamento = participacao.getDataPagamento();
        this.observacoes = participacao.getObservacoes();

        if (participacao.getEvento() != null) {
            this.evento = new EventoResponseDTO(participacao.getEvento());
        }
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

    public EventoResponseDTO getEvento() {
        return evento;
    }

    public void setEvento(EventoResponseDTO evento) {
        this.evento = evento;
    }
}