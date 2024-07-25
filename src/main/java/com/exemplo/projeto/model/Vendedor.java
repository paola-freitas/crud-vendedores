package com.exemplo.projeto.model;

import com.exemplo.projeto.enums.TipoContratacao;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.NotBlank;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Document(collection = "vendedor")
public class Vendedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    private String matricula;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    private LocalDate dataNascimento;

    @NotBlank(message = "Documento é obrigatório")
    private String documento;

    @NotBlank(message = "Tipo de contratação é obrigatório")
    @Enumerated(EnumType.STRING)
    private TipoContratacao tipoContratacao;

    // Se precisar de lógica customizada, ainda é possível definir métodos personalizados
    public void setId(Long id) {
        this.id = id;
        setMatriculaBasedOnIdAndTipoContratacao();
    }

    public void setTipoContratacao(TipoContratacao tipoContratacao) {
        this.tipoContratacao = tipoContratacao;
        setMatriculaBasedOnIdAndTipoContratacao();
    }

    private void setMatriculaBasedOnIdAndTipoContratacao() {
        if (this.id != null && this.tipoContratacao != null) {
            this.matricula = this.id + "-" + this.tipoContratacao.getSuffix();
        }
    }
}