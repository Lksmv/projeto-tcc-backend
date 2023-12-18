package com.roberta.alugueltrajes.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.roberta.alugueltrajes.entity.AluguelEntity;
import com.roberta.alugueltrajes.entity.CargoEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
public class UsuarioDTO {

    private Integer codigo;

    private CargoDTO cargoDTO;

    private String nome;

    private String login;

    private char ativo;
}
