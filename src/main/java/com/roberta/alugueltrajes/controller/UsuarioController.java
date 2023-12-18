package com.roberta.alugueltrajes.controller;

import com.roberta.alugueltrajes.dtos.ClienteDTO;
import com.roberta.alugueltrajes.dtos.UsuarioCreateDTO;
import com.roberta.alugueltrajes.dtos.UsuarioDTO;
import com.roberta.alugueltrajes.exceptions.NaoEncontradoException;
import com.roberta.alugueltrajes.exceptions.RegraNegocioException;
import com.roberta.alugueltrajes.service.UsuarioService;
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
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Operation(summary = "Criar Usuário", description = "Cria um novo registro de usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados de entrada."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @PostMapping
    public ResponseEntity<UsuarioDTO> create(@RequestBody @Valid UsuarioCreateDTO usuario) throws NaoEncontradoException, RegraNegocioException {
        return new ResponseEntity<>(usuarioService.create(usuario), HttpStatus.OK);
    }

    @Operation(summary = "Listar Usuários", description = "Retorna uma lista paginada de usuários.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuários recuperada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @GetMapping
    public Page<UsuarioDTO> list(@RequestParam("page") int page, @RequestParam("size") int size, @RequestParam(value = "filtro", required = false) String filtro) {
        Pageable pageable = PageRequest.of(page, size);
        return usuarioService.findAll(pageable, filtro);
    }

    @Operation(summary = "Atualizar Usuário", description = "Atualiza os detalhes de um usuário existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados de entrada."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @PutMapping("/{codigo}")
    public ResponseEntity<UsuarioDTO> atualizarUsuario(
            @PathVariable Integer codigo,
            @RequestBody @Valid UsuarioCreateDTO usuarioCreateDTO) throws NaoEncontradoException, RegraNegocioException {
        UsuarioDTO usuarioDTO = usuarioService.Update(usuarioCreateDTO, codigo);
        return ResponseEntity.ok(usuarioDTO);
    }

    @Operation(summary = "Deletar Usuário", description = "Exclui um usuário existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Integer codigo) throws NaoEncontradoException {
        usuarioService.delete(codigo);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar Usuário por Codigo", description = "Recupera um usuário por seu Codigo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário recuperado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @GetMapping("/{codigo}")
    public ResponseEntity<UsuarioDTO> buscarUsuarioPorCodigo(@PathVariable Integer codigo) throws NaoEncontradoException {
        UsuarioDTO usuarioDTO = usuarioService.getByCodigo(codigo);
        return ResponseEntity.ok(usuarioDTO);
    }


}
