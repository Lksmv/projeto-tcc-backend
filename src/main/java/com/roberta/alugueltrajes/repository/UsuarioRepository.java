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
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {

    @Query("SELECT u FROM usuario u WHERE u.ativo = :ativo AND (LOWER(u.nome) LIKE %:filtro% OR CAST(u.codigo AS string) LIKE %:filtro%) order by u.codigo")
    Page<UsuarioEntity> findAtivosByNomeOrCodigo(@Param("ativo") char ativo, @Param("filtro") String filtro, Pageable pageable);

    Optional<UsuarioEntity> findUsuarioEntityByLogin(String login);

    Optional<UsuarioEntity> findByCodigo(Integer codigo);

    @Query("SELECT MIN(codigo + 1) FROM usuario WHERE codigo + 1 NOT IN (SELECT codigo FROM usuario )")
    Integer getNextCodigo();


}
