package com.roberta.alugueltrajes.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roberta.alugueltrajes.dtos.CargoDTO;
import com.roberta.alugueltrajes.entity.CargoEntity;
import com.roberta.alugueltrajes.exceptions.NaoEncontradoException;
import com.roberta.alugueltrajes.repository.CargoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CargoService {

    private final CargoRepository repository;
    private final ObjectMapper objectMapper;

    public CargoEntity getById(Integer id) throws NaoEncontradoException {
        return repository.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Cargo não encontrado com o ID: " + id));
    }

    public CargoDTO getDtoById(Integer id) throws NaoEncontradoException {
        return toDto(repository.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Cargo não encontrado com o ID: " + id)));
    }

    public List<CargoDTO> getAll() {
        return repository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public CargoDTO toDto(CargoEntity cargo) {
        return objectMapper.convertValue(cargo, CargoDTO.class);
    }

}
