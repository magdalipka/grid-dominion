package com.example.griddominion.services;


import java.util.HashMap;
import java.util.UUID;

import com.example.griddominion.models.db.ClanModel;
import com.example.griddominion.utils.Constants;
import com.example.griddominion.utils.Item;

import com.example.griddominion.utils.UserJoinClanResponse;
import com.example.griddominion.utils.errors.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.griddominion.models.api.input.UserCreationInput;
import com.example.griddominion.models.db.SessionModel;
import com.example.griddominion.models.db.UserModel;
import com.example.griddominion.models.db.InventoryModel;
import com.example.griddominion.repositories.InventoryRepository;
import com.example.griddominion.repositories.SessionRepository;
import com.example.griddominion.repositories.UserRepository;
import com.example.griddominion.utils.PasswordHash;

import org.springframework.dao.DataIntegrityViolationException;


@Service
public class UserService {

  Logger logger = LoggerFactory.getLogger(UserService.class);

  @Autowired
  UserRepository userRepository;
  @Autowired
  SessionRepository sessionRepository;
  @Autowired
  InventoryRepository inventoryRepository;

  public UserModel createUser(UserCreationInput input) {

    if (input.nick == null || input.password == null) {
      throw new BadRequest("Invalid input");
    }

    if (userRepository.existsByNick(input.nick)) {
      throw new ResourceConflict("User with nick " + input.nick + " already exists");
    }

    String hashedPassword = PasswordHash.hash(input.password);
    String id = UUID.randomUUID().toString();

    UserModel user = new UserModel();
    user.setId(id);
    user.setNick(input.nick);
    user.setLevel(1);
    user.setExperience(0);
    user.setExperienceToLevelUp(Constants.BASE_EXPERIENCE);
    user.setHashedPassword(hashedPassword);
    user.setCreatedAt();

    try {
      userRepository.insert(user.getId(), user.getHashedPassword(), user.getCreatedAt(), user.getNick(), user.getLevel(), user.getExperience(), user.getExperienceToLevelUp());
      InventoryModel inventory = new InventoryModel();
      inventory.setUser(user);
      HashMap<Item, Integer> inventoryHashMap =  new HashMap<>();
      inventoryHashMap.put(Item.GOLD,1000);
      inventoryHashMap.put(Item.FOOD,1000);
      inventoryHashMap.put(Item.WOOD,1000);
      inventory.setInventory(inventoryHashMap);
      inventoryRepository.save(inventory);
    } catch (DataIntegrityViolationException e) {
      throw new ResourceConflict("User with nick " + input.nick + " already exists");
    }


    return user;
  }

  public void deleteUser(String id) {
    UserModel user = userRepository.findById(id).orElse(null);
    InventoryModel inventory = inventoryRepository.findByUserId(user);

    if (user == null) {
      throw new Unauthorized("User not found");
    }
    inventoryRepository.delete(inventory);
    userRepository.delete(user);
  }

  public SessionModel login(String nick, String password) {
    UserModel user = userRepository.findByNick(nick);

    if (user == null || !PasswordHash.compare(password, user.getHashedPassword())) {
      throw new Unauthorized("Incorrect credentials");
    }

    String sessionId = UUID.randomUUID().toString();
    SessionModel session = new SessionModel();
    session.setId(sessionId);
    session.setUserId(user.getId());
    session.setCreatedAt();
    session.setUpdatedAt();
    session.setExpiresAt();

    this.sessionRepository.save(session);

    return session;
  }

  public void logout(String sessionId) {
    SessionModel session = this.sessionRepository.findById(sessionId).orElse(null);

    if (session == null) {
      throw new Unauthorized("Session not found");
    }

    this.sessionRepository.delete(session);
  }

  public SessionModel refreshSession(String sessionId) {
    SessionModel session = this.sessionRepository.findById(sessionId).orElse(null);

    if (session == null) {
      throw new Unauthorized("Session not found");
    }

    if (session.isExpired()) {
      this.sessionRepository.delete(session);
      throw new Unauthorized("Session expired");
    }

    session.setUpdatedAt();
    session.setExpiresAt();

    this.sessionRepository.save(session);

    return session;
  }

  public UserModel getUserBySessionToken(String sessionId) {

    if ("".equals(sessionId)) {
      throw new Unauthorized("Not logged in");
    }

    SessionModel session = this.sessionRepository.findById(sessionId).orElse(null);

    if (session == null) {
      throw new Unauthorized("Session not found");
    }

    if (session.isExpired()) {
      this.sessionRepository.delete(session);
      throw new Unauthorized("Session expired");
    }

    UserModel user = this.userRepository.findById(session.getUserId()).orElse(null);

    if (user == null) {
      throw new Unauthorized("User not found");
    }

    return user;
  }

  public UserModel getUserById(String userId) {
    UserModel user = this.userRepository.findById(userId).orElse(null);

    if (user == null) {
      throw new NotFound("User not found");
    }

    return user;
  }

  public UserJoinClanResponse joinClan(UserModel user, ClanModel clan) {
    if (clan.getUsersList().size() >= Constants.MAX_CLAN_MEMBERS) {
      return UserJoinClanResponse.FULL;
    }
    if (clan.isPrivate() == false) {
      clan.getUsersList().add(user);
      user.setClan(clan);
      return UserJoinClanResponse.JOINED;
    }
    else {
      clan.getUsersToApprove().add(user);
      return UserJoinClanResponse.SENT_INVITE;
    }
  }
}
