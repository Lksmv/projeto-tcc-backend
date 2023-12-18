package com.roberta.alugueltrajes.repository;

import com.roberta.alugueltrajes.entity.ClienteEntity;
import com.roberta.alugueltrajes.entity.UsuarioEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ClienteRepository extends JpaRepository<ClienteEntity, Integer> {

    @Query("SELECT u FROM cliente u " +
            "WHERE u.ativo = :ativo " +
            "AND (LOWER(u.nome) LIKE %:filtro% " +
            "OR CAST(u.codigo AS string) LIKE %:filtro% " +
            "OR LOWER(u.cpf) LIKE %:filtro%) order by u.codigo")
    Page<ClienteEntity> findAtivosByNomeOrCodigoOrCpf(@Param("ativo") char ativo, @Param("filtro") String filtro, Pageable pageable);

    Optional<ClienteEntity> findClienteEntityByCodigo(Integer codigo);

    @Query ("SELECT MIN(codigo + 1) FROM cliente WHERE codigo + 1 NOT IN (SELECT codigo FROM cliente)")
    Integer getNextCodigo();

}
