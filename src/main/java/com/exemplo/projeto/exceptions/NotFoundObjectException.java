package com.exemplo.projeto.exceptions;

public class NotFoundObjectException extends RuntimeException {

    public NotFoundObjectException(String message) {
        super(message);
    }
}
