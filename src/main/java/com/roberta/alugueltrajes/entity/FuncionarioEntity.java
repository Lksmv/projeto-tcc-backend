package com.roberta.alugueltrajes.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "funcionario")
public class FuncionarioEntity {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "funcionario_id_funcionario_seq")
    @SequenceGenerator(name = "funcionario_id_funcionario_seq", sequenceName = "funcionario_id_funcionario_seq", allocationSize = 1)
    @Column(name = "id_funcionario")
    @Id
    private Integer idFuncionario;

    @Column(name = "codigo", unique = true)
    private Integer codigo;

    @Column(name = "nome")
    private String nome;

    @Column(name = "ativo")
    private char ativo;
}
