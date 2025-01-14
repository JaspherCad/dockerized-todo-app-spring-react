package com.jaspher.rest.webservices.restfulwebservices.customexceptions;

public class AccountAlreadyExistsException extends RuntimeException {
    public AccountAlreadyExistsException(String message) {
        super(message);
    }
}
