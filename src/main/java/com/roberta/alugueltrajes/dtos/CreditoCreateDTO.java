package com.roberta.alugueltrajes.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.PastOrPresent;
import java.util.Date;

@Data
public class CreditoCreateDTO {

    @Schema(description = "Codigo do cliente", example = "10")
    private Integer codigoCliente;

    @Schema(description = "Data", example = "2023-09-09")
    private Date data;

    @Schema(description = "valor", example = "200.00")
    private double valor;

    @Schema(description = "observacoes", example = "credito por cancelamento")
    private String observacoes;

}
