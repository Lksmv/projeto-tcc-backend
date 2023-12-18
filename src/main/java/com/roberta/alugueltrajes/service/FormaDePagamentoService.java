package com.roberta.alugueltrajes.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roberta.alugueltrajes.dtos.FormaDePagamentoCreateDTO;
import com.roberta.alugueltrajes.dtos.FormaDePagamentoDTO;
import com.roberta.alugueltrajes.dtos.UsuarioDTO;
import com.roberta.alugueltrajes.entity.FormaDePagamentoEntity;
import com.roberta.alugueltrajes.entity.UsuarioEntity;
import com.roberta.alugueltrajes.exceptions.NaoEncontradoException;
import com.roberta.alugueltrajes.exceptions.RegraNegocioException;
import com.roberta.alugueltrajes.repository.FormaDePagamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FormaDePagamentoService {

    private final FormaDePagamentoRepository repository;
    private final ObjectMapper objectMapper;

    public FormaDePagamentoDTO create(FormaDePagamentoCreateDTO formaDePagamentoCreateDTO) throws RegraNegocioException {

        if(formaDePagamentoCreateDTO.getTaxa()<0){
            throw new RegraNegocioException("Taxa não pode ser negativa!");
        }
        if(formaDePagamentoCreateDTO.getNome().trim().length()< 3){
            throw new RegraNegocioException(("Forma de pagamento não pode ser menor que 3."));
        }
        FormaDePagamentoEntity formaDePagamento = objectMapper.convertValue(formaDePagamentoCreateDTO, FormaDePagamentoEntity.class);
        formaDePagamento.setAtivo('T');
        return toDto(repository.save(formaDePagamento));
    }

    public void delete(Integer idFormaDePagamento) throws NaoEncontradoException {
        FormaDePagamentoEntity formaDePagamento = getById(idFormaDePagamento);
        formaDePagamento.setAtivo('F');
        repository.save(formaDePagamento);
    }

    public FormaDePagamentoDTO update(FormaDePagamentoCreateDTO formaDePagamentoCreateDTO, Integer idFormaDePagamento) throws NaoEncontradoException, RegraNegocioException {
        if(formaDePagamentoCreateDTO.getTaxa()<0){
            throw new RegraNegocioException("Taxa não pode ser negativa!");
        }
        if(formaDePagamentoCreateDTO.getNome().trim().length()< 3){
            throw new RegraNegocioException(("Nome forma de pagamento não pode ser menor que 3."));
        }
        FormaDePagamentoEntity formaDePagamento = getById(idFormaDePagamento);
        formaDePagamento.setNome(formaDePagamentoCreateDTO.getNome());
        formaDePagamento.setTaxa(formaDePagamentoCreateDTO.getTaxa());
        return toDto(repository.save(formaDePagamento));
    }

    public FormaDePagamentoEntity getById(Integer id) throws NaoEncontradoException {
        return repository.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Forma de pagamento não encontrada com o ID: " + id));
    }

    public FormaDePagamentoDTO getDtoById(Integer id) throws NaoEncontradoException {
        return toDto(getById(id));
    }

    public Page<FormaDePagamentoDTO> findAll(Pageable pageable, String parametro) {
        if(parametro==null || parametro.isEmpty()) parametro = "";
        Page<FormaDePagamentoEntity> page = repository.findAtivosByNome('T',parametro,pageable);
        return page.map(this::toDto);
    }


    public FormaDePagamentoDTO toDto(FormaDePagamentoEntity formaDePagamento) {
        return objectMapper.convertValue(formaDePagamento, FormaDePagamentoDTO.class);
    }
}

