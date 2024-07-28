package com.exemplo.projeto.exceptions;

public class VendedorValidationDocumentoException extends RuntimeException {

    public VendedorValidationDocumentoException() {
        super("Documento com formato errado ou não condiz com o tipo de contratação informado.");
    }
}
