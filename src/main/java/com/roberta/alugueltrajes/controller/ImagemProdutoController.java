package com.roberta.alugueltrajes.controller;

import com.roberta.alugueltrajes.dtos.ImagemProdutoDTO;
import com.roberta.alugueltrajes.exceptions.NaoEncontradoException;
import com.roberta.alugueltrajes.exceptions.RegraNegocioException;
import com.roberta.alugueltrajes.service.ImagemProdutoService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Validated
@Slf4j
@RestController
@RequestMapping("/imagens")
public class ImagemProdutoController {

    private final ImagemProdutoService imagemProdutoService;


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<ImagemProdutoDTO> uploadFile(@RequestParam("file") MultipartFile file,
                                                       @RequestParam Integer codigo) throws NaoEncontradoException, IOException {
        return ResponseEntity.ok(imagemProdutoService.inserir(codigo, file));
    }

    @GetMapping("/{imagemId}")
    public ResponseEntity<ImagemProdutoDTO> getImagem(@PathVariable Integer imagemId) throws RegraNegocioException {
        return ResponseEntity.ok(imagemProdutoService.getById(imagemId));
    }

    @DeleteMapping("/{imagemId}")
    public ResponseEntity<Void> deletePorId(@PathVariable Integer imagemId) throws RegraNegocioException {
        imagemProdutoService.deletarPorId(imagemId);
        return ResponseEntity.ok().build();
    }
}

