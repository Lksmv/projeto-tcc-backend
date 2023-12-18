package com.roberta.alugueltrajes.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.roberta.alugueltrajes.entity.AluguelEntity;
import com.roberta.alugueltrajes.entity.ProdutoEntity;
import com.roberta.alugueltrajes.enums.StatusAluguelProduto;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
public class AluguelProdutoDTO {

    private ProdutoDTO produtoDTO;

    private StatusAluguelProduto status;
}
