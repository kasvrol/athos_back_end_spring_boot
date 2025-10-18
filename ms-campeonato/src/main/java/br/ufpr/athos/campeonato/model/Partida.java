package br.ufpr.athos.campeonato.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "partidas")
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campeonato_id", nullable = false)
    private Campeonato campeonato;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipe1_id", nullable = false)
    private Equipe equipe1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipe2_id", nullable = false)
    private Equipe equipe2;

    @Column(name = "placar_equipe1")
    private Integer placarEquipe1;

    @Column(name = "placar_equipe2")
    private Integer placarEquipe2;

    @Column(name = "data_hora")
    private LocalDateTime dataHora;

    @Column(length = 50)
    private String fase; // Ex: "GRUPOS", "OITAVAS", "QUARTAS", "SEMI", "FINAL"

    @Column
    private Integer rodada;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPartida status;

    @Column(length = 200)
    private String local;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
        if (status == null) {
            status = StatusPartida.AGENDADA;
        }
    }

    public enum StatusPartida {
        AGENDADA,
        EM_ANDAMENTO,
        FINALIZADA,
        CANCELADA,
        ADIADA
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Campeonato getCampeonato() {
        return campeonato;
    }

    public void setCampeonato(Campeonato campeonato) {
        this.campeonato = campeonato;
    }

    public Equipe getEquipe1() {
        return equipe1;
    }

    public void setEquipe1(Equipe equipe1) {
        this.equipe1 = equipe1;
    }

    public Equipe getEquipe2() {
        return equipe2;
    }

    public void setEquipe2(Equipe equipe2) {
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

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
