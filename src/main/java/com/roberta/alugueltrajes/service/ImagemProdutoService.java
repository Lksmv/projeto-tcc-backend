package com.roberta.alugueltrajes.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roberta.alugueltrajes.dtos.ImagemProdutoDTO;
import com.roberta.alugueltrajes.entity.ImagemProdutoEntity;
import com.roberta.alugueltrajes.entity.ProdutoEntity;
import com.roberta.alugueltrajes.exceptions.NaoEncontradoException;
import com.roberta.alugueltrajes.exceptions.RegraNegocioException;
import com.roberta.alugueltrajes.repository.ImagemProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ImagemProdutoService {

    private final ImagemProdutoRepository imagemProdutoRepository;
    private final ProdutoService produtoService;
    private final ObjectMapper objectMapper;
    private final UploadArquivosService uploadArquivosService;


    public ImagemProdutoDTO inserir(Integer codigo, MultipartFile file) throws NaoEncontradoException, IOException {
        ProdutoEntity produto = produtoService.getEntityByCodigo(codigo);
        ImagemProdutoEntity imagemProdutoEntity = new ImagemProdutoEntity();


        String caminhoImagem = uploadArquivosService.inserir(file);

        imagemProdutoEntity.setCaminhoImagem(caminhoImagem);
        imagemProdutoEntity.setProduto(produto);

        ImagemProdutoEntity img = imagemProdutoRepository.save(imagemProdutoEntity);
        return objectMapper.convertValue(img,ImagemProdutoDTO.class);
    }

    public ImagemProdutoDTO getById(Integer imagemId) throws RegraNegocioException {
        ImagemProdutoEntity imagemProdutoEntity = imagemProdutoRepository.findById(imagemId)
                .orElseThrow(() -> new RegraNegocioException("Imagem não encontrada"));
        return objectMapper.convertValue(imagemProdutoEntity,ImagemProdutoDTO.class);
    }

    public void deletarPorId(Integer imagemId) throws RegraNegocioException {
        ImagemProdutoEntity imagemProdutoEntity = imagemProdutoRepository.findById(imagemId)
                .orElseThrow(() -> new RegraNegocioException("Imagem não encontrada"));
        uploadArquivosService.deletar(imagemProdutoEntity.getCaminhoImagem());
        imagemProdutoRepository.delete(imagemProdutoEntity);
    }

}
