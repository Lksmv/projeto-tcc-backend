package com.roberta.alugueltrajes.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;

@Data
public class FormaDePagamentoDTO {

    private Integer idFormaDePagamento;

    private String nome;

    private double taxa;
}
