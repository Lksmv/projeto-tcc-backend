package com.roberta.alugueltrajes.service;

import com.roberta.alugueltrajes.dtos.UsuarioCreateDTO;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Transactional
public class InitialDataLoader implements CommandLineRunner {

    private final UsuarioService userService;

    public InitialDataLoader(UsuarioService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userService.findAll(PageRequest.of(0, 10),"").getTotalElements() == 0) {
            UsuarioCreateDTO usuarioCreateDTO = new UsuarioCreateDTO();
            usuarioCreateDTO.setNome("admin");
            usuarioCreateDTO.setCodigo(0);
            usuarioCreateDTO.setSenha("admin123");
            usuarioCreateDTO.setIdCargo(1);
            usuarioCreateDTO.setLogin("admin");
            userService.create(usuarioCreateDTO);
        }
    }
}
