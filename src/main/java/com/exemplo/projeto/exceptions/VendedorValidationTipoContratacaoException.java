package com.exemplo.projeto.exceptions;

public class VendedorValidationTipoContratacaoException extends RuntimeException {

    public VendedorValidationTipoContratacaoException() {
        super("Tipo contratação informado não encontrado.");
    }
}
