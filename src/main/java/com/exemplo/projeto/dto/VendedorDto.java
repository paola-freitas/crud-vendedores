package com.exemplo.projeto.dto;

import com.exemplo.projeto.enums.TipoContratacao;
import com.sun.istack.NotNull;

import java.time.LocalDate;

public class VendedorDto {
    @NotNull
    private String matricula;

    @NotNull
    private String nome;

    private LocalDate dataNascimento;

    @NotNull
    private String documento;

    @NotNull
    private TipoContratacao tipoContratacao;

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
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
    }

}
