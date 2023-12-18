package com.roberta.alugueltrajes.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class AluguelProdutoId implements Serializable {


    @JsonIgnore
    @Column(name = "id_aluguel")
    private Integer idAluguelEntity;

    @JsonIgnore
    @Column(name = "id_produto")
    private Integer idProdutoEntity;
}
