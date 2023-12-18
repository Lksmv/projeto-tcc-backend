package com.roberta.alugueltrajes.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum StatusAluguel {
    ABERTO(1),
    FECHADO(2),
    CANCELADO(3);

    private final Integer id;

    StatusAluguel(Integer id){
        this.id = id;
    }

    public static StatusAluguel porId(Integer id){
        return Arrays.stream(StatusAluguel.values())
                .filter(ids -> ids.getId().equals(id))
                .findFirst()
                .get();
    }
}
