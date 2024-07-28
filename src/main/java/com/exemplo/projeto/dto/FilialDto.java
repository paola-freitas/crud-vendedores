package com.exemplo.projeto.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class FilialDto {
    private String id;

    @NotBlank(message = "O nome da filial é obrigatório")
    private String nome;

    @NotBlank(message = "O CNPJ é obrigatório")
    @Pattern(regexp = "\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}", message = "Formato de CNPJ inválido")
    private String cnpj;

    @NotBlank(message = "A cidade é obrigatória")
    private String cidade;

    @NotBlank(message = "O estado (UF) é obrigatório")
    @Pattern(regexp = "[A-Z]{2}", message = "Formato de UF inválido")
    private String uf;

    private String tipo;

    private boolean ativo;
}
