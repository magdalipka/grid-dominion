package com.example.griddominion.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

public class Headers extends HttpHeaders {

  public Headers() {
    this.add("Content-Type", "application/json");
  }

  public Headers addSid(String sid) {
    var cookie = ResponseCookie
        .from("sid", sid)
        .secure(false)
        .httpOnly(false)
        .path("/")
        .maxAge(3600 * 24)
        .build();

    this.add(HttpHeaders.SET_COOKIE, cookie.toString());
    return this;
  }
}
