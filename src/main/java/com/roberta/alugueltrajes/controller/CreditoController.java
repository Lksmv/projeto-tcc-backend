package com.roberta.alugueltrajes.controller;

import com.roberta.alugueltrajes.dtos.CreditoCreateDTO;
import com.roberta.alugueltrajes.dtos.CreditoDTO;
import com.roberta.alugueltrajes.exceptions.NaoEncontradoException;
import com.roberta.alugueltrajes.service.CreditoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@Validated
@Slf4j
@RestController
@RequestMapping("/credito")
public class CreditoController {

    private final CreditoService creditoService;

    @Operation(summary = "Criar Crédito", description = "Cria um novo registro de crédito.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Crédito criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados de entrada."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @PostMapping
    public ResponseEntity<CreditoDTO> create(@RequestBody @Valid CreditoCreateDTO credito) throws NaoEncontradoException {
        return new ResponseEntity<>(creditoService.create(credito), HttpStatus.OK);
    }

    @Operation(summary = "Listar Créditos", description = "Retorna uma lista de registros de crédito.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de créditos recuperada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @GetMapping
    public Page<CreditoDTO> list(Pageable pageable) {
        return creditoService.getAll(pageable);
    }

    @Operation(summary = "Atualizar Crédito", description = "Atualiza os detalhes de um registro de crédito existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Crédito atualizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados de entrada."),
            @ApiResponse(responseCode = "404", description = "Crédito não encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @PutMapping("/{idCredito}")
    public ResponseEntity<CreditoDTO> atualizarCredito(
            @PathVariable Integer idCredito,
            @RequestBody @Valid CreditoCreateDTO creditoCreateDTO) throws NaoEncontradoException {
        CreditoDTO creditoDTO = creditoService.update(creditoCreateDTO, idCredito);
        return ResponseEntity.ok(creditoDTO);
    }

    @Operation(summary = "Deletar Crédito", description = "Exclui um registro de crédito existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Crédito excluído com sucesso."),
            @ApiResponse(responseCode = "404", description = "Crédito não encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @DeleteMapping("/{idCredito}")
    public ResponseEntity<Void> deletarCredito(@PathVariable Integer idCredito) throws NaoEncontradoException {
        creditoService.delete(idCredito);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar Crédito por ID", description = "Recupera um registro de crédito por seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Crédito recuperado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Crédito não encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @GetMapping("/{idCredito}")
    public ResponseEntity<CreditoDTO> buscarCreditoPorId(@PathVariable Integer idCredito) throws NaoEncontradoException {
        CreditoDTO creditoDTO = creditoService.getDtoById(idCredito);
        return ResponseEntity.ok(creditoDTO);
    }
}
