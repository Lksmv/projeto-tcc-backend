package com.roberta.alugueltrajes.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.roberta.alugueltrajes.enums.StatusAluguelProduto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "ALUGUEL_PRODUTO")
public class
AluguelProdutoEntity {

    @EmbeddedId
    private AluguelProdutoId aluguelProdutoId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_produto",insertable = false,updatable = false)
    private ProdutoEntity produtoEntity;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_aluguel",insertable = false,updatable = false)
    private AluguelEntity aluguelEntity;

    @Column(name = "status")
    private StatusAluguelProduto status;
}
