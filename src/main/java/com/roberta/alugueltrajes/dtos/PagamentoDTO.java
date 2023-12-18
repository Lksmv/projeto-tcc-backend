package com.roberta.alugueltrajes.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.Data;

import java.util.Date;

@Data
public class PagamentoDTO {

    private double valor;

    private Date data;

    private FormaDePagamentoDTO formaDePagamentoDTO;

}
