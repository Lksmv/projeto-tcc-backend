package com.roberta.alugueltrajes.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.PastOrPresent;
import java.util.Date;

@Data
public class FormaDePagamentoCreateDTO {

    @Schema(description = "taxa", example = "200.00")
    private double taxa;

    @Schema(description = "nome", example = "credito")
    private String nome;
}
