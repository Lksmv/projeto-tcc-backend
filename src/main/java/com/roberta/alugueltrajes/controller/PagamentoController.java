package com.roberta.alugueltrajes.controller;

import com.roberta.alugueltrajes.dtos.PagamentoCreateDTO;
import com.roberta.alugueltrajes.dtos.PagamentoDTO;
import com.roberta.alugueltrajes.exceptions.NaoEncontradoException;
import com.roberta.alugueltrajes.service.PagamentoService;
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
@RequestMapping("/pagamento")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    @Operation(summary = "Realizar Pagamento", description = "Realiza um novo pagamento.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagamento realizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados de entrada."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @PostMapping
    public ResponseEntity<PagamentoDTO> realizarPagamento(@RequestBody @Valid PagamentoCreateDTO pagamento) throws NaoEncontradoException {
        return new ResponseEntity<>(pagamentoService.criarPagamento(pagamento), HttpStatus.OK);
    }

    @Operation(summary = "Listar Pagamentos", description = "Retorna uma lista de pagamentos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pagamentos recuperada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @GetMapping
    public Page<PagamentoDTO> listarPagamentos(Pageable pageable) {
        return pagamentoService.listarPagamentos(pageable);
    }

    @Operation(summary = "Detalhes do Pagamento", description = "Retorna detalhes de um pagamento existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalhes do pagamento recuperados com sucesso."),
            @ApiResponse(responseCode = "404", description = "Pagamento não encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @GetMapping("/{idPagamento}")
    public ResponseEntity<PagamentoDTO> detalhesPagamento(@PathVariable Integer idPagamento) throws NaoEncontradoException {
        PagamentoDTO pagamentoDTO = pagamentoService.getDetalhesPagamento(idPagamento);
        return ResponseEntity.ok(pagamentoDTO);
    }

    @Operation(summary = "Atualizar Pagamento", description = "Atualiza os detalhes de um pagamento existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagamento atualizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados de entrada."),
            @ApiResponse(responseCode = "404", description = "Pagamento não encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @PutMapping("/{idPagamento}")
    public ResponseEntity<PagamentoDTO> atualizarPagamento(
            @PathVariable Integer idPagamento,
            @RequestBody @Valid PagamentoCreateDTO pagamentoCreateDTO) throws NaoEncontradoException {
        PagamentoDTO pagamentoDTO = pagamentoService.atualizarPagamento(idPagamento, pagamentoCreateDTO);
        return ResponseEntity.ok(pagamentoDTO);
    }

    @Operation(summary = "Deletar Pagamento", description = "Exclui um pagamento existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pagamento excluído com sucesso."),
            @ApiResponse(responseCode = "404", description = "Pagamento não encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @DeleteMapping("/{idPagamento}")
    public ResponseEntity<Void> deletarPagamento(@PathVariable Integer idPagamento) throws NaoEncontradoException {
        pagamentoService.deletarPagamento(idPagamento);
        return ResponseEntity.noContent().build();
    }
}
