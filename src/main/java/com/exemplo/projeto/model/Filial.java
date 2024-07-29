package com.exemplo.projeto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "filial")
public class Filial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome da filial é obrigatório")
    private String nome;

    @NotBlank(message = "O CNPJ é obrigatório")
    @Pattern(regexp = "\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}", message = "Formato de CNPJ inválido")
    private String cnpj;

    private String cidade;

    @Pattern(regexp = "[A-Z]{2}", message = "Formato de UF inválido")
    private String uf;

    private String tipo;

    private boolean ativo;

    @Column(name = "data_cadastro")
    private LocalDate dataCadastro;

    @Column(name = "ultima_atualizacao")
    private LocalDate ultimaAtualizacao;
}
