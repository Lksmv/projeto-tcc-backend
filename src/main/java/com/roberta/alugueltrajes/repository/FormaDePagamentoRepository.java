package com.roberta.alugueltrajes.repository;

import com.roberta.alugueltrajes.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FormaDePagamentoRepository extends JpaRepository<FormaDePagamentoEntity,Integer> {

    @Query("SELECT u FROM forma_de_pagamento u " +
            "WHERE u.ativo = :ativo " +
            "AND (LOWER(u.nome) LIKE %:filtro% " +
            "OR CAST(u.idFormaDePagamento AS string) LIKE %:filtro%) " +
            "order by u.idFormaDePagamento")
    Page<FormaDePagamentoEntity> findAtivosByNome(@Param("ativo") char ativo, @Param("filtro") String filtro, Pageable pageable);


}
