package com.roberta.alugueltrajes.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "categoria")
public class CategoriaEntity {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categoria_id_categoria_seq")
    @SequenceGenerator(name = "categoria_id_categoria_seq", sequenceName = "categoria_id_categoria_seq", allocationSize = 1)
    @Column(name = "id_categoria")
    @Id
    private Integer idCategoria;

    @Column(name = "codigo", unique = true)
    private Integer codigo;

    @Column(name = "nome")
    private String nome;

    @Column(name = "ativo")
    private char ativo;
}
