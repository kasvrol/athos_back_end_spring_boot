package br.ufpr.athos.campeonato.dto;

public class EstatisticasDTO {
    private Integer totalEquipes;
    private Integer totalPartidas;
    private Integer partidasRealizadas;
    private Integer totalGols;
    private Double mediaGolsPorPartida;
    private String faseAtual;

    // Default constructor
    public EstatisticasDTO() {
    }

    // Constructor with all fields for easy creation
    public EstatisticasDTO(Integer totalEquipes, Integer totalPartidas, Integer partidasRealizadas,
                          Integer totalGols, Double mediaGolsPorPartida, String faseAtual) {
        this.totalEquipes = totalEquipes;
        this.totalPartidas = totalPartidas;
        this.partidasRealizadas = partidasRealizadas;
        this.totalGols = totalGols;
        this.mediaGolsPorPartida = mediaGolsPorPartida;
        this.faseAtual = faseAtual;
    }

    // Getters and Setters
    public Integer getTotalEquipes() {
        return totalEquipes;
    }

    public void setTotalEquipes(Integer totalEquipes) {
        this.totalEquipes = totalEquipes;
    }

    public Integer getTotalPartidas() {
        return totalPartidas;
    }

    public void setTotalPartidas(Integer totalPartidas) {
        this.totalPartidas = totalPartidas;
    }

    public Integer getPartidasRealizadas() {
        return partidasRealizadas;
    }

    public void setPartidasRealizadas(Integer partidasRealizadas) {
        this.partidasRealizadas = partidasRealizadas;
    }

    public Integer getTotalGols() {
        return totalGols;
    }

    public void setTotalGols(Integer totalGols) {
        this.totalGols = totalGols;
    }

    public Double getMediaGolsPorPartida() {
        return mediaGolsPorPartida;
    }

    public void setMediaGolsPorPartida(Double mediaGolsPorPartida) {
        this.mediaGolsPorPartida = mediaGolsPorPartida;
    }

    public String getFaseAtual() {
        return faseAtual;
    }

    public void setFaseAtual(String faseAtual) {
        this.faseAtual = faseAtual;
    }
}
