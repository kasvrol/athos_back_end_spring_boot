package br.ufpr.athos.campeonato.event;

import java.io.Serializable;

public class EquipeEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private String equipeId;
    private String equipeNome;
    private String campeonatoId;
    private String campeonatoNome;
    private String capitaoId;
    private String tipoEvento; // "equipe.inscrita"

    // Default constructor (required for serialization)
    public EquipeEvent() {
    }

    // Constructor with all fields
    public EquipeEvent(String equipeId, String equipeNome, String campeonatoId,
                       String campeonatoNome, String capitaoId, String tipoEvento) {
        this.equipeId = equipeId;
        this.equipeNome = equipeNome;
        this.campeonatoId = campeonatoId;
        this.campeonatoNome = campeonatoNome;
        this.capitaoId = capitaoId;
        this.tipoEvento = tipoEvento;
    }

    // Getters and setters
    public String getEquipeId() {
        return equipeId;
    }

    public void setEquipeId(String equipeId) {
        this.equipeId = equipeId;
    }

    public String getEquipeNome() {
        return equipeNome;
    }

    public void setEquipeNome(String equipeNome) {
        this.equipeNome = equipeNome;
    }

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

    public String getCapitaoId() {
        return capitaoId;
    }

    public void setCapitaoId(String capitaoId) {
        this.capitaoId = capitaoId;
    }

    public String getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    @Override
    public String toString() {
        return "EquipeEvent{" +
                "equipeId='" + equipeId + '\'' +
                ", equipeNome='" + equipeNome + '\'' +
                ", campeonatoId='" + campeonatoId + '\'' +
                ", campeonatoNome='" + campeonatoNome + '\'' +
                ", capitaoId='" + capitaoId + '\'' +
                ", tipoEvento='" + tipoEvento + '\'' +
                '}';
    }
}
