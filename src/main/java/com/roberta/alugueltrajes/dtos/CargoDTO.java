package com.roberta.alugueltrajes.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.roberta.alugueltrajes.entity.UsuarioEntity;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.Data;

import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.Set;

@Data
public class CargoDTO {

    private Integer idCargo;
    private String nome;
}
