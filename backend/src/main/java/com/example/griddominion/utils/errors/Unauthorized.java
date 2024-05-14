package com.example.griddominion.utils.errors;

import org.springframework.web.server.ResponseStatusException;

public class Unauthorized extends ResponseStatusException {
  public Unauthorized(String errorMessage) {
    super(org.springframework.http.HttpStatus.UNAUTHORIZED, errorMessage);
  }
}
