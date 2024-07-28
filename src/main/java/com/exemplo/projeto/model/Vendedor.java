package com.exemplo.projeto.model;

import com.exemplo.projeto.enums.TipoContratacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "vendedor")
public class Vendedor {

    private static long counter = 10000000L;

    @Id
    private Long id;

    private String matricula;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;


    private LocalDate dataNascimento;

    @NotBlank(message = "Documento é obrigatório")
    private String documento;

    @NotBlank(message = "E-mail é obrigatório")
    private String email;

    @NotBlank(message = "Tipo de contratação é obrigatório")
    private TipoContratacao tipoContratacao;

    private Long idFilial;

    @NotBlank(message = "Filial é obrigatória")
    private String nomeFilial;

    // Matrícula é um campo calculado, não persistente
    /*public String getMatricula() {
        if (id != null && tipoContratacao != null) {
            return id + "-" + tipoContratacao.getSuffix();
        }
        return null;
    }*/

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
