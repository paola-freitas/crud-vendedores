package com.exemplo.projeto.enums;

import lombok.Getter;

@Getter
public enum TipoContratacao {
    OUTSOURCING("Outsourcing", "OUT"),
    CLT("CLT", "CLT"),
    PESSOA_JURIDICA("Pessoa Jurídica", "PJ");

    private final String descriptor;
    private final String suffix;

    TipoContratacao(String descriptor, String suffix) {
        this.descriptor = descriptor;
        this.suffix = suffix;
    }

}
