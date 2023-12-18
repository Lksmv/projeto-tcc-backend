package com.roberta.alugueltrajes.controller;

import com.roberta.alugueltrajes.entity.CorEntity;
import com.roberta.alugueltrajes.service.CorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Validated
@Slf4j
@RestController
@RequestMapping("/cor")
public class CorController {

    private final CorService corService;

    @GetMapping
    public ResponseEntity<List<CorEntity>> getAllCores() {
        List<CorEntity> cores = corService.getAllCores();
        return ResponseEntity.ok(cores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CorEntity> getCorById(@PathVariable Integer id) {
        Optional<CorEntity> cor = corService.getCorById(id);
        if (cor.isPresent()) {
            return ResponseEntity.ok(cor.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<CorEntity>> getAllCoresByNomeContains(@RequestParam String nome) {
        List<CorEntity> cores = corService.getAllCoresByNomeContins(nome);
        return ResponseEntity.ok(cores);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCor(@PathVariable Integer id) {
        corService.deleteCor(id);
        return ResponseEntity.noContent().build();
    }
}
