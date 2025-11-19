package br.ufpr.athos.campeonato.event;

import java.io.Serializable;
import java.time.LocalDate;

public class CampeonatoEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private String campeonatoId;
    private String campeonatoNome;
    private String organizadorId;
    private String tipoEvento; // "campeonato.criado", "campeonato.status.alterado", etc.
    private String statusAntigo;  // Optional, for status changes
    private String statusNovo;    // Optional, for status changes
    private LocalDate dataInicio;

    // Default constructor (required for serialization)
    public CampeonatoEvent() {
    }

    // Constructor with all fields
    public CampeonatoEvent(String campeonatoId, String campeonatoNome, String organizadorId,
                           String tipoEvento, String statusAntigo, String statusNovo, LocalDate dataInicio) {
        this.campeonatoId = campeonatoId;
        this.campeonatoNome = campeonatoNome;
        this.organizadorId = organizadorId;
        this.tipoEvento = tipoEvento;
        this.statusAntigo = statusAntigo;
        this.statusNovo = statusNovo;
        this.dataInicio = dataInicio;
    }

    // Getters and setters
    public String getCampeonatoId() {
        return campeonatoId;
    }

    public void setCampeonatoId(String campeonatoId) {
        this.campeonatoId = campeonatoId;
    }

    public String getCampeonatoNome() {
        return campeonatoNome;
    }

    public void setCampeonatoNome(String campeonatoNome) {
        this.campeonatoNome = campeonatoNome;
    }

    public String getOrganizadorId() {
        return organizadorId;
    }

    public void setOrganizadorId(String organizadorId) {
        this.organizadorId = organizadorId;
    }

    public String getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public String getStatusAntigo() {
        return statusAntigo;
    }

    public void setStatusAntigo(String statusAntigo) {
        this.statusAntigo = statusAntigo;
    }

    public String getStatusNovo() {
        return statusNovo;
    }

    public void setStatusNovo(String statusNovo) {
        this.statusNovo = statusNovo;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    @Override
    public String toString() {
        return "CampeonatoEvent{" +
                "campeonatoId='" + campeonatoId + '\'' +
                ", campeonatoNome='" + campeonatoNome + '\'' +
                ", organizadorId='" + organizadorId + '\'' +
                ", tipoEvento='" + tipoEvento + '\'' +
                ", statusAntigo='" + statusAntigo + '\'' +
                ", statusNovo='" + statusNovo + '\'' +
                ", dataInicio=" + dataInicio +
                '}';
    }
}
