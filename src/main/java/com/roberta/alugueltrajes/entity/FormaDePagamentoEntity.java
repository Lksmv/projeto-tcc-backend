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
@Entity(name = "forma_de_pagamento")
public class FormaDePagamentoEntity {


    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "forma_de_pagamento_id_forma_pagamento_seq")
    @SequenceGenerator(name = "forma_de_pagamento_id_forma_pagamento_seq", sequenceName = "forma_de_pagamento_id_forma_pagamento_seq", allocationSize = 1)
    @Column(name = "id_forma_pagamento")
    @Id
    private Integer idFormaDePagamento;

    @Column(name = "nome")
    private String nome;

    @Column(name = "taxa")
    private double taxa;

    @Column(name = "ativo")
    private char ativo;
}
