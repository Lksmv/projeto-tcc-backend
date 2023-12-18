package com.roberta.alugueltrajes.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "imagem_produto")
public class ImagemProdutoEntity {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "imagem_produto_id_imagem_seq")
    @SequenceGenerator(name = "imagem_produto_id_imagem_seq", sequenceName = "imagem_produto_id_imagem_seq", allocationSize = 1)
    @Column(name = "id_imagem")
    @Id
    private Integer idImagem;

    @Column(name = "caminho_imagem", nullable = false)
    private String caminhoImagem;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_produto")
    private ProdutoEntity produto;
}
