package com.roberta.alugueltrajes.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roberta.alugueltrajes.dtos.CargoDTO;
import com.roberta.alugueltrajes.dtos.ClienteDTO;
import com.roberta.alugueltrajes.dtos.UsuarioCreateDTO;
import com.roberta.alugueltrajes.dtos.UsuarioDTO;
import com.roberta.alugueltrajes.entity.ClienteEntity;
import com.roberta.alugueltrajes.entity.UsuarioEntity;
import com.roberta.alugueltrajes.exceptions.NaoEncontradoException;
import com.roberta.alugueltrajes.exceptions.RegraNegocioException;
import com.roberta.alugueltrajes.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final ObjectMapper objectMapper;
    private final CargoService cargoService;
    private final PasswordEncoder passwordEncoder;

    public UsuarioDTO create(UsuarioCreateDTO dto) throws NaoEncontradoException, RegraNegocioException {
        if(dto.getCodigo()<0){
            throw new RegraNegocioException("Código não pode ser negativo!");
        }
        validarCodigo(dto);
        if(repository.findByCodigo(dto.getCodigo()).isPresent())
            throw new RegraNegocioException("codigo já existe");

        if(repository.findUsuarioEntityByLogin(dto.getLogin()).isPresent())
            throw new RegraNegocioException("login já existe");

        UsuarioEntity entity = toEntity(dto);
        entity.setAtivo('T');
        entity.setSenha(passwordEncoder.encode(dto.getSenha()));
        return toDto(repository.save(entity));
    }

    public UsuarioDTO Update(UsuarioCreateDTO dto, Integer codigo) throws NaoEncontradoException, RegraNegocioException {
        if(dto.getCodigo()<0){
            throw new RegraNegocioException("Código não pode ser negativo!");
        }
        UsuarioEntity entity = getEntityByCodigo(codigo);
        validarCodigo(dto);

        if(repository.findByCodigo(dto.getCodigo()).isPresent() && !dto.getCodigo().equals(codigo))
            throw new RegraNegocioException("codigo já existe");

        if(repository.findUsuarioEntityByLogin(dto.getLogin()).isPresent() && !dto.getLogin().equals(entity.getLogin()))
            throw new RegraNegocioException("login já existe");

        entity.setCodigo(dto.getCodigo());
        entity.setNome(dto.getNome());
        entity.setLogin(dto.getLogin());
        if(!dto.getSenha().isEmpty()){
            entity.setSenha(passwordEncoder.encode(dto.getSenha()));
        }
        entity.setCargoEntity(cargoService.getById(dto.getIdCargo()));
        return toDto(repository.save(entity));
    }

    private void validarCodigo(UsuarioCreateDTO dto){
        if(dto.getCodigo()==null || dto.getCodigo()==0){
            Integer nextCodigo = repository.getNextCodigo();
            dto.setCodigo(nextCodigo==null?1:nextCodigo);
        }
    }

    public void delete(Integer codigo) throws NaoEncontradoException {
        UsuarioEntity entity = getEntityByCodigo(codigo);
        repository.delete(entity);
    }

    public UsuarioEntity getEntityByCodigo(Integer codigo) throws NaoEncontradoException {
        return repository.findByCodigo(codigo)
                .orElseThrow(() -> new NaoEncontradoException("Usuário não encontrado com o Codigo: " + codigo));
    }

    public UsuarioDTO getByCodigo(Integer codigo) throws NaoEncontradoException {
        return toDto(getEntityByCodigo(codigo));
    }

    public Page<UsuarioDTO> findAll(Pageable pageable, String parametro) {
        Page<UsuarioEntity> usuariosPage = repository.findAtivosByNomeOrCodigo('T',parametro.toLowerCase(),pageable);
        return usuariosPage.map(this::toDto);
    }

    private boolean isNumeric(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public UsuarioEntity findByLogin(String login) throws NaoEncontradoException {
        return repository.findUsuarioEntityByLogin(login)
                .orElseThrow(() -> new NaoEncontradoException("Usuário não encontrado"));
    }


    public UsuarioDTO toDto(UsuarioEntity entity) {
        UsuarioDTO dto = objectMapper.convertValue(entity, UsuarioDTO.class);
        dto.setCargoDTO(objectMapper.convertValue(entity.getCargoEntity(), CargoDTO.class));
        return dto;
    }

    private UsuarioEntity toEntity(UsuarioCreateDTO createDTO) throws NaoEncontradoException {
        UsuarioEntity entity = objectMapper.convertValue(createDTO, UsuarioEntity.class);
        entity.setCargoEntity(cargoService.getById(createDTO.getIdCargo()));
        return entity;
    }

    private UsuarioEntity toEntity(UsuarioDTO usuarioDTO) throws NaoEncontradoException {
        UsuarioEntity entity = objectMapper.convertValue(usuarioDTO, UsuarioEntity.class);
        entity.setCargoEntity(cargoService.getById(usuarioDTO.getCargoDTO().getIdCargo()));
        return entity;
    }
}
