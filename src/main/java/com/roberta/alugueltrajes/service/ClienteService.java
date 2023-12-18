package com.roberta.alugueltrajes.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roberta.alugueltrajes.dtos.ClienteCreateDTO;
import com.roberta.alugueltrajes.dtos.ClienteDTO;
import com.roberta.alugueltrajes.dtos.CreditoDTO;
import com.roberta.alugueltrajes.entity.ClienteEntity;
import com.roberta.alugueltrajes.exceptions.NaoEncontradoException;
import com.roberta.alugueltrajes.exceptions.RegraNegocioException;
import com.roberta.alugueltrajes.repository.ClienteRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAmount;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ClienteService {

    private final ClienteRepository repository;
    private final ObjectMapper objectMapper;

    public ClienteDTO create(ClienteCreateDTO dto) throws RegraNegocioException {
        if(dto.getTelefone().trim().replace("_", "").length() != 14) {
            throw new RegraNegocioException("Telefone inválido");
        }
        if(dto.getCodigo()<0){
            throw new RegraNegocioException("Código não pode ser negativo!");
        }
        if(dto.getCodigo()==null || dto.getCodigo()==0){
            Integer codigo = repository.getNextCodigo();
            dto.setCodigo(codigo==null?1:codigo);
        }
        if(repository.findClienteEntityByCodigo(dto.getCodigo()).isPresent())
            throw new RegraNegocioException("codigo já existe");

        ClienteEntity entity = objectMapper.convertValue(dto, ClienteEntity.class);
        entity.setCpf(entity.getCpf().replaceAll("[^0-9]", ""));
        entity.setAtivo('T');
        entity.setAluguelEntity(new HashSet<>());
        entity = repository.save(entity);

        return toDto(entity);
    }


    public ClienteDTO update(Integer codigo, ClienteCreateDTO dto) throws NaoEncontradoException, RegraNegocioException {
        if(dto.getTelefone().trim().replace("_", "").length() != 14) {
            throw new RegraNegocioException("Telefone inválido");
        }
        if(dto.getCodigo()<0){
            throw new RegraNegocioException("Código não pode ser negativo!");
        }
        if(dto.getCodigo()==null || dto.getCodigo()==0){
            Integer nextCodigo = repository.getNextCodigo();
            dto.setCodigo(nextCodigo==null?1:nextCodigo);
        }
        if(repository.findClienteEntityByCodigo(dto.getCodigo()).isPresent() && !dto.getCodigo().equals(codigo)){
            throw new RegraNegocioException("codigo já existe");
        }
        ClienteEntity entity = getEntityByCodigo(codigo);
        entity.setCodigo(dto.getCodigo());
        entity.setCpf(dto.getCpf().replaceAll("[^0-9]", ""));
        entity.setCep(dto.getCep());
        entity.setBairro(dto.getBairro());
        entity.setNome(dto.getNome());
        entity.setDataNascimento(dto.getDataNascimento());
        entity.setTelefone(dto.getTelefone());
        entity.setRedeSocial(dto.getRedeSocial());
        entity.setPessoasAutorizadas(dto.getPessoasAutorizadas());
        entity.setObservacoes(dto.getObservacoes());
        entity.setUf(dto.getUf());
        entity.setEndereco(dto.getEndereco());

        return toDto(repository.save(entity));
    }


    public void delete(Integer codigo) throws NaoEncontradoException {
        ClienteEntity entity = getEntityByCodigo(codigo);
        entity.setAtivo('F');
        repository.save(entity);
    }

    public ClienteEntity getEntityByCodigo(Integer codigo) throws NaoEncontradoException {
        return repository.findClienteEntityByCodigo(codigo)
                .orElseThrow(() -> new NaoEncontradoException("Cliente não encontrado com o Codigo: " + codigo));
    }

    public ClienteDTO getById(Integer codigo) throws NaoEncontradoException {
        return toDto(getEntityByCodigo(codigo));
    }

    public Page<ClienteDTO> findAll(Pageable pageable,String filtro) {
        Page<ClienteEntity> usuariosPage = repository.findAtivosByNomeOrCodigoOrCpf('T',filtro.toLowerCase(),pageable);
        return usuariosPage.map(this::toDto);
    }

    public ClienteDTO toDto(ClienteEntity entity) {
        ClienteDTO dto = objectMapper.convertValue(entity, ClienteDTO.class);
        dto.setCodigo(entity.getCodigo());
        String cpf = dto.getCpf();
        dto.setCpf(String.format("%s.%s.%s-%s",
                cpf.substring(0, 3),
                cpf.substring(3, 6),
                cpf.substring(6, 9),
                cpf.substring(9)));
        List<CreditoDTO> creditoDTOS = entity.getCreditoEntities().stream()
                .map(creditoEntity -> objectMapper.convertValue(creditoEntity, CreditoDTO.class))
                .collect(Collectors.toList());
        dto.setListaCreditos(creditoDTOS);
        return dto;
    }

    private boolean isNumeric(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
