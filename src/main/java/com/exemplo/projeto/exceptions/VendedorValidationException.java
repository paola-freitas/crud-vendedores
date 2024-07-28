package com.exemplo.projeto.exceptions;

public class VendedorValidationException extends RuntimeException {

    public VendedorValidationException() {
        super("Tipo contratação informado está incorreto.");
    }
}
