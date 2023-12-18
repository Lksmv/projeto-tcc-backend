package com.roberta.alugueltrajes.dtos;

import com.roberta.alugueltrajes.entity.CategoriaEntity;
import com.roberta.alugueltrajes.enums.Genero;
import com.roberta.alugueltrajes.enums.StatusProduto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ProdutoCreateDTO {

    @Schema(description = "Codigo", example = "10")
    private Integer codigo;

    @Schema(description = "Nome", example = "Produto A")
    @NotBlank
    private String nome;

    @Schema(description = "Codigo categoria", example = "10")
    private Integer codigoCategoria;

    @Schema(description = "Marca produto", example = "Shein")
    private String marca;

    @Schema(description = "Gênero")
    private Genero genero;

    @Schema(description = "Tamanho", example = "M")
    private String tamanho;

    @Schema(description = "Cor", example = "Azul")
    private String cor;

    @Schema(description = "Valor do Produto", example = "99.99")
    @DecimalMin("0.0")
    private double valor;

    @Schema(description = "Observações", example = "Produto em promoção")
    private String observacoes;

}
