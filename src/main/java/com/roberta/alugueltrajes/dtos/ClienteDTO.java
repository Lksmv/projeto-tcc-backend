package com.roberta.alugueltrajes.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.roberta.alugueltrajes.entity.AluguelEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
public class ClienteDTO {

    private Integer codigo;

    private String nome;

    private String cpf;

    private Date dataNascimento;

    private String telefone;

    private String redeSocial;

    private String pessoasAutorizadas;

    private String observacoes;

    private String cep;

    private String uf;

    private String endereco;

    private String bairro;

    private String cidade;

    private char ativo;

    private List<CreditoDTO> listaCreditos;

}
