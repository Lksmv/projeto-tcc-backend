package com.roberta.alugueltrajes.dtos;

import com.roberta.alugueltrajes.enums.StatusAluguel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

@Data
public class AluguelCreateDTO {

    @Schema(description = "Codigo", example = "10")
    private Integer codigoCliente;

    @Schema(description = "Codigo", example = "10")
    private Integer codigoFuncionario;

    @Schema(description = "Data de Saída", example = "2023-09-09")
    private Date dataSaida;

    @Schema(description = "Data de Devolução", example = "2023-09-15")
    private Date dataDevolucao;


    @Schema(description = "Utilizar Crédito", example = "S")
    @Pattern(regexp = "S|N", message = "O valor deve ser 'S' ou 'N'")
    private String utilizarCredito;

    @Schema(description = "Observações", example = "Esta é uma observação")
    private String observacoes;

    @Schema(description = "valor dos produtos", example = "100.00")
    private double valor;

    @Schema(description = "Valor Adicional", example = "20.0")
    @DecimalMin("0.0")
    private double valorAdicional;

    @Schema(description = "Lista de Produtos", example = "[1, 2, 3]")
    @NotEmpty
    private List<Integer> listaProdutos;

    @Schema(description = "Produto patrocinado", example = "S")
    @Pattern(regexp = "S|N", message = "O valor deve ser 'S' ou 'N'")
    private String patrocinio;

    @Schema(description = "Forma de pagamento", example = "1")
    private Integer formaPagamento;

    @Schema(description = "Valor pago", example = "10.0")
    private double valorPago;

}
