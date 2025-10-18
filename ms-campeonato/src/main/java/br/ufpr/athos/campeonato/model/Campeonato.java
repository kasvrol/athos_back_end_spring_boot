package br.ufpr.athos.campeonato.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "campeonatos")
public class Campeonato {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank(message = "Nome do campeonato é obrigatório")
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String nome;

    @NotBlank(message = "Esporte é obrigatório")
    @Column(nullable = false, length = 50)
    private String esporte;

    @NotNull(message = "Data de início é obrigatória")
    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @NotNull(message = "Data de término é obrigatória")
    @Column(name = "data_fim", nullable = false)
    private LocalDate dataFim;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FormatoCampeonato formato;

    @NotBlank(message = "ID do organizador é obrigatório")
    @Column(name = "organizador_id", nullable = false)
    private String organizadorId;

    @Size(max = 500)
    @Column(length = 500)
    private String descricao;

    @Column(name = "max_equipes")
    private Integer maxEquipes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusCampeonato status;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @OneToMany(mappedBy = "campeonato", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Equipe> equipes = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
        dataAtualizacao = LocalDateTime.now();
        if (status == null) {
            status = StatusCampeonato.INSCRICOES_ABERTAS;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }

    public enum FormatoCampeonato {
        PONTOS_CORRIDOS,
        ELIMINATORIO,
        GRUPOS_ELIMINATORIO,
        MATA_MATA
    }

    public enum StatusCampeonato {
        INSCRICOES_ABERTAS,
        INSCRICOES_FECHADAS,
        EM_ANDAMENTO,
        FINALIZADO,
        CANCELADO
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEsporte() {
        return esporte;
    }

    public void setEsporte(String esporte) {
        this.esporte = esporte;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public FormatoCampeonato getFormato() {
        return formato;
    }

    public void setFormato(FormatoCampeonato formato) {
        this.formato = formato;
    }

    public String getOrganizadorId() {
        return organizadorId;
    }

    public void setOrganizadorId(String organizadorId) {
        this.organizadorId = organizadorId;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getMaxEquipes() {
        return maxEquipes;
    }

    public void setMaxEquipes(Integer maxEquipes) {
        this.maxEquipes = maxEquipes;
    }

    public StatusCampeonato getStatus() {
        return status;
    }

    public void setStatus(StatusCampeonato status) {
        this.status = status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public List<Equipe> getEquipes() {
        return equipes;
    }

    public void setEquipes(List<Equipe> equipes) {
        this.equipes = equipes;
    }
}
