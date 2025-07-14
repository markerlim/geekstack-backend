package com.geekstack.cards.exception;

public class DocumentNotFoundException extends RuntimeException {
    
    public DocumentNotFoundException() {
        super();
    }

    public DocumentNotFoundException(String message) {
        super(message);
    }

    public DocumentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}