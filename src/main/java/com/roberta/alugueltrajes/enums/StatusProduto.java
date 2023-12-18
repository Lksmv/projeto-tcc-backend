package com.roberta.alugueltrajes.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum StatusProduto {
    DISPONIVEL(1),
    INDISPONIVEL(2),
    AGUARDANDO_RETIRADA(3),
    ALUGADO(4),
    MANUTENCAO(5);

    private final Integer id;

    StatusProduto(Integer id){
        this.id = id;
    }

    public static StatusProduto porId(Integer id){
        return Arrays.stream(StatusProduto.values())
                .filter(ids -> ids.getId().equals(id))
                .findFirst()
                .get();
    }
}
