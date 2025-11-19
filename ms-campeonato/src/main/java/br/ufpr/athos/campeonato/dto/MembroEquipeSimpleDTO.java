package br.ufpr.athos.campeonato.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO simplificado para membros de equipe
 * Usado para representação aninhada dentro de EquipeResponseDTO
 */
public class MembroEquipeSimpleDTO {

    private String usuarioId;

    @JsonProperty("nomeUsuario")
    private String usuarioNome;

    private String dataEntrada;

    public MembroEquipeSimpleDTO() {
    }

    public MembroEquipeSimpleDTO(String usuarioId, String usuarioNome, String dataEntrada) {
        this.usuarioId = usuarioId;
        this.usuarioNome = usuarioNome;
        this.dataEntrada = dataEntrada;
    }

    // Getters and Setters
    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuarioNome() {
        return usuarioNome;
    }

    public void setUsuarioNome(String usuarioNome) {
        this.usuarioNome = usuarioNome;
    }

    public String getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(String dataEntrada) {
        this.dataEntrada = dataEntrada;
    }
}
