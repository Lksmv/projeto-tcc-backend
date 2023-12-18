package com.roberta.alugueltrajes.repository;

import com.roberta.alugueltrajes.entity.AluguelEntity;
import com.roberta.alugueltrajes.entity.ClienteEntity;
import com.roberta.alugueltrajes.entity.UsuarioEntity;
import com.roberta.alugueltrajes.enums.StatusAluguel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.TemporalType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface AluguelRepository extends JpaRepository<AluguelEntity, Integer> {

    List<AluguelEntity> findAllByClienteEntity(ClienteEntity clienteEntity);
    Optional<AluguelEntity> findByCodigo(Integer codigo);

    @Query("SELECT a FROM aluguel a " +
            "WHERE ((cast(:dataInicial as date ) IS NULL AND cast(:dataFinal as date ) IS NULL) " +
            "       OR (a.dataSaida >= cast(:dataInicial as date ) AND a.dataSaida <= cast(:dataFinal as date ))) " +
            "AND (:categoria IS NULL OR EXISTS (SELECT ap FROM a.aluguelProdutoEntities ap WHERE ap.produtoEntity.categoriaEntity.codigo = :categoria)) " +
            "AND (:status IS NULL OR a.statusAluguel = :status)" +
            "order by a.codigo")
    List<AluguelEntity> findAluguelsByDataSaidaBetweenCategoriaAndStatus(
            @Param("dataInicial") Date dataInicial,
            @Param("dataFinal") Date dataFinal,
            @Param("categoria") Integer categoria,
            @Param("status") StatusAluguel status
    );

    @Query("SELECT a FROM aluguel a " +
            "JOIN a.aluguelProdutoEntities ap " +
            "WHERE (a.dataSaida <= :dataDevolucao AND a.dataDevolucao >= :dataSaida) " +
            "AND ap.produtoEntity.codigo = :codigo " +
            "AND ap.status = 1 " +
            "AND a.statusAluguel = 0 " +
            "order by a.codigo")
    List<AluguelEntity> findAluguelsConflitantes(
            @Param("dataSaida") Date dataSaida,
            @Param("dataDevolucao") Date dataDevolucao,
            @Param("codigo") Integer codigo
    );

    @Query("SELECT u FROM aluguel u WHERE u.ativo = :ativo " +
            "AND LOWER(u.clienteEntity.nome) LIKE %:filtro% " +
            "OR CAST(u.codigo AS string) LIKE %:filtro% order by u.codigo")
    Page<AluguelEntity> findAtivosByNomeOrCodigo(@Param("ativo") char ativo, @Param("filtro") String filtro, Pageable pageable);

    @Query("SELECT MIN(codigo + 1) FROM aluguel WHERE codigo + 1 NOT IN (SELECT codigo FROM aluguel )")
    Integer getNextCodigo();

    Integer countByDataEmissaoBetween(Date seteDiasAtras, Date hoje);
    Integer countByDataSaidaAndStatusAluguel(Date data,StatusAluguel statusAluguel);

    Integer countByDataEmissao(Date data);

    Integer countByDataDevolucaoBeforeAndStatusAluguel(Date data,StatusAluguel statusAluguel);
}
