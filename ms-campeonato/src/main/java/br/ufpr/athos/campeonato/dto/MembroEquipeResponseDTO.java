package br.ufpr.athos.campeonato.dto;

import java.time.LocalDateTime;

public class MembroEquipeResponseDTO {

    private String id;
    private String usuarioId;
    private String usuarioNome;
    private String equipeId;
    private String equipeNome;
    private LocalDateTime dataEntrada;

    public MembroEquipeResponseDTO() {
    }

    public MembroEquipeResponseDTO(String id, String usuarioId, String usuarioNome,
                                   String equipeId, String equipeNome, LocalDateTime dataEntrada) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.usuarioNome = usuarioNome;
        this.equipeId = equipeId;
        this.equipeNome = equipeNome;
        this.dataEntrada = dataEntrada;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public LocalDateTime getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDateTime dataEntrada) {
        this.dataEntrada = dataEntrada;
    }
}
