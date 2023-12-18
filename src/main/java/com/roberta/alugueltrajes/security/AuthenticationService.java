package com.roberta.alugueltrajes.security;

import com.roberta.alugueltrajes.entity.UsuarioEntity;
import com.roberta.alugueltrajes.exceptions.NaoEncontradoException;
import com.roberta.alugueltrajes.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {

    private final UsuarioService usuarioService;

    @Override
    public UserDetails loadUserByUsername(String loginUsername) throws UsernameNotFoundException {
        try {
            UsuarioEntity user = usuarioService.findByLogin(loginUsername);
            return user;
        } catch (NaoEncontradoException e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }
}