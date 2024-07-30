package com.exemplo.projeto.exceptions;

public class VendedorNotFoundException extends RuntimeException {

    public VendedorNotFoundException(String matricula) {
        super("Vendedor com matrícula " + matricula + " não existe.");
    }

    public VendedorNotFoundException() {
        super("Vendedor não existe.");
    }
}
