package com.roberta.alugueltrajes.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum StatusAluguelProduto {
    DEVOLVIDO(1),
    ALUGADO(2),
    CANCELADO(3);

    private final Integer id;

    StatusAluguelProduto(Integer id){
        this.id = id;
    }

    public static StatusAluguelProduto porId(Integer id){
        return Arrays.stream(StatusAluguelProduto.values())
                .filter(ids -> ids.getId().equals(id))
                .findFirst()
                .get();
    }
}
