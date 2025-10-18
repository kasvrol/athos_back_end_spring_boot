package br.ufpr.athos.avaliacao.dto;

public class MediaAvaliacaoDTO {

    private String usuarioId;
    private Double mediaNotas;
    private Long totalAvaliacoes;

    public MediaAvaliacaoDTO() {}

    public MediaAvaliacaoDTO(String usuarioId, Double mediaNotas, Long totalAvaliacoes) {
        this.usuarioId = usuarioId;
        this.mediaNotas = mediaNotas;
        this.totalAvaliacoes = totalAvaliacoes;
    }

    // Getters and Setters
    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Double getMediaNotas() {
        return mediaNotas;
    }

    public void setMediaNotas(Double mediaNotas) {
        this.mediaNotas = mediaNotas;
    }

    public Long getTotalAvaliacoes() {
        return totalAvaliacoes;
    }

    public void setTotalAvaliacoes(Long totalAvaliacoes) {
        this.totalAvaliacoes = totalAvaliacoes;
    }
}
