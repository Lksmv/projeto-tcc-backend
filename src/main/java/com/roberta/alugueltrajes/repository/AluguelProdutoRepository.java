package com.roberta.alugueltrajes.repository;

import com.roberta.alugueltrajes.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface AluguelProdutoRepository extends JpaRepository<AluguelProdutoEntity, AluguelProdutoId> {

    List<AluguelProdutoEntity> findAluguelProdutoEntitiesByProdutoEntity(ProdutoEntity produto);


}
