package com.roberta.alugueltrajes.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roberta.alugueltrajes.dtos.CreditoCreateDTO;
import com.roberta.alugueltrajes.dtos.CreditoDTO;
import com.roberta.alugueltrajes.entity.CreditoEntity;
import com.roberta.alugueltrajes.exceptions.NaoEncontradoException;
import com.roberta.alugueltrajes.repository.AluguelRepository;
import com.roberta.alugueltrajes.repository.ClienteRepository;
import com.roberta.alugueltrajes.repository.CreditoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CreditoService {

    private final CreditoRepository repository;
    private final ObjectMapper objectMapper;
    private final ClienteService clienteService;

    public CreditoDTO create(CreditoCreateDTO creditoCreateDTO) throws NaoEncontradoException {
        CreditoEntity credito = objectMapper.convertValue(creditoCreateDTO, CreditoEntity.class);
        credito.setClienteEntity(clienteService.getEntityByCodigo(creditoCreateDTO.getCodigoCliente()));
        return toDto(repository.save(credito));
    }

    public void delete(Integer idCredito) throws NaoEncontradoException {
        CreditoEntity credito = getById(idCredito);
        repository.delete(credito);
    }

    public CreditoDTO update(CreditoCreateDTO creditoCreateDTO, Integer idCredito) throws NaoEncontradoException {
        getById(idCredito);
        CreditoEntity credito = objectMapper.convertValue(creditoCreateDTO, CreditoEntity.class);
        credito.setId_credito(idCredito);
        credito.setClienteEntity(clienteService.getEntityByCodigo(creditoCreateDTO.getCodigoCliente()));
        return toDto(repository.save(credito));
    }

    public CreditoEntity getById(Integer id) throws NaoEncontradoException {
        return repository.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Crédito não encontrado com o ID: " + id));
    }

    public CreditoDTO getDtoById(Integer id) throws NaoEncontradoException {
        return toDto(getById(id));
    }

    public Page<CreditoDTO> getAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(this::toDto);
    }

    public CreditoDTO toDto(CreditoEntity credito) {
        return objectMapper.convertValue(credito, CreditoDTO.class);
    }

}
