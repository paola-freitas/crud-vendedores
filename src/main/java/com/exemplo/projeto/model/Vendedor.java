package com.exemplo.projeto.model;

import com.exemplo.projeto.enums.TipoContratacao;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "vendedores")
public class Vendedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "matricula", nullable = false, unique = true)
    private String matricula;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "documento", nullable = false, unique = true)
    private String documento;

    @Column(name = "tipo_contratacao", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoContratacao tipoContratacao;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
        setMatriculaBasedOnIdAndTipoContratacao();  // Atualiza matrícula quando o ID é setado
    }

    public String getMatricula() {
        return matricula;
    }

    private void setMatriculaBasedOnIdAndTipoContratacao() {
        if (this.id != null && this.tipoContratacao != null) {
            this.matricula = this.id + "-" + this.tipoContratacao.getSuffix();
        }
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public TipoContratacao getTipoContratacao() {
        return tipoContratacao;
    }

    public void setTipoContratacao(TipoContratacao tipoContratacao) {
        this.tipoContratacao = tipoContratacao;
        setMatriculaBasedOnIdAndTipoContratacao();  // Atualiza matrícula quando o tipo de contratação é setado
    }
}