package com.roberta.alugueltrajes.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CategoriaCreateDTO {

    @Schema(description = "Codigo", example = "10")
    private Integer codigo;

    @Schema(description = "Nome", example = "Terno")
    @NotBlank
    private String nome;
}
