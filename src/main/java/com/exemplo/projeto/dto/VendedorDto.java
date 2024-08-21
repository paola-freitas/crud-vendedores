package com.exemplo.projeto.dto;

import com.exemplo.projeto.enums.TipoContratacao;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VendedorDto {
    @Pattern(regexp = "^\\d+-OUT|\\d+-CLT|\\d+-PJ$", message = "Matrícula deve terminar com '-OUT', '-CLT' ou '-PJ'")
    @NotBlank(message = "Matrícula é obrigatório")
    private String matricula;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    private LocalDate dataNascimento;

    @NotBlank(message = "Documento é obrigatório")
    private String documento;

    @NotBlank(message = "E-mail é obrigatório")
    private String email;

    @NotNull(message = "Tipo de contratação é obrigatório")
    private String tipoContratacao;

    @NotNull(message = "Filial é obrigatória")
    private FilialDto filial;

}