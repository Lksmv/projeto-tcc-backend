package com.roberta.alugueltrajes.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.roberta.alugueltrajes.entity.AluguelEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@Data
public class CreditoDTO {

    private Date data;

    private double valor;

    private String observacoes;
}
