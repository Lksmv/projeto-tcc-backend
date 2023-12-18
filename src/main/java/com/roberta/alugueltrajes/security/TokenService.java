package com.roberta.alugueltrajes.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roberta.alugueltrajes.dtos.TokenDTO;
import com.roberta.alugueltrajes.entity.UsuarioEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TokenService {

    private static final String KEY = "CARGOS";
    private final ObjectMapper objectMapper;
    @Value("${jwt.secret}")
    private String secret;

    public TokenDTO getToken(UsuarioEntity usuarioEntity) {

        LocalDateTime dataLocalDateTime = LocalDateTime.now();
        Date date = Date.from(dataLocalDateTime.atZone(ZoneId.systemDefault()).plusDays(30).toInstant());

        List<String> cargos = usuarioEntity.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String token = Jwts.builder()
                .setIssuer("pog")
                .claim(Claims.ID, usuarioEntity.getCodigo().toString())
                .claim(KEY, cargos)
                .setIssuedAt(date)
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        return new TokenDTO(token, usuarioEntity.getCodigo(), usuarioEntity.getCargoEntity().getNome(), usuarioEntity.getNome());
    }

    public UsernamePasswordAuthenticationToken isValid(String token) {
        if (token == null)
            return null;
        token = token.replace("Bearer ", "");

        Claims chaves = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

        String id = chaves.get(Claims.ID, String.class);
        List<String> cargos = chaves.get(KEY, List.class);
        List<SimpleGrantedAuthority> cargosList = cargos.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());


        return new UsernamePasswordAuthenticationToken(id,
                null, cargosList);
    }
}