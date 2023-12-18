package com.roberta.alugueltrajes.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UsuarioCreateDTO {

    @Schema(description = "Codigo", example = "10")
    private Integer codigo;

    @Schema(description = "ID do Cargo", example = "1")
    private Integer idCargo;

    @Schema(description = "Nome", example = "João Silva")
    @NotBlank
    private String nome;

    @Schema(description = "Login", example = "joao123")
    @NotBlank
    private String login;

    @Schema(description = "Senha", example = "senha123")
    private String senha;
}
