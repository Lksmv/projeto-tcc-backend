package com.roberta.alugueltrajes.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "pagamento")
public class PagamentoEntity {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pagamento_id_pagamento_seq")
    @SequenceGenerator(name = "pagamento_id_pagamento_seq", sequenceName = "pagamento_id_pagamento_seq", allocationSize = 1)
    @Column(name = "id_pagamento")
    @Id
    private Integer idPagamento;

    @Column(name = "valor")
    private double valor;

    @Column(name = "data")
    private Date data;

    @ManyToOne
    @JoinColumn(name = "id_forma_de_pagamento")
    private FormaDePagamentoEntity formaDePagamento;

    @ManyToOne
    @JoinColumn(name = "id_aluguel")
    private AluguelEntity aluguelEntity;
}
