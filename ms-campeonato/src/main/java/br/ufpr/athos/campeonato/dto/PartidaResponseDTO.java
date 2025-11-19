package br.ufpr.athos.campeonato.dto;

import br.ufpr.athos.campeonato.model.Partida.StatusPartida;
import com.fasterxml.jackson.annotation.JsonGetter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PartidaResponseDTO {
    private String id;
    private String campeonatoId;
    private String campeonatoNome;
    private EquipeResponseDTO equipe1;
    private EquipeResponseDTO equipe2;
    private Integer placarEquipe1;
    private Integer placarEquipe2;
    private LocalDateTime dataHora;
    private String data;
    private String horario;
    private String local;
    private String fase;
    private Integer rodada;
    private StatusPartida status;
    private LocalDateTime dataCriacao;

    // Formatters for date and time
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public EquipeResponseDTO getEquipe1() {
        return equipe1;
    }

    public void setEquipe1(EquipeResponseDTO equipe1) {
        this.equipe1 = equipe1;
    }

    public EquipeResponseDTO getEquipe2() {
        return equipe2;
    }

    public void setEquipe2(EquipeResponseDTO equipe2) {
        this.equipe2 = equipe2;
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

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
        // Auto-populate data and horario when dataHora is set
        if (dataHora != null) {
            this.data = dataHora.format(DATE_FORMATTER);
            this.horario = dataHora.format(TIME_FORMATTER);
        }
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getFase() {
        return fase;
    }

    public void setFase(String fase) {
        this.fase = fase;
    }

    public Integer getRodada() {
        return rodada;
    }

    public void setRodada(Integer rodada) {
        this.rodada = rodada;
    }

    public StatusPartida getStatus() {
        return status;
    }

    public void setStatus(StatusPartida status) {
        this.status = status;
    }

    /**
     * Custom getter for status that maps FINALIZADA to FINALIZADO for frontend
     */
    @JsonGetter("status")
    public String getStatusAsString() {
        if (status == StatusPartida.FINALIZADA) {
            return "FINALIZADO";
        }
        return status != null ? status.toString() : null;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
