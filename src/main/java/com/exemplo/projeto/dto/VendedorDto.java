package com.exemplo.projeto.dto;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VendedorDto {
    @Pattern(regexp = "^\\d+-OUT|\\d+-CLT|\\d+-PJ$", message = "Matrícula deve terminar com '-OUT', '-CLT' ou '-PJ'")
    private String matricula;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @Past(message = "Data de nascimento deve ser uma data passada")
    private LocalDate dataNascimento;

    @NotBlank(message = "Documento é obrigatório")
    private String documento;

    @NotBlank(message = "E-mail é obrigatório")
    private String email;

    @NotBlank(message = "Tipo de contratação é obrigatório")
    @Pattern(regexp = "CLT|PJ|OUT", message = "Tipo de contratação deve ser 'CLT', 'PJ' ou 'OUT'")
    private String tipoContratacao;

    private Long idFilial;

    @NotBlank(message = "Filial é obrigatória")
    private String nomeFilial;

}