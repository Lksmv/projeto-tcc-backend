package com.roberta.alugueltrajes.controller;

import com.roberta.alugueltrajes.dtos.CategoriaCreateDTO;
import com.roberta.alugueltrajes.dtos.CategoriaDTO;
import com.roberta.alugueltrajes.dtos.UsuarioCreateDTO;
import com.roberta.alugueltrajes.dtos.UsuarioDTO;
import com.roberta.alugueltrajes.exceptions.NaoEncontradoException;
import com.roberta.alugueltrajes.exceptions.RegraNegocioException;
import com.roberta.alugueltrajes.service.CategoriaService;
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
import java.util.List;

@RequiredArgsConstructor
@Validated
@Slf4j
@RestController
@RequestMapping("/categoria")
public class CategoriaController {

    private final CategoriaService categoriaService;

    @Operation(summary = "Criar Categoria", description = "Cria uma nova categoria.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria criada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados de entrada."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @PostMapping
    public ResponseEntity<CategoriaDTO> create(@RequestBody @Valid CategoriaCreateDTO categoria) throws RegraNegocioException {
        return new ResponseEntity<>(categoriaService.create(categoria), HttpStatus.OK);
    }

    @Operation(summary = "Listar Categorias", description = "Retorna uma lista de categorias.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de categorias recuperada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @GetMapping
    public Page<CategoriaDTO> list(@RequestParam("page") int page, @RequestParam("size") int size, @RequestParam(value = "filtro", required = false) String filtro) {
        Pageable pageable = PageRequest.of(page, size);
        return categoriaService.findAll(pageable, filtro);
    }

    @Operation(summary = "Atualizar Categoria", description = "Atualiza os detalhes de um Categoria existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria atualizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados de entrada."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @PutMapping("/{codigo}")
    public ResponseEntity<CategoriaDTO> atualizarCategoria(
            @PathVariable Integer codigo,
            @RequestBody @Valid CategoriaCreateDTO categoriaCreateDTO) throws NaoEncontradoException, RegraNegocioException {
        CategoriaDTO categoriaDTO = categoriaService.Update(categoriaCreateDTO, codigo);
        return ResponseEntity.ok(categoriaDTO);
    }

    @Operation(summary = "Deletar Categoria", description = "Exclui uma Categoria existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Categoria excluída com sucesso."),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> deletarCategoria(@PathVariable Integer codigo) throws NaoEncontradoException {
        categoriaService.delete(codigo);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar categoria por Codigo", description = "Recupera uma categoria por seu Codigo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria recuperado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @GetMapping("/{codigo}")
    public ResponseEntity<CategoriaDTO> buscarCategoriaPorCodigo(@PathVariable Integer codigo) throws NaoEncontradoException {
        CategoriaDTO categoriaDTO = categoriaService.getByCodigo(codigo);
        return ResponseEntity.ok(categoriaDTO);
    }
}
