package com.roberta.alugueltrajes.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roberta.alugueltrajes.dtos.CategoriaCreateDTO;
import com.roberta.alugueltrajes.dtos.CategoriaDTO;
import com.roberta.alugueltrajes.dtos.UsuarioCreateDTO;
import com.roberta.alugueltrajes.dtos.UsuarioDTO;
import com.roberta.alugueltrajes.entity.CategoriaEntity;
import com.roberta.alugueltrajes.entity.UsuarioEntity;
import com.roberta.alugueltrajes.exceptions.NaoEncontradoException;
import com.roberta.alugueltrajes.exceptions.RegraNegocioException;
import com.roberta.alugueltrajes.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CategoriaService {

    private final CategoriaRepository repository;
    private final ObjectMapper objectMapper;

    public CategoriaDTO create(CategoriaCreateDTO categoriaCreateDTO) throws RegraNegocioException {
        if(categoriaCreateDTO.getCodigo()<0){
            throw new RegraNegocioException("Código não pode ser negativo!");
        }
        validarCodigo(categoriaCreateDTO);

        if(repository.findByCodigo(categoriaCreateDTO.getCodigo()).isPresent())
            throw new RegraNegocioException("codigo já existe");

        CategoriaEntity categoria = objectMapper.convertValue(categoriaCreateDTO, CategoriaEntity.class);
        categoria.setAtivo('T');
        return toDto(repository.save(categoria));
    }

    public void delete(Integer codigo) throws NaoEncontradoException {
        CategoriaEntity categoria = getEntityByCodigo(codigo);
        categoria.setAtivo('F');
        repository.save(categoria);
    }

    public CategoriaDTO Update(CategoriaCreateDTO dto, Integer codigo) throws NaoEncontradoException, RegraNegocioException {
        if(dto.getCodigo()<0){
            throw new RegraNegocioException("Código não pode ser negativo!");
        }
        validarCodigo(dto);
        CategoriaEntity categoria = getEntityByCodigo(codigo);
        if(repository.findByCodigo(dto.getCodigo()).isPresent()&& !dto.getCodigo().equals(codigo))
            throw new RegraNegocioException("codigo já existe");
        categoria.setCodigo(dto.getCodigo());
        categoria.setNome(dto.getNome());

        return toDto(repository.save(categoria));
    }

    private void validarCodigo(CategoriaCreateDTO dto){
        if(dto.getCodigo()==null || dto.getCodigo()==0){
            Integer nextCodigo = repository.getNextCodigo();
            dto.setCodigo(nextCodigo==null?1:nextCodigo);
        }
    }

    public CategoriaEntity getEntityByCodigo(Integer codigo) throws NaoEncontradoException {
        return repository.findByCodigo(codigo)
                .orElseThrow(() -> new NaoEncontradoException("Categoria não encontrado com o Codigo: " + codigo));
    }

    public CategoriaDTO getByCodigo(Integer codigo) throws NaoEncontradoException {
        return toDto(getEntityByCodigo(codigo));
    }

    public Page<CategoriaDTO> findAll(Pageable pageable, String parametro) {
        Page<CategoriaEntity> page = repository.findAtivosByNomeOrCodigo('T',parametro.toLowerCase(),pageable);
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

    public CategoriaDTO toDto(CategoriaEntity categoria) {
        return objectMapper.convertValue(categoria, CategoriaDTO.class);
    }

}
