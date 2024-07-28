package com.exemplo.projeto.model;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "filiais")
public class Filial {
    @Id
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

    @NotBlank(message = "O tipo é obrigatório")
    private String tipo;

    private boolean ativo;

    private LocalDateTime dataCadastro;

    private LocalDateTime ultimaAtualizacao;
}
