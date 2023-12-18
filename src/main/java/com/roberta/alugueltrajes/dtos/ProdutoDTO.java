package com.roberta.alugueltrajes.dtos;

import com.roberta.alugueltrajes.entity.CategoriaEntity;
import com.roberta.alugueltrajes.enums.Genero;
import com.roberta.alugueltrajes.enums.StatusProduto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.Column;
import java.util.List;

@Data
public class ProdutoDTO {

    private Integer codigo;

    private String nome;

    private CategoriaDTO categoria;

    private Genero genero;

    private String tamanho;

    private String marca;

    private String cor;

    private List<ImagemProdutoDTO> imagens;

    private double valor;

    private String observacoes;

    private StatusProduto statusProduto;

    private char trajeVendido;

    private Integer quantidade;

}
