package com.example.griddominion.controllers;

import com.example.griddominion.models.api.input.CoordinateInput;
import com.example.griddominion.models.api.output.CoordinatesOutput;
import com.example.griddominion.models.db.ClanModel;
import com.example.griddominion.models.db.CoordinatesModel;
import com.example.griddominion.models.db.UserModel;
import com.example.griddominion.repositories.ClanRepository;
import com.example.griddominion.utils.UserJoinClanResponse;
import com.example.griddominion.utils.errors.NotFound;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.example.griddominion.models.api.input.UserCreationInput;
import com.example.griddominion.models.api.output.SessionOutput;
import com.example.griddominion.models.api.output.UserOutput;
import com.example.griddominion.services.UserService;
import com.example.griddominion.utils.BasicAuth;
import com.example.griddominion.utils.Headers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/users")
public class UserController {

  Logger logger = LoggerFactory.getLogger(UserController.class);

  @Autowired()
  private UserService userService;
  @Autowired()
  private ClanRepository clanRepository;

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

  @PostMapping("/coord")
  public ResponseEntity<UserOutput> saveUserCoordinates(@CookieValue("sid") String authToken, @RequestBody CoordinateInput input) {
    UserModel user = userService.getUserBySessionToken(authToken);
    user.setCoordinates(new CoordinatesModel(input.x, input.y));
    return ResponseEntity.ok(new UserOutput(user));
  }

  @GetMapping("/coord")
  public ResponseEntity<CoordinatesOutput> getUserCoordinates(@CookieValue("sid") String authToken) {
    UserModel user = userService.getUserBySessionToken(authToken);
    CoordinatesModel coordinates = user.getCoordinates();
    CoordinatesOutput coordinatesOutput = new CoordinatesOutput(coordinates.getX(), coordinates.getY());
    return ResponseEntity.ok(coordinatesOutput);
  }

  @PatchMapping("/joinClan/{clan_id}")
  public ResponseEntity<String> joinClan(@CookieValue("sid") String authToken, @PathVariable("clan_id") String clanId) {
    UserModel user = userService.getUserBySessionToken(authToken);
    ClanModel clan = clanRepository.findById(clanId).orElse(null);
    if(clan==null) throw new NotFound("Clan not found");
    UserJoinClanResponse response = userService.joinClan(user, clan);
    HttpStatus httpStatus;
    String message;
    switch (response) {
      case JOINED:
        httpStatus = HttpStatus.OK;
        message = "User joined clan successfully.";
        break;
      case SENT_INVITE:
        httpStatus = HttpStatus.ACCEPTED;
        message = "User sent an invite to join the clan.";
        break;
      case FULL:
        httpStatus = HttpStatus.INSUFFICIENT_STORAGE;
        message = "Clan is full. User could not join.";
        break;
      default:
        httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        message = "Unknown error occurred.";
    }
    return ResponseEntity.status(httpStatus).body(message);
  }




}