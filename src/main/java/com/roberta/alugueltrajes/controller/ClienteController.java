package com.roberta.alugueltrajes.controller;

import com.roberta.alugueltrajes.dtos.ClienteCreateDTO;
import com.roberta.alugueltrajes.dtos.ClienteDTO;
import com.roberta.alugueltrajes.exceptions.NaoEncontradoException;
import com.roberta.alugueltrajes.exceptions.RegraNegocioException;
import com.roberta.alugueltrajes.service.ClienteService;
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
@RequestMapping("/cliente")
public class ClienteController {

    private final ClienteService clienteService;

    @Operation(summary = "Criar Cliente", description = "Cria um novo registro de cliente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados de entrada."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @PostMapping
    public ResponseEntity<ClienteDTO> criarCliente(@RequestBody @Valid ClienteCreateDTO cliente) throws RegraNegocioException {
        return new ResponseEntity<>(clienteService.create(cliente), HttpStatus.OK);
    }

    @Operation(summary = "Listar Clientes", description = "Retorna uma lista paginada de clientes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de clientes recuperada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @GetMapping
    public Page<ClienteDTO> list(@RequestParam("page") int page, @RequestParam("size") int size, @RequestParam(value = "filtro", required = false) String filtro) {
        Pageable pageable = PageRequest.of(page, size);
        return clienteService.findAll(pageable, filtro);
    }


    @Operation(summary = "Atualizar Cliente", description = "Atualiza os detalhes de um cliente existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados de entrada."),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @PutMapping("/{codigo}")
    public ResponseEntity<ClienteDTO> atualizarCliente(
            @PathVariable Integer codigo,
            @RequestBody @Valid ClienteCreateDTO clienteCreateDTO) throws NaoEncontradoException, RegraNegocioException {
        ClienteDTO clienteDTO = clienteService.update(codigo, clienteCreateDTO);
        return ResponseEntity.ok(clienteDTO);
    }

    @Operation(summary = "Deletar Cliente", description = "Exclui um cliente existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cliente excluído com sucesso."),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @DeleteMapping("/{codigo}")
    public void deletarCliente(@PathVariable Integer codigo) throws NaoEncontradoException {
        clienteService.delete(codigo);
        ResponseEntity.ok();
    }

    @Operation(summary = "Buscar Cliente por Codigo", description = "Recupera um cliente por seu Codigo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente recuperado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @GetMapping("/{codigo}")
    public ResponseEntity<ClienteDTO> buscarClientePorId(@PathVariable Integer codigo) throws NaoEncontradoException {
        ClienteDTO clienteDTO = clienteService.getById(codigo);
        return ResponseEntity.ok(clienteDTO);
    }
}
