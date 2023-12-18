package com.roberta.alugueltrajes.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.roberta.alugueltrajes.enums.StatusAluguel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "aluguel")
public class AluguelEntity {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aluguel_id_aluguel_seq")
    @SequenceGenerator(name = "aluguel_id_aluguel_seq", sequenceName = "aluguel_id_aluguel_seq", allocationSize = 1)
    @Column(name = "id_aluguel")
    @Id
    private Integer idAluguel;

    @Column(name = "codigo", unique = true)
    private Integer codigo;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente")
    private ClienteEntity clienteEntity;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_funcionario")
    private FuncionarioEntity funcionarioEntity;

    @Column(name = "data_saida")
    private Date dataSaida;

    @Column(name = "data_devolucao")
    private Date dataDevolucao;

    @Column(name = "data_emissao")
    private Date dataEmissao;

    @Column(name = "utilizar_credito")
    private char utilizarCredito;

    @Column(name = "observacoes")
    private String observacoes;

    @Column(name = "valor_adicional")
    private double valorAdicional;

    @Column(name = "status")
    private StatusAluguel statusAluguel;

    @Column(name = "ativo")
    private char ativo;

    @Column(name = "valor")
    private double valor;

    @Column(name = "patrocinio")
    private char patrocinio;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "aluguelEntity")
    private Set<AluguelProdutoEntity> aluguelProdutoEntities = new HashSet<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "aluguelEntity")
    private Set<PagamentoEntity> pagamentoEntities = new HashSet<>();
}
