package com.example.griddominion.utils.errors;

import org.springframework.web.server.ResponseStatusException;

public class Forbidden extends ResponseStatusException {
    public Forbidden(String errorMessage) {
        super(org.springframework.http.HttpStatus.FORBIDDEN, errorMessage);
    }
}