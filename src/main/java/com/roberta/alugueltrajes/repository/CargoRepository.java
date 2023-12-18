package com.roberta.alugueltrajes.repository;

import com.roberta.alugueltrajes.entity.CargoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CargoRepository extends JpaRepository<CargoEntity,Integer> {

}
