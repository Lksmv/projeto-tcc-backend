package com.roberta.alugueltrajes.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "cargo")
public class CargoEntity implements GrantedAuthority {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cargo_id_cargo_seq")
    @SequenceGenerator(name = "cargo_id_cargo_seq", sequenceName = "cargo_id_cargo_seq", allocationSize = 1)
    @Column(name = "id_cargo")
    @Id
    private Integer idCargo;

    @Column(name = "nome")
    private String nome;

    @JsonIgnore
    @OneToMany(mappedBy = "cargoEntity", fetch = FetchType.LAZY)
    private Set<UsuarioEntity> usuario;

    @Override
    public String getAuthority() {
        return nome;
    }
}