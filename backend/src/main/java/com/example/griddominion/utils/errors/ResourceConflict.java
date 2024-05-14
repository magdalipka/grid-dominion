package com.example.griddominion.utils.errors;

import org.springframework.web.server.ResponseStatusException;

public class ResourceConflict extends ResponseStatusException {
  public ResourceConflict(String errorMessage) {
    super(org.springframework.http.HttpStatus.CONFLICT, errorMessage);
  }
}
