package com.roberta.alugueltrajes.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roberta.alugueltrajes.dtos.*;
import com.roberta.alugueltrajes.entity.CorEntity;
import com.roberta.alugueltrajes.entity.ImagemProdutoEntity;
import com.roberta.alugueltrajes.entity.ProdutoEntity;
import com.roberta.alugueltrajes.entity.UsuarioEntity;
import com.roberta.alugueltrajes.enums.Genero;
import com.roberta.alugueltrajes.enums.StatusProduto;
import com.roberta.alugueltrajes.exceptions.NaoEncontradoException;
import com.roberta.alugueltrajes.exceptions.RegraNegocioException;
import com.roberta.alugueltrajes.repository.ImagemProdutoRepository;
import com.roberta.alugueltrajes.repository.ProdutoRepository;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProdutoService {

    private final ProdutoRepository repository;
    private final ObjectMapper objectMapper;
    private final CategoriaService categoriaService;
    private final CorService corService;
    private final ImagemProdutoRepository imagemProdutoRepository;
    private final UploadArquivosService uploadArquivosService;

    public ProdutoDTO create(ProdutoCreateDTO dto) throws IOException, NaoEncontradoException, RegraNegocioException {
        if(dto.getCodigo()<0){
            throw new RegraNegocioException("Código não pode ser negativo!");
        }
        if (dto.getCodigo() == null || dto.getCodigo() == 0) {
            Integer nextCodigo = repository.getNextCodigo();
            dto.setCodigo(nextCodigo == null ? 1 : nextCodigo);
        }
        if (repository.findByCodigo(dto.getCodigo()).isPresent())
            throw new RegraNegocioException("Código já existe");

        CorEntity cor = new CorEntity();
        if (!corService.findCor(dto.getCor()).isPresent()) {
            cor.setNome(dto.getCor());
            cor = corService.saveCor(cor);
        }else{
            cor = corService.findCor(dto.getCor()).get();
        }

        ProdutoEntity entity = objectMapper.convertValue(dto, ProdutoEntity.class);
        entity.setAtivo('T');
        entity.setQuantidade(0);
        entity.setTrajeVendido('F');
        entity.setStatusProduto(StatusProduto.DISPONIVEL);
        entity.setImagens(new ArrayList<>());
        entity.setCategoriaEntity(categoriaService.getEntityByCodigo(dto.getCodigoCategoria()));
        entity.setCorEntity(cor);
        return toDto(repository.save(entity));
    }


    public ProdutoDTO update(Integer codigo, ProdutoUpdateDTO dto) throws IOException, NaoEncontradoException, RegraNegocioException {
        ProdutoEntity produto = getEntityByCodigo(codigo);
        if (dto.getCodigo() == null || dto.getCodigo() == 0) {
            Integer nextCodigo = repository.getNextCodigo();
            dto.setCodigo(nextCodigo == null ? 1 : nextCodigo);
        }
        if(dto.getCodigo()<0){
            throw new RegraNegocioException("Código não pode ser negativo!");
        }
        if (repository.findByCodigo(dto.getCodigo()).isPresent() && !dto.getCodigo().equals(codigo))
            throw new RegraNegocioException("Código já existe");

        CorEntity cor = new CorEntity();
        if (!corService.findCor(dto.getCor()).isPresent()) {
            cor.setNome(dto.getCor());
            cor = corService.saveCor(cor);
        }else{
            cor = corService.findCor(dto.getCor()).get();
        }

        produto.setCodigo(dto.getCodigo());
        produto.setNome(dto.getNome());
        produto.setObservacoes(dto.getObservacoes());
        produto.setCategoriaEntity(categoriaService.getEntityByCodigo(dto.getCodigoCategoria()));
        produto.setGenero(dto.getGenero());
        produto.setCorEntity(cor); // Define a nova cor do produto
        produto.setTamanho(dto.getTamanho());
        produto.setValor(dto.getValor());
        produto.setStatusProduto(dto.getStatusProduto());
        produto.setTrajeVendido(dto.getTrajeVendido());
        return toDto(repository.save(produto));
    }



    public void delete(Integer codigo) throws NaoEncontradoException {
        ProdutoEntity entity = getEntityByCodigo(codigo);
        entity.setAtivo('F');
        for (ImagemProdutoEntity imagem : entity.getImagens()) {
            uploadArquivosService.deletar(imagem.getCaminhoImagem());
            imagemProdutoRepository.delete(imagem);
        }
        repository.save(entity);
    }

    public ProdutoEntity getEntityByCodigo(Integer codigo) throws NaoEncontradoException {
        return repository.findByCodigo(codigo)
                .orElseThrow(() -> new NaoEncontradoException("Produto não encontrado com o Codigo: " + codigo));
    }

    public ProdutoDTO getByCodigo(Integer codigo) throws NaoEncontradoException {
        return toDto(getEntityByCodigo(codigo));
    }

    public void adicionarQuantidadeAlugado(Integer codigo) throws NaoEncontradoException {
        ProdutoEntity produto = getEntityByCodigo(codigo);
        produto.setQuantidade(produto.getQuantidade() + 1);
    }

    public Page<ProdutoDTO> findAll(Pageable pageable, String parametro) {
        Page<ProdutoEntity> page = repository.findAtivosByNomeOrCodigo('T',parametro.toLowerCase(),pageable);
        return page.map(this::toDto);
    }

    public Page<ProdutoDTO> findAllComImagem(Pageable pageable) {
        Page<ProdutoEntity> page = repository.findProdutoEntitiesByImagensIsNotEmpty(pageable);
        return page.map(this::toDto);
    }
    public Page<ProdutoDTO> findAllComCategoriaCorGenero(Integer codigo, String cor, Genero genero,String tamanho,Pageable pageable) {
        Page<ProdutoEntity> page = repository.findProdutosFiltradosComImagens(codigo,cor,genero,tamanho,pageable);
        return page.map(this::toDto);
    }

    private boolean isNumeric(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    public ProdutoDTO toDto(ProdutoEntity entity) {
        ProdutoDTO dto = objectMapper.convertValue(entity, ProdutoDTO.class);
        dto.setCor(entity.getCorEntity().getNome());
        dto.setCategoria(categoriaService.toDto(entity.getCategoriaEntity()));
        if (entity.getImagens() != null) {
            List<ImagemProdutoDTO> imagemProdutoDTOList = entity.getImagens().stream()
                    .map(imagemProdutoEntity -> objectMapper.convertValue(imagemProdutoEntity,ImagemProdutoDTO.class))
                    .collect(Collectors.toList());
            dto.setImagens(imagemProdutoDTOList);
        }
        return dto;
    }

    public Integer getQuantidadeProdutosPorStatus(StatusProduto statusProduto){
        return repository.countByStatusProduto(statusProduto);
    }

}
