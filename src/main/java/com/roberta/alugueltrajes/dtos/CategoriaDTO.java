package com.roberta.alugueltrajes.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CategoriaDTO {
    
    private Integer codigo;

    private String nome;
}
