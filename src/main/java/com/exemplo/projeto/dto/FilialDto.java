package com.exemplo.projeto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilialDto {
    private Long id;

    private String nome;

    @Pattern(regexp = "\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}", message = "Formato de CNPJ inválido")
    private String cnpj;

    private String cidade;

    @Pattern(regexp = "[A-Z]{2}", message = "Formato de UF inválido")
    private String uf;

    private String tipo;

    private boolean ativo;

    private LocalDate dataCadastro;

    private LocalDate ultimaAtualizacao;

}
