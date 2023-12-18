package com.roberta.alugueltrajes.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class AluguelDiaDTO {

    private Date dia;

    private Integer quantidade;

    private String diaDaSemana;
}
