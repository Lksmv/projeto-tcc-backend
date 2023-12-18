package com.roberta.alugueltrajes.repository;

import com.roberta.alugueltrajes.entity.CorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CorRepository extends JpaRepository<CorEntity,Integer> {

    Optional<CorEntity> findByNomeIgnoreCase(String nome);

    List<CorEntity> findAllByNomeContainsIgnoreCase(String nome);



}
