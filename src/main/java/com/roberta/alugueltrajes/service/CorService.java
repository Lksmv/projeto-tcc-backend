package com.roberta.alugueltrajes.service;

import com.roberta.alugueltrajes.entity.CorEntity;
import com.roberta.alugueltrajes.repository.CorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CorService {
    private final CorRepository corRepository;

    public List<CorEntity> getAllCores() {
        return corRepository.findAll();
    }

    public List<CorEntity> getAllCoresByNomeContins(String nome) {
        return corRepository.findAllByNomeContainsIgnoreCase(nome);
    }

    public Optional<CorEntity> getCorById(Integer id) {
        return corRepository.findById(id);
    }

    public CorEntity saveCor(CorEntity corEntity) {
        return corRepository.save(corEntity);
    }

    public void deleteCor(Integer id) {
        corRepository.deleteById(id);
    }

    public CorEntity updateCor(Integer id, CorEntity corEntity) {
        if (corRepository.existsById(id)) {
            corEntity.setIdCor(id);
            return corRepository.save(corEntity);
        }
        return null;
    }

    public Optional<CorEntity> findCor(String nome) {
        return corRepository.findByNomeIgnoreCase(nome);
    }
}
