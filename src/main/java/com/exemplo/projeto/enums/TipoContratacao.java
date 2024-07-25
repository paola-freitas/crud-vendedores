package com.exemplo.projeto.enums;

public enum TipoContratacao {
    OUTSOURCING("Outsourcing", "OUT"),
    CLT("CLT", "CLT"),
    PESSOA_JURIDICA("Pessoa Jurídica", "PJ");

    private final String descricao;
    private final String suffix;

    TipoContratacao(String descricao, String suffix) {
        this.descricao = descricao;
        this.suffix = suffix;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getSuffix() {
        return suffix;
    }
}
