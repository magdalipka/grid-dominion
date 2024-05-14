package com.example.griddominion.utils;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

public class PasswordHash {
  public static String hash(String passwordToHash) {

    try {
      MessageDigest md = MessageDigest.getInstance("SHA-512");
      byte[] hashedPassword = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
      return Base64.getEncoder().encodeToString(hashedPassword);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static Boolean compare(String providedPassword, String hashedString) {

    try {
      MessageDigest md = MessageDigest.getInstance("SHA-512");
      String hashedPassword = Base64.getEncoder()
          .encodeToString(md.digest(providedPassword.getBytes(StandardCharsets.UTF_8)));
      return hashedPassword.equals(hashedString);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}