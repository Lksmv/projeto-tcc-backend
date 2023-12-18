package com.roberta.alugueltrajes.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "usuario")
public class UsuarioEntity implements UserDetails {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_id_usuario_seq")
    @SequenceGenerator(name = "usuario_id_usuario_seq", sequenceName = "usuario_id_usuario_seq", allocationSize = 1)
    @Column(name = "id_usuario")
    @Id
    private Integer idUsuario;

    @Column(name = "codigo", unique = true)
    private Integer codigo;

    @Column(name = "nome")
    private String nome;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CARGO")
    private CargoEntity cargoEntity;

    @Column(name = "login")
    private String login;

    @Column(name = "senha")
    private String senha;

    @Column(name = "ativo")
    private char ativo;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<CargoEntity> lista = new ArrayList();
        lista.add(cargoEntity);
        return lista;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
