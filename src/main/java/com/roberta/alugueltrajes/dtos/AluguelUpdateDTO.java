package com.roberta.alugueltrajes.dtos;

import com.roberta.alugueltrajes.enums.StatusAluguel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

@Data
public class AluguelUpdateDTO {

    @Schema(description = "Data de Saída", example = "2023-09-09")
    private Date dataSaida;

    @Schema(description = "Data de Devolução", example = "2023-09-15")
    private Date dataDevolucao;

    @Schema(description = "Data de Emissão", example = "2023-09-09")
    private Date dataEmissao;

    @Schema(description = "Observações", example = "Esta é uma observação")
    private String observacoes;

    @Schema(description = "Valor Adicional", example = "20.0")
    @DecimalMin("0.0")
    private double valorAdicional;

    @Schema(description = "Status do aluguel", example = "ABERTO")
    private StatusAluguel statusAluguel;





}
