package com.roberta.alugueltrajes.entity;

import com.roberta.alugueltrajes.enums.Genero;
import com.roberta.alugueltrajes.enums.StatusProduto;
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
@Entity(name = "produto")
public class ProdutoEntity {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "produto_id_produto_seq")
    @SequenceGenerator(name = "produto_id_produto_seq", sequenceName = "produto_id_produto_seq", allocationSize = 1)
    @Column(name = "id_produto")
    @Id
    private Integer idProduto;

    @Column(name = "codigo", unique = true)
    private Integer codigo;

    @Column(name = "nome")
    private String nome;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private CategoriaEntity categoriaEntity;

    @Enumerated(EnumType.STRING)
    @Column(name = "genero")
    private Genero genero;

    @Column(name = "tamanho")
    private String tamanho;

    @ManyToOne
    @JoinColumn(name = "id_cor")
    private CorEntity corEntity;

    @Column(name = "marca")
    private String marca;

    @Column(name = "valor")
    private double valor;

    @Column(name = "observacoes")
    private String observacoes;

    @Column(name = "status")
    private StatusProduto statusProduto;

    @Column(name = "ativo")
    private char ativo;

    @Column(name = "traje_vendido")
    private char trajeVendido;

    @Column(name = "quantidade")
    private Integer quantidade;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL)
    private List<ImagemProdutoEntity> imagens = new ArrayList<>();
}
