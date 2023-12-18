package com.roberta.alugueltrajes.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.roberta.alugueltrajes.entity.AluguelProdutoEntity;
import com.roberta.alugueltrajes.entity.ClienteEntity;
import com.roberta.alugueltrajes.entity.UsuarioEntity;
import com.roberta.alugueltrajes.enums.StatusAluguel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
public class AluguelDTO {

    private Integer codigo;

    private ClienteDTO clienteDTO;

    private FuncionarioDTO funcionarioDTO;

    private double valor;

    private Date dataSaida;

    private Date dataDevolucao;

    private Date dataEmissao;

    private String utilizarCredito;

    private String observacoes;

    private double valorAdicional;

    private StatusAluguel statusAluguel;

    private Set<AluguelProdutoDTO> listaProdutos;

    private Set<PagamentoDTO> listaPagamentos;
}
