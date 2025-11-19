package br.ufpr.athos.campeonato.dto;

import jakarta.validation.constraints.NotBlank;

public class AdicionarMembroDTO {

    @NotBlank(message = "ID do usuário é obrigatório")
    private String usuarioId;

    public AdicionarMembroDTO() {
    }

    public AdicionarMembroDTO(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }
}
