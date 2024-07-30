package com.exemplo.projeto.exceptions;

public class VendedorValidationDataNascimentoException extends RuntimeException {

    public VendedorValidationDataNascimentoException() {
        super("Data de nascimento possui valor inválido.");
    }
}
