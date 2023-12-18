package com.roberta.alugueltrajes.dtos;

import com.roberta.alugueltrajes.entity.AluguelEntity;
import com.roberta.alugueltrajes.entity.FormaDePagamentoEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@Data
public class PagamentoCreateDTO {


    @Schema(description = "valor", example = "100.00")
    private double valor;

    @Schema(description = "id Forma de pagamento", example = "1")
    private Integer idFormaPagamento;

    @Schema(description = "Codigo", example = "10")
    private Integer codigoAluguel;
}
