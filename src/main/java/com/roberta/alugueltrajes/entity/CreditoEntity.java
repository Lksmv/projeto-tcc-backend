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
@Entity(name = "credito")
public class CreditoEntity {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "credito_id_credito_seq")
    @SequenceGenerator(name = "credito_id_credito_seq", sequenceName = "credito_id_credito_seq", allocationSize = 1)
    @Column(name = "id_credito")
    @Id
    private Integer id_credito;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente")
    private ClienteEntity clienteEntity;

    @Column(name = "data")
    private Date data;

    @Column(name = "valor")
    private double valor;

    @Column(name = "observacoes")
    private String observacoes;


}
