package com.exemplo.projeto.dto;

import com.exemplo.projeto.enums.TipoContratacao;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Getter
@Setter
public class VendedorDto {
    @Pattern(regexp = "^\\d+-OUT|\\d+-CLT|\\d+-PJ$", message = "Matrícula deve terminar com '-OUT', '-CLT' ou '-PJ'")
    private String matricula;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    private LocalDate dataNascimento;

    @NotBlank(message = "Documento é obrigatório")
    private String documento;

    @NotBlank(message = "Tipo de contratação é obrigatório")
    private TipoContratacao tipoContratacao;

}
