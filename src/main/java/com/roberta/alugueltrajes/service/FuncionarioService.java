package com.roberta.alugueltrajes.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roberta.alugueltrajes.dtos.FuncionarioCreateDTO;
import com.roberta.alugueltrajes.dtos.FuncionarioDTO;
import com.roberta.alugueltrajes.dtos.UsuarioCreateDTO;
import com.roberta.alugueltrajes.dtos.UsuarioDTO;
import com.roberta.alugueltrajes.entity.FuncionarioEntity;
import com.roberta.alugueltrajes.entity.UsuarioEntity;
import com.roberta.alugueltrajes.exceptions.NaoEncontradoException;
import com.roberta.alugueltrajes.exceptions.RegraNegocioException;
import com.roberta.alugueltrajes.repository.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.xml.validation.Validator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FuncionarioService {

    private final FuncionarioRepository repository;
    private final ObjectMapper objectMapper;

    public FuncionarioDTO create(FuncionarioCreateDTO funcionarioCreateDTO) {

        Integer nextCodigo = repository.getNextCodigo();

        FuncionarioEntity funcionario = objectMapper.convertValue(funcionarioCreateDTO, FuncionarioEntity.class);
        funcionario.setCodigo(nextCodigo==null?1:nextCodigo);
        funcionario.setAtivo('T');
        return toDto(repository.save(funcionario));
    }

    public void delete(Integer codigo) throws NaoEncontradoException {
        FuncionarioEntity funcionario = getByCodigo(codigo);
        funcionario.setAtivo('F');
        repository.save(funcionario);
    }

    public FuncionarioDTO update(FuncionarioCreateDTO funcionarioCreateDTO, Integer codigo) throws NaoEncontradoException {

        FuncionarioEntity funcionario = getByCodigo(codigo);
        funcionario.setNome(funcionarioCreateDTO.getNome());
        return toDto(repository.save(funcionario));
    }


    public FuncionarioEntity getByCodigo(Integer codigo) throws NaoEncontradoException {
        return repository.findById(codigo)
                .orElseThrow(() -> new NaoEncontradoException("Funcionário não encontrado com o Codigo: " + codigo));
    }

    public FuncionarioDTO getDtoByCodigo(Integer codigo) throws NaoEncontradoException {
        return toDto(getByCodigo(codigo));
    }

    public Page<FuncionarioDTO> findAll(Pageable pageable, String parametro) {
        Page<FuncionarioEntity> page = repository.findAtivosByNomeOrCodigo('T',parametro.toLowerCase(),pageable);
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

    public FuncionarioDTO toDto(FuncionarioEntity funcionario) {
        return objectMapper.convertValue(funcionario, FuncionarioDTO.class);
    }
}

