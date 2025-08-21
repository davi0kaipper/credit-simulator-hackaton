package org.acme.domain.exceptions;

public class InvalidPageNumberException extends Exception {
    public InvalidPageNumberException(String message) {
        super(message);
    }
}
