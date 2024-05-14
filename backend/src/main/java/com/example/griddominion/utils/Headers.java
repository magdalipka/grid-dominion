package com.example.griddominion.utils;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

public class Headers extends HttpHeaders {

  private List<String> exposedHeaders = Arrays.asList("Accept", "Access-Control-Allow-Headers",
      "Access-Control-Allow-Methods",
      "Access-Control-Allow-Origin", "Content-Type");

  public Headers() {
    this.add("Access-Control-Allow-Headers", "Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    this.add("Access-Control-Allow-Methods", "*");
    this.add("Access-Control-Allow-Origin", "*");
    this.add("Content-Type", "application/json");
    this.add("Access-Control-Allow-Credentials", "true");
    this.setAccessControlExposeHeaders(this.exposedHeaders);
  }

  public Headers addSid(String sid) {
    var cookie = ResponseCookie
        .from("sid", sid)
        .secure(true)
        .httpOnly(true)
        .path("/")
        .maxAge(3600 * 24)
        .build();

    this.add(HttpHeaders.SET_COOKIE, cookie.toString());
    return this;
  }
}
