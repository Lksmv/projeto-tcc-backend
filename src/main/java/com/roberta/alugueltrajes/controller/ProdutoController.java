package com.roberta.alugueltrajes.controller;

import com.roberta.alugueltrajes.dtos.ProdutoCreateDTO;
import com.roberta.alugueltrajes.dtos.ProdutoDTO;
import com.roberta.alugueltrajes.dtos.ProdutoUpdateDTO;
import com.roberta.alugueltrajes.dtos.UsuarioDTO;
import com.roberta.alugueltrajes.enums.Genero;
import com.roberta.alugueltrajes.enums.StatusProduto;
import com.roberta.alugueltrajes.exceptions.NaoEncontradoException;
import com.roberta.alugueltrajes.exceptions.RegraNegocioException;
import com.roberta.alugueltrajes.service.ProdutoService;
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
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Validated
@Slf4j
@RestController
@RequestMapping("/produto")
public class ProdutoController {

    private final ProdutoService produtoService;

    @Operation(summary = "Criar Produto", description = "Cria um novo registro de produto.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados de entrada."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @PostMapping
    public ResponseEntity<ProdutoDTO> criarProduto(@RequestBody @Valid ProdutoCreateDTO produtoCreateDTO) throws IOException, NaoEncontradoException, RegraNegocioException {
        return new ResponseEntity<>(produtoService.create(produtoCreateDTO), HttpStatus.OK);
    }

    @Operation(summary = "Listar Produtos", description = "Retorna uma lista paginada de produtos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de produtos recuperada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @GetMapping
    public Page<ProdutoDTO> list(@RequestParam("page") int page, @RequestParam("size") int size, @RequestParam(value = "filtro", required = false) String filtro) {
        Pageable pageable = PageRequest.of(page, size);
        return produtoService.findAll(pageable, filtro);
    }

    @Operation(summary = "Listar Produtos com imagem", description = "Retorna uma lista paginada de produtos com filtro por categoria, cor e gênero.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de produtos recuperada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @GetMapping("/com-imagem-filtro")
    public Page<ProdutoDTO> listComImagemFiltrada(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam(value = "categoria", required = false) Integer categoria,
            @RequestParam(value = "cor", required = false) String cor,
            @RequestParam(value = "genero", required = false) Genero genero,
            @RequestParam(value = "tamanho", required = false) String tamanho
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return produtoService.findAllComCategoriaCorGenero(categoria, cor, genero, tamanho,pageable);
    }
    @Operation(summary = "Listar Produtos com imagem", description = "Retorna uma lista paginada de produtos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de produtos recuperada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @GetMapping("/com-imagem")
    public Page<ProdutoDTO> listComImagem(@RequestParam("page") int page, @RequestParam("size") int size, @RequestParam(value = "filtro", required = false) String filtro) {
        Pageable pageable = PageRequest.of(page, size);
        return produtoService.findAllComImagem(pageable);
    }

    @Operation(summary = "Atualizar Produto", description = "Atualiza os detalhes de um produto existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados de entrada."),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @PutMapping("/{codigo}")
    public ResponseEntity<ProdutoDTO> atualizarProduto(
            @PathVariable Integer codigo,
            @RequestBody @Valid ProdutoUpdateDTO produtoUpdateDTO) throws NaoEncontradoException, IOException, RegraNegocioException {
        ProdutoDTO produtoDTO = produtoService.update(codigo, produtoUpdateDTO);
        return ResponseEntity.ok(produtoDTO);
    }

    @Operation(summary = "Deletar Produto", description = "Exclui um produto existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Produto excluído com sucesso."),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> deletarProduto(@PathVariable Integer codigo) throws NaoEncontradoException {
        produtoService.delete(codigo);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar Produto por Codigo", description = "Recupera um produto por seu Codigo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto recuperado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @GetMapping("/{codigo}")
    public ResponseEntity<ProdutoDTO> buscarProdutoPorId(@PathVariable Integer codigo) throws NaoEncontradoException {
        ProdutoDTO produtoDTO = produtoService.getByCodigo(codigo);
        return ResponseEntity.ok(produtoDTO);
    }

    @GetMapping("/quantidade-alugados")
    public Integer getQuantidadeAlugados(){
        return produtoService.getQuantidadeProdutosPorStatus(StatusProduto.ALUGADO);
    }

    @GetMapping("/quantidade-aguardando-retirada")
    public Integer getQuantidadeAguardando(){
        return produtoService.getQuantidadeProdutosPorStatus(StatusProduto.AGUARDANDO_RETIRADA);
    }



}
