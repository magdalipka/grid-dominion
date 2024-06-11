package com.example.griddominion.controllers;

import com.example.griddominion.models.api.output.UserOutputWithCoordinates;
import com.example.griddominion.services.UserService;
import com.example.griddominion.utils.errors.Unauthorized;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.example.griddominion.models.api.input.ClanCreationInput;
import com.example.griddominion.models.api.input.ResourcesTransferInput;
import com.example.griddominion.models.api.output.ClanOutput;
import com.example.griddominion.services.ClanService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clans")
public class ClanController {

  @Autowired
  private ClanService clanService;
  @Autowired
  private UserService userService;

  @PostMapping()
  public ResponseEntity<ClanOutput> createClan(@RequestBody ClanCreationInput input,
      @CookieValue("sid") String authToken) {
    var user = userService.getUserBySessionToken(authToken);
    var clan = clanService.createClan(input, user);
    var clanOutput = new ClanOutput(clan);
    return ResponseEntity.ok(clanOutput);
  }

  @GetMapping()
  public ResponseEntity<List<ClanOutput>> getAllClans() {
    var clans = clanService.getAllClans();
    List<ClanOutput> clanOutputs = clans.stream()
        .map(ClanOutput::new)
        .collect(Collectors.toList());
    return ResponseEntity.ok(clanOutputs);
  }

  @GetMapping("/{clan_id}")
  public ResponseEntity<ClanOutput> getClan(@PathVariable("clan_id") String clanId) {
    var clan = clanService.getClanById(clanId);
    var clanOutput = new ClanOutput(clan);
    return ResponseEntity.ok(clanOutput);
  }

  @PostMapping("/sendResources")
  public ResponseEntity<?> sendResources(@RequestBody ResourcesTransferInput resourcesTransferInput,
      @CookieValue("sid") String authToken) {
    var user = userService.getUserBySessionToken(authToken);
    clanService.sendResources(user, resourcesTransferInput);
    return ResponseEntity.ok().body("Resources transferred successfully.");
  }

  @GetMapping("/{clan_id}/usersWithTheirCoordinates")
  public ResponseEntity<List<UserOutputWithCoordinates>> getUsersWithTheirCoordinates(
      @PathVariable("clan_id") String clanId) {
    var users = clanService.getUsersInClan(clanId);
    List<UserOutputWithCoordinates> userOutputs = users.stream()
        .map(UserOutputWithCoordinates::new)
        .collect(Collectors.toList());
    return ResponseEntity.ok(userOutputs);
  }

  @PostMapping("/{clan_id}/join")
  public ResponseEntity<?> joinClan(@PathVariable("clan_id") String clanId,
      @CookieValue("sid") String authToken) {
    var user = userService.getUserBySessionToken(authToken);
    var clan = clanService.getClanById(clanId);
    if (clan.getUsersList().contains(user)) {
      return ResponseEntity.badRequest().body("User is already in clan.");
    }
    if (clan.getUsersToApprove().contains(user)) {
      return ResponseEntity.badRequest().body("User is already waiting for approval in this class.");
    }
    clanService.addUserToApprovalList(user, clan);
    return ResponseEntity.ok().body("User was added to approval list.");
  }

  @PostMapping("/{clan_id}/approveUser/{user_id}")
  public ResponseEntity<?> approveUser(@PathVariable("clan_id") String clanId, @PathVariable("user_id") String user_id,
      @CookieValue("sid") String authToken) {
    try {
      var requestingUser = userService.getUserBySessionToken(authToken);
      var clan = clanService.getClanById(clanId);
      var user = userService.getUserById(user_id);
      if (clan.getAdmin().getId().equals(requestingUser.getId()) == false) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only admin can approve users to join the clan.");
      }
      if (clan.getUsersList().contains(user)) {
        return ResponseEntity.badRequest().body("User is already in clan.");
      }
      if (clan.getUsersToApprove().contains(user) == false) {
        return ResponseEntity.badRequest().body("User is not waiting for approval in this class.");
      }
      clanService.addUserToClan(user, clan);
      clan.getUsersToApprove().remove(user);
      return ResponseEntity.ok().body("User was approved.");
    } catch (Unauthorized e) {
      return ResponseEntity.badRequest().body("Session not found");
    }
  }

  @PostMapping("/{clan_id}/rejectUser/{user_id}")
  public ResponseEntity<?> rejectUser(@PathVariable("clan_id") String clanId, @PathVariable("user_id") String userId,
      @CookieValue("sid") String authToken) {
    try {
      var requestingUser = userService.getUserBySessionToken(authToken);
      var clan = clanService.getClanById(clanId);
      var user = userService.getUserById(userId);

      if (!clan.getAdmin().getId().equals(requestingUser.getId())) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only admin can reject users from joining the clan.");
      }
      if (!clan.getUsersToApprove().contains(user)) {
        return ResponseEntity.badRequest().body("User is not waiting for approval in this clan.");
      }
      if (clan.getUsersList().contains(user)) {
        return ResponseEntity.badRequest().body("User is a member of this clan, not in waiting list. If you wanted to" +
            " remove user from clan, use /{clan_id}/removeUser/{user_id}");
      }
      clan.getUsersToApprove().remove(user);
      return ResponseEntity.ok().body("User was rejected.");
    } catch (Unauthorized e) {
      return ResponseEntity.badRequest().body("Session not found");
    }
  }

  @PostMapping("/{clan_id}/removeUser/{user_id}")
  public ResponseEntity<?> removeUser(@PathVariable("clan_id") String clanId, @PathVariable("user_id") String userId,
      @CookieValue("sid") String authToken) {
    try {
      var requestingUser = userService.getUserBySessionToken(authToken);
      var clan = clanService.getClanById(clanId);
      var user = userService.getUserById(userId);

      if (!clan.getAdmin().getId().equals(requestingUser.getId())) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only admin can remove users from the clan.");
      }

      if (!clan.getUsersList().contains(user)) {
        return ResponseEntity.badRequest().body("User is not a member of the clan.");
      }

      clanService.removeUser(user, clan);
      return ResponseEntity.ok().body("User was removed from the clan.");
    } catch (Unauthorized e) {
      return ResponseEntity.badRequest().body("Session not found");
    }
  }

}
