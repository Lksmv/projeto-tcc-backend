package com.roberta.alugueltrajes.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "cor")
public class CorEntity {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cor_id_cor_seq")
    @SequenceGenerator(name = "cor_id_cor_seq", sequenceName = "cor_id_cor_seq", allocationSize = 1)
    @Column(name = "id_cor")
    @Id
    private Integer idCor;

    @Column(name = "nome")
    private String nome;

}
