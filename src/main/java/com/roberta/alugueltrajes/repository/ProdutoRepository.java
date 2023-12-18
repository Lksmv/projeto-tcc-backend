package com.roberta.alugueltrajes.repository;

import com.roberta.alugueltrajes.entity.ClienteEntity;
import com.roberta.alugueltrajes.entity.ProdutoEntity;
import com.roberta.alugueltrajes.entity.UsuarioEntity;
import com.roberta.alugueltrajes.enums.Genero;
import com.roberta.alugueltrajes.enums.StatusProduto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<ProdutoEntity, Integer> {

    @Query("SELECT u FROM produto u WHERE u.ativo = :ativo AND (LOWER(u.nome) LIKE %:filtro% OR CAST(u.codigo AS string) LIKE %:filtro%) order by u.codigo")
    Page<ProdutoEntity> findAtivosByNomeOrCodigo(@Param("ativo") char ativo, @Param("filtro") String filtro, Pageable pageable);

    @Query("SELECT p FROM produto p " +
            "WHERE (:categoria is null or p.categoriaEntity.codigo = :categoria) " +
            "AND (:cor is null or p.corEntity.nome = :cor) " +
            "AND (:genero is null or p.genero = :genero) " +
            "AND (:tamanho is null or p.tamanho = :tamanho) " +
            "AND p.ativo = 'T' " +
            "AND EXISTS (SELECT 1 FROM p.imagens i) ")
    Page<ProdutoEntity> findProdutosFiltradosComImagens(
            @Param("categoria") Integer categoria,
            @Param("cor") String cor,
            @Param("genero") Genero genero,
            @Param("tamanho") String tamanho,
            Pageable pageable
    );

    Page<ProdutoEntity> findProdutoEntitiesByImagensIsNotEmpty(Pageable pageable);

    Optional<ProdutoEntity> findByCodigo(Integer codigo);

    @Query("SELECT MIN(codigo + 1) FROM produto WHERE codigo + 1 NOT IN (SELECT codigo FROM produto )")
    Integer getNextCodigo();

    Integer countByStatusProduto(StatusProduto statusProduto);



}
