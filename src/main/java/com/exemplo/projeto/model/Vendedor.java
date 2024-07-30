package com.exemplo.projeto.model;

import com.exemplo.projeto.enums.TipoContratacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String matricula;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @NotBlank(message = "Documento é obrigatório")
    private String documento;

    @NotBlank(message = "E-mail é obrigatório")
    private String email;

    @Column(name = "tipo_contratacao", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoContratacao tipoContratacao;

    @NotNull(message = "IdFilial é obrigatória")
    private Long idFilial;

    private String nomeFilial;

    @PrePersist
    @PreUpdate
    public void updatedMatricula() {
        this.matricula = newMatricula(this.tipoContratacao);
    }

    private String newMatricula(TipoContratacao tipoContratacao) {
        String suffix = tipoContratacao.getSuffix();
        return this.id + "-" + suffix;
    }

}
