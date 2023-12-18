package com.roberta.alugueltrajes.controller;

import com.roberta.alugueltrajes.dtos.FuncionarioCreateDTO;
import com.roberta.alugueltrajes.dtos.FuncionarioDTO;
import com.roberta.alugueltrajes.dtos.UsuarioDTO;
import com.roberta.alugueltrajes.exceptions.NaoEncontradoException;
import com.roberta.alugueltrajes.service.FuncionarioService;
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
@RequestMapping("/funcionario")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    @Operation(summary = "Criar Funcionário", description = "Cria um novo funcionário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funcionário criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados de entrada."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @PostMapping
    public ResponseEntity<FuncionarioDTO> create(@RequestBody @Valid FuncionarioCreateDTO funcionario) {
        return new ResponseEntity<>(funcionarioService.create(funcionario), HttpStatus.OK);
    }

    @Operation(summary = "Listar Funcionarios", description = "Retorna uma lista paginada de funcionarios.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de funcionarios recuperada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @GetMapping
    public Page<FuncionarioDTO> list(@RequestParam("page") int page, @RequestParam("size") int size, @RequestParam(value = "filtro", required = false) String filtro) {
        Pageable pageable = PageRequest.of(page, size);
        return funcionarioService.findAll(pageable, filtro);
    }

    @Operation(summary = "Atualizar Funcionário", description = "Atualiza os detalhes de um funcionário existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funcionário atualizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados de entrada."),
            @ApiResponse(responseCode = "404", description = "Funcionário não encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @PutMapping("/{codigo}")
    public ResponseEntity<FuncionarioDTO> atualizarFuncionario(
            @PathVariable Integer codigo,
            @RequestBody @Valid FuncionarioCreateDTO funcionarioCreateDTO) throws NaoEncontradoException {
        FuncionarioDTO funcionarioDTO = funcionarioService.update(funcionarioCreateDTO, codigo);
        return ResponseEntity.ok(funcionarioDTO);
    }

    @Operation(summary = "Deletar Funcionário", description = "Exclui um funcionário existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Funcionário excluído com sucesso."),
            @ApiResponse(responseCode = "404", description = "Funcionário não encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> deletarFuncionario(@PathVariable Integer codigo) throws NaoEncontradoException {
        funcionarioService.delete(codigo);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar Funcionário por Codigo", description = "Recupera um funcionário por seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funcionário recuperado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Funcionário não encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @GetMapping("/{codigo}")
    public ResponseEntity<FuncionarioDTO> buscarFuncionarioPorId(@PathVariable Integer codigo) throws NaoEncontradoException {
        FuncionarioDTO funcionarioDTO = funcionarioService.getDtoByCodigo(codigo);
        return ResponseEntity.ok(funcionarioDTO);
    }
}
