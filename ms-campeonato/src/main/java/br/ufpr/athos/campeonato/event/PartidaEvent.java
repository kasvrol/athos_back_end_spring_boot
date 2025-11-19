package br.ufpr.athos.campeonato.event;

import java.io.Serializable;

public class PartidaEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private String partidaId;
    private String campeonatoId;
    private String campeonatoNome;
    private String equipe1Nome;
    private String equipe2Nome;
    private Integer placarEquipe1;
    private Integer placarEquipe2;
    private String tipoEvento; // "partida.finalizada", "partidas.geradas"
    private Integer quantidadePartidas; // For "partidas.geradas"

    // Default constructor (required for serialization)
    public PartidaEvent() {
    }

    // Constructor with main fields
    public PartidaEvent(String partidaId, String campeonatoId, String campeonatoNome,
                        String equipe1Nome, String equipe2Nome, Integer placarEquipe1,
                        Integer placarEquipe2, String tipoEvento, Integer quantidadePartidas) {
        this.partidaId = partidaId;
        this.campeonatoId = campeonatoId;
        this.campeonatoNome = campeonatoNome;
        this.equipe1Nome = equipe1Nome;
        this.equipe2Nome = equipe2Nome;
        this.placarEquipe1 = placarEquipe1;
        this.placarEquipe2 = placarEquipe2;
        this.tipoEvento = tipoEvento;
        this.quantidadePartidas = quantidadePartidas;
    }

    // Getters and setters
    public String getPartidaId() {
        return partidaId;
    }

    public void setPartidaId(String partidaId) {
        this.partidaId = partidaId;
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

    public String getEquipe1Nome() {
        return equipe1Nome;
    }

    public void setEquipe1Nome(String equipe1Nome) {
        this.equipe1Nome = equipe1Nome;
    }

    public String getEquipe2Nome() {
        return equipe2Nome;
    }

    public void setEquipe2Nome(String equipe2Nome) {
        this.equipe2Nome = equipe2Nome;
    }

    public Integer getPlacarEquipe1() {
        return placarEquipe1;
    }

    public void setPlacarEquipe1(Integer placarEquipe1) {
        this.placarEquipe1 = placarEquipe1;
    }

    public Integer getPlacarEquipe2() {
        return placarEquipe2;
    }

    public void setPlacarEquipe2(Integer placarEquipe2) {
        this.placarEquipe2 = placarEquipe2;
    }

    public String getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public Integer getQuantidadePartidas() {
        return quantidadePartidas;
    }

    public void setQuantidadePartidas(Integer quantidadePartidas) {
        this.quantidadePartidas = quantidadePartidas;
    }

    @Override
    public String toString() {
        return "PartidaEvent{" +
                "partidaId='" + partidaId + '\'' +
                ", campeonatoId='" + campeonatoId + '\'' +
                ", campeonatoNome='" + campeonatoNome + '\'' +
                ", equipe1Nome='" + equipe1Nome + '\'' +
                ", equipe2Nome='" + equipe2Nome + '\'' +
                ", placarEquipe1=" + placarEquipe1 +
                ", placarEquipe2=" + placarEquipe2 +
                ", tipoEvento='" + tipoEvento + '\'' +
                ", quantidadePartidas=" + quantidadePartidas +
                '}';
    }
}
