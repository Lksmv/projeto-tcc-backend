package com.roberta.alugueltrajes.enums;

import java.util.Arrays;

public enum Genero {
    M("MASCULINO"),
    F("FEMININO"),
    U("UNISEX");

    private final String tag;

    Genero(String tag) {
        this.tag = tag;
    }
}
