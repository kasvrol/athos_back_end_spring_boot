package br.ufpr.athos.pagamento.dto;

import br.ufpr.athos.pagamento.model.Pagamento;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PagamentoResponseDTO {

    private String id;
    private String usuarioId;
    private String eventoId;
    private String eventoNome;
    private BigDecimal valor;
    private String status;
    private String stripeClientSecret;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataConfirmacao;

    public PagamentoResponseDTO() {}

    public PagamentoResponseDTO(Pagamento pagamento) {
        this.id = pagamento.getId();
        this.usuarioId = pagamento.getUsuarioId();
        this.eventoId = pagamento.getEventoId();
        this.eventoNome = pagamento.getEventoNome();
        this.valor = pagamento.getValor();
        this.status = pagamento.getStatus().name();
        this.stripeClientSecret = pagamento.getStripeClientSecret();
        this.dataCriacao = pagamento.getDataCriacao();
        this.dataConfirmacao = pagamento.getDataConfirmacao();
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

    public String getEventoNome() {
        return eventoNome;
    }

    public void setEventoNome(String eventoNome) {
        this.eventoNome = eventoNome;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStripeClientSecret() {
        return stripeClientSecret;
    }

    public void setStripeClientSecret(String stripeClientSecret) {
        this.stripeClientSecret = stripeClientSecret;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataConfirmacao() {
        return dataConfirmacao;
    }

    public void setDataConfirmacao(LocalDateTime dataConfirmacao) {
        this.dataConfirmacao = dataConfirmacao;
    }
}
