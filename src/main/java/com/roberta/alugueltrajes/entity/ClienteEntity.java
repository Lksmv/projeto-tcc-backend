package com.roberta.alugueltrajes.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Entity(name = "cliente")
public class ClienteEntity {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cliente_id_cliente_seq")
    @SequenceGenerator(name = "cliente_id_cliente_seq", sequenceName = "cliente_id_cliente_seq", allocationSize = 1)
    @Column(name = "id_cliente")
    @Id
    private Integer idCliente;

    @Column(name = "codigo", unique = true)
    private Integer codigo;

    @Column(name = "nome")
    private String nome;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "nascimento")
    private Date dataNascimento;

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "rede_social")
    private String redeSocial;

    @Column(name = "pessoas_autorizadas")
    private String pessoasAutorizadas;

    @Column(name = "observacoes")
    private String observacoes;

    @Column(name = "CEP")
    private String cep;

    @Column(name = "UF")
    private String uf;

    @Column(name = "endereco")
    private String endereco;

    @Column(name = "bairro")
    private String bairro;

    @Column(name = "cidade")
    private String cidade;

    @Column(name = "ativo")
    private char ativo;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY)
    private Set<AluguelEntity> aluguelEntity = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "clienteEntity", fetch = FetchType.LAZY)
    private Set<CreditoEntity> creditoEntities = new HashSet<>();
}
