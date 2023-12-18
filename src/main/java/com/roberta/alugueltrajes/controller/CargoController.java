package com.roberta.alugueltrajes.controller;

import com.roberta.alugueltrajes.dtos.CargoDTO;
import com.roberta.alugueltrajes.exceptions.NaoEncontradoException;
import com.roberta.alugueltrajes.service.CargoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@Validated
@Slf4j
@RestController
@RequestMapping("/cargo")
public class CargoController {

    private final CargoService cargoService;

    @Operation(summary = "Buscar Cargo por ID", description = "Recupera um cargo por seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cargo recuperado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Cargo não encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CargoDTO> buscarCargoPorId(@PathVariable Integer id) throws NaoEncontradoException {
        CargoDTO cargo = cargoService.getDtoById(id);
        return ResponseEntity.ok(cargo);
    }

    @Operation(summary = "Listar Cargos", description = "Retorna uma lista de todos os cargos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de cargos recuperada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @GetMapping
    public ResponseEntity<List<CargoDTO>> listarCargos() {
        List<CargoDTO> cargos = cargoService.getAll();
        return ResponseEntity.ok(cargos);
    }
}
