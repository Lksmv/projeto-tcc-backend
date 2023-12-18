package com.roberta.alugueltrajes.repository;

import com.roberta.alugueltrajes.entity.CargoEntity;
import com.roberta.alugueltrajes.entity.CategoriaEntity;
import com.roberta.alugueltrajes.entity.ClienteEntity;
import com.roberta.alugueltrajes.entity.PagamentoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PagamentoRepository extends JpaRepository<PagamentoEntity, Integer> {

}


