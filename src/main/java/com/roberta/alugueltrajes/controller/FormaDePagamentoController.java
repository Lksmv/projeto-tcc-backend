package com.roberta.alugueltrajes.controller;

import com.roberta.alugueltrajes.dtos.FormaDePagamentoCreateDTO;
import com.roberta.alugueltrajes.dtos.FormaDePagamentoDTO;
import com.roberta.alugueltrajes.dtos.UsuarioDTO;
import com.roberta.alugueltrajes.exceptions.NaoEncontradoException;
import com.roberta.alugueltrajes.exceptions.RegraNegocioException;
import com.roberta.alugueltrajes.service.FormaDePagamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
@RequestMapping("/forma-de-pagamento")
public class FormaDePagamentoController {

    private final FormaDePagamentoService formaDePagamentoService;

    @Operation(summary = "Criar Forma de Pagamento", description = "Cria uma nova forma de pagamento.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Forma de pagamento criada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados de entrada."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @PostMapping
    public ResponseEntity<FormaDePagamentoDTO> create(@RequestBody @Valid FormaDePagamentoCreateDTO formaDePagamento) throws RegraNegocioException {
        return new ResponseEntity<>(formaDePagamentoService.create(formaDePagamento), HttpStatus.OK);
    }

    @Operation(summary = "Listar Formas de Pagamento", description = "Retorna uma lista de formas de pagamento.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de formas de pagamento recuperada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @GetMapping
    public Page<FormaDePagamentoDTO> list(@RequestParam("page") int page, @RequestParam("size") int size, @RequestParam(value = "filtro", required = false) String filtro) {
        Pageable pageable = PageRequest.of(page, size);
        return formaDePagamentoService.findAll(pageable, filtro);
    }
    @Operation(summary = "Atualizar Forma de Pagamento", description = "Atualiza os detalhes de uma forma de pagamento existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Forma de pagamento atualizada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados de entrada."),
            @ApiResponse(responseCode = "404", description = "Forma de pagamento não encontrada."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @PutMapping("/{idFormaDePagamento}")
    public ResponseEntity<FormaDePagamentoDTO> atualizarFormaDePagamento(
            @PathVariable Integer idFormaDePagamento,
            @RequestBody @Valid FormaDePagamentoCreateDTO formaDePagamentoCreateDTO) throws NaoEncontradoException, RegraNegocioException {
        FormaDePagamentoDTO formaDePagamentoDTO = formaDePagamentoService.update(formaDePagamentoCreateDTO, idFormaDePagamento);
        return ResponseEntity.ok(formaDePagamentoDTO);
    }

    @Operation(summary = "Deletar Forma de Pagamento", description = "Exclui uma forma de pagamento existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Forma de pagamento excluída com sucesso."),
            @ApiResponse(responseCode = "404", description = "Forma de pagamento não encontrada."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @DeleteMapping("/{idFormaDePagamento}")
    public ResponseEntity<Void> deletarFormaDePagamento(@PathVariable Integer idFormaDePagamento) throws NaoEncontradoException {
        formaDePagamentoService.delete(idFormaDePagamento);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar Forma de Pagamento por ID", description = "Recupera uma forma de pagamento por seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Forma de pagamento recuperada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Forma de pagamento não encontrada."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @GetMapping("/{idFormaDePagamento}")
    public ResponseEntity<FormaDePagamentoDTO> buscarFormaDePagamentoPorId(@PathVariable Integer idFormaDePagamento) throws NaoEncontradoException {
        FormaDePagamentoDTO formaDePagamentoDTO = formaDePagamentoService.getDtoById(idFormaDePagamento);
        return ResponseEntity.ok(formaDePagamentoDTO);
    }
}