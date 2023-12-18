package com.roberta.alugueltrajes.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.PastOrPresent;
import java.util.Date;

@Data
public class FuncionarioCreateDTO {

    @Schema(description = "Nome funcionario", example = "john doe")
    private String nome;
}
