package com.roberta.alugueltrajes.dtos;

import com.roberta.alugueltrajes.enums.StatusAluguelProduto;
import lombok.Data;


@Data
public class AluguelProdutoUpdateDTO {

    private Integer codigoProduto;

    private Integer codigoAluguel;

    private StatusAluguelProduto status;

}
