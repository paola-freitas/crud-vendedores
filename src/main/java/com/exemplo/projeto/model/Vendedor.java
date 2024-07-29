package com.exemplo.projeto.model;

import com.exemplo.projeto.enums.TipoContratacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.annotation.PostConstruct;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "vendedor")
public class Vendedor {

    private static long counter = 10000000L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String matricula;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    private LocalDate dataNascimento;

    @NotBlank(message = "Documento é obrigatório")
    private String documento;

    @NotBlank(message = "E-mail é obrigatório")
    private String email;

    @NotNull(message = "Tipo de contratação é obrigatório")
    @Enumerated(EnumType.STRING)
    private TipoContratacao tipoContratacao;

    @NotNull(message = "IdFilial é obrigatória")
    private Long idFilial;

    private String nomeFilial;

    @PostConstruct
    private void init() {
        if (this.id == null) {
            this.id = generateId();
        }
        if (this.matricula == null && this.tipoContratacao != null) {
            this.matricula = generateMatricula(this.id, this.tipoContratacao);
        }
    }

    private Long generateId() {
        return ++counter;
    }

    private String generateMatricula(Long id, TipoContratacao tipoContratacao) {
        String numericPart = String.format("%08d", id);
        String suffix = tipoContratacao.getSuffix();
        return numericPart + "-" + suffix;
    }
}
