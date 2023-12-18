package com.roberta.alugueltrajes.dtos;

import lombok.Data;

import javax.persistence.Column;

@Data
public class ImagemProdutoDTO {

    private Integer idImagem;

    private String caminhoImagem;
}
