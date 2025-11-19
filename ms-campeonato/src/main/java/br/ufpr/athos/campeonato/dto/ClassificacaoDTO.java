package br.ufpr.athos.campeonato.dto;

public class ClassificacaoDTO {
    private Integer posicao;
    private EquipeResponseDTO equipe;
    private Integer jogos;
    private Integer vitorias;
    private Integer empates;
    private Integer derrotas;
    private Integer golsFeitos;
    private Integer golsSofridos;
    private Integer saldoGols;
    private Integer pontos;

    // Default constructor
    public ClassificacaoDTO() {
    }

    // Constructor with all fields for easy creation
    public ClassificacaoDTO(Integer posicao, EquipeResponseDTO equipe, Integer jogos,
                           Integer vitorias, Integer empates, Integer derrotas, Integer golsFeitos,
                           Integer golsSofridos, Integer saldoGols, Integer pontos) {
        this.posicao = posicao;
        this.equipe = equipe;
        this.jogos = jogos;
        this.vitorias = vitorias;
        this.empates = empates;
        this.derrotas = derrotas;
        this.golsFeitos = golsFeitos;
        this.golsSofridos = golsSofridos;
        this.saldoGols = saldoGols;
        this.pontos = pontos;
    }

    // Getters and Setters
    public Integer getPosicao() {
        return posicao;
    }

    public void setPosicao(Integer posicao) {
        this.posicao = posicao;
    }

    public EquipeResponseDTO getEquipe() {
        return equipe;
    }

    public void setEquipe(EquipeResponseDTO equipe) {
        this.equipe = equipe;
    }

    public Integer getJogos() {
        return jogos;
    }

    public void setJogos(Integer jogos) {
        this.jogos = jogos;
    }

    public Integer getVitorias() {
        return vitorias;
    }

    public void setVitorias(Integer vitorias) {
        this.vitorias = vitorias;
    }

    public Integer getEmpates() {
        return empates;
    }

    public void setEmpates(Integer empates) {
        this.empates = empates;
    }

    public Integer getDerrotas() {
        return derrotas;
    }

    public void setDerrotas(Integer derrotas) {
        this.derrotas = derrotas;
    }

    public Integer getGolsFeitos() {
        return golsFeitos;
    }

    public void setGolsFeitos(Integer golsFeitos) {
        this.golsFeitos = golsFeitos;
    }

    public Integer getGolsSofridos() {
        return golsSofridos;
    }

    public void setGolsSofridos(Integer golsSofridos) {
        this.golsSofridos = golsSofridos;
    }

    public Integer getSaldoGols() {
        return saldoGols;
    }

    public void setSaldoGols(Integer saldoGols) {
        this.saldoGols = saldoGols;
    }

    public Integer getPontos() {
        return pontos;
    }

    public void setPontos(Integer pontos) {
        this.pontos = pontos;
    }

    // Helper method for getting equipe nome (used for sorting)
    public String getEquipeNome() {
        return equipe != null ? equipe.getNome() : "";
    }
}
