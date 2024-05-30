package com.example.griddominion.utils.errors;

import org.springframework.web.server.ResponseStatusException;

public class InsufficientStorage extends ResponseStatusException {
    public InsufficientStorage(String errorMessage) {
        super(org.springframework.http.HttpStatus.INSUFFICIENT_STORAGE, errorMessage);
    }
}

