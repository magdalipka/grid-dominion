package com.example.griddominion.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import com.example.griddominion.models.api.input.UserLoginInput;


public class BasicAuth {
  public static UserLoginInput decode(String authHeader) {
    String base64Credentials = authHeader.substring("Basic".length()).trim();
    byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
    String credentials = new String(credDecoded, StandardCharsets.UTF_8);
    // credentials = username:password
    String[] values = credentials.split(":", 2);
    return new UserLoginInput(values[0], values[1]);
  }
}