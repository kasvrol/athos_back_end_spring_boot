package br.ufpr.athos.pagamento.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class PagamentoRequestDTO {

    @NotBlank(message = "ID do usuário é obrigatório")
    private String usuarioId;

    @NotBlank(message = "ID do evento é obrigatório")
    private String eventoId;

    @NotBlank(message = "Nome do evento é obrigatório")
    private String eventoNome;

    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    private BigDecimal valor;

    public PagamentoRequestDTO() {}

    // Getters and Setters
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
}
