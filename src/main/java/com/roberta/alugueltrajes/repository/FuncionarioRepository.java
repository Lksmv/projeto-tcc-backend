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
public interface FuncionarioRepository extends JpaRepository<FuncionarioEntity,Integer> {

    @Query("SELECT u FROM funcionario u WHERE u.ativo = :ativo AND (LOWER(u.nome) LIKE %:filtro% OR CAST(u.codigo AS string) LIKE %:filtro%) order by u.codigo")
    Page<FuncionarioEntity> findAtivosByNomeOrCodigo(@Param("ativo") char ativo, @Param("filtro") String filtro, Pageable pageable);

    @Query("SELECT MIN(codigo + 1) FROM funcionario WHERE codigo + 1 NOT IN (SELECT codigo FROM funcionario )")
    Integer getNextCodigo();



}
