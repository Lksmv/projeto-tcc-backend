package com.roberta.alugueltrajes.controller;

import com.roberta.alugueltrajes.dtos.LoginDTO;
import com.roberta.alugueltrajes.dtos.TokenDTO;
import com.roberta.alugueltrajes.entity.UsuarioEntity;
import com.roberta.alugueltrajes.exceptions.CredenciaisInvalidasException;
import com.roberta.alugueltrajes.exceptions.NaoEncontradoException;
import com.roberta.alugueltrajes.security.TokenService;
import com.roberta.alugueltrajes.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/auth")
public class LoginController {

    private final AuthenticationManager authenticationManager;

    private final UsuarioService usuarioService;
    private final TokenService tokenService;


    @Operation(summary = "Autenticar Usuário", description = "Autentica um usuário com base no login e senha fornecidos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticação bem-sucedida, retorna um token."),
            @ApiResponse(responseCode = "401", description = "Credenciais de autenticação inválidas."),
            @ApiResponse(responseCode = "403", description = "Problemas ao fazer autenticação."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @PostMapping("/login")
    public ResponseEntity<TokenDTO> auth(@RequestBody @Valid LoginDTO loginDTO) throws CredenciaisInvalidasException {
        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getLogin(),
                            loginDTO.getSenha()
                    );
            Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

            Object principal = authenticate.getPrincipal();
            UsuarioEntity pessoaEntity = (UsuarioEntity) principal;
            TokenDTO token = tokenService.getToken(pessoaEntity);
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (BadCredentialsException e) {
            throw new CredenciaisInvalidasException("Credenciais inválidas");
        }
    }
}
