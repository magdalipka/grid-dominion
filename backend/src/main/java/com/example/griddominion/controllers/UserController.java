package com.example.griddominion.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.example.griddominion.models.api.input.UserCreationInput;
import com.example.griddominion.models.api.output.SessionOutput;
import com.example.griddominion.models.api.output.UserOutput;
import com.example.griddominion.services.UserService;
import com.example.griddominion.utils.BasicAuth;
import com.example.griddominion.utils.Headers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/users")
public class UserController {

  Logger logger = LoggerFactory.getLogger(UserController.class);

  @Autowired()
  private UserService userService;

  @PostMapping()
  public ResponseEntity<SessionOutput> registerUser(@RequestBody UserCreationInput userInput) {
    var user = userService.createUser(userInput);
    var session = userService.login(user.getNick(), userInput.password);
    var SessionOutput = new SessionOutput(session);
    return ResponseEntity.ok().headers(
        new Headers().addSid(session.getId())).body(SessionOutput);
  }

  @PostMapping("/login")
  public ResponseEntity<SessionOutput> loginUser(@RequestHeader("Authorization") String authHeader) {
    var userInput = BasicAuth.decode(authHeader);
    var session = userService.login(userInput.getNick(), userInput.getPassword());

    var SessionOutput = new SessionOutput(session);
    return ResponseEntity.ok().headers(
        new Headers().addSid(session.getId())).body(SessionOutput);
  }

  @PostMapping("/session")
  public ResponseEntity<SessionOutput> refreshSession(@CookieValue("sid") String authToken) {
    var session = userService.refreshSession(authToken);

    var SessionOutput = new SessionOutput(session);
    return ResponseEntity.ok().headers(
        new Headers().addSid(session.getId())).body(SessionOutput);
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logoutUser(@CookieValue("sid") String authToken) {
    userService.logout(authToken);
    return ResponseEntity.ok().headers(new Headers().addSid("")).build();
  }

  @GetMapping()
  public ResponseEntity<UserOutput> getMe(@CookieValue("sid") String authToken) {
    var user = userService.getUserBySessionToken(authToken);
    return ResponseEntity.ok(new UserOutput(user));
  }

}