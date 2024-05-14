package com.example.griddominion.utils.errors;

import org.springframework.web.server.ResponseStatusException;

public class NotFound extends ResponseStatusException {
  public NotFound(String errorMessage) {
    super(org.springframework.http.HttpStatus.NOT_FOUND, errorMessage);
  }
}
