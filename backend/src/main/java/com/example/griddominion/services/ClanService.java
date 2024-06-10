package com.example.griddominion.services;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.example.griddominion.models.api.input.ClanCreationInput;
import com.example.griddominion.models.api.input.ResourcesTransferInput;
import com.example.griddominion.models.db.ClanModel;
import com.example.griddominion.models.db.InventoryModel;
import com.example.griddominion.models.db.UserModel;
import com.example.griddominion.repositories.ClanRepository;
import com.example.griddominion.repositories.InventoryRepository;
import com.example.griddominion.repositories.UserRepository;
import com.example.griddominion.utils.Constants;
import com.example.griddominion.utils.Item;
import com.example.griddominion.utils.errors.*;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.dao.DataIntegrityViolationException;

@Service
public class ClanService {
  @Autowired
  ClanRepository clanRepository;
  @Autowired
  UserRepository userRepository;
  @Autowired
  InventoryRepository inventoryRepository;

  public ClanModel createClan(ClanCreationInput input, UserModel admin) {

    if (input.name == null) {
      throw new BadRequest("Clan name is not specified");
    }

    // check if clan name already exists here?

    String id = UUID.randomUUID().toString();

    ClanModel clan = new ClanModel();
    clan.setId(id);
    clan.setAdminId(admin);
    clan.setName(input.name);
    if (input.isPrivate)
      clan.makePrivate();
    else
      clan.makePublic();
    clan.setLevel(1);
    clan.setExperience(0);
    clan.setExperienceToLevelUp(Constants.BASE_CLAN_EXPERIENCE);

    try {
      clanRepository.save(clan);
    } catch (DataIntegrityViolationException e) {
      throw new ResourceConflict("Clan with name " + input.name + " already exists");
    }
    admin.setClan(clan);
    userRepository.save(admin);

    return clan;
  }

  public void deleteClan(String id) {
    ClanModel clan = clanRepository.findById(id).orElse(null);

    if (clan == null) {
      throw new Unauthorized("Clan not found");
    }

    clanRepository.delete(clan);
  }

  public ClanModel getClanById(String clanId) {
    ClanModel clan = this.clanRepository.findById(clanId).orElse(null);

    if (clan == null) {
      throw new NotFound("Clan not found");
    }

    return clan;
  }

  public void addUserToApprovalList(UserModel user, ClanModel clan) {
    if (clan.getUsersList().size() < Constants.MAX_CLAN_MEMBERS) {
      clan.getUsersToApprove().add(user);
      user.setClan(clan);
    } else {
      throw new InsufficientStorage("Clan is full!");
    }
  }

  public void addUserToClan(UserModel user, ClanModel clan) {
    if (clan.getUsersList().size() < Constants.MAX_CLAN_MEMBERS) {
      clan.getUsersList().add(user);
      user.setClan(clan);
      clan.getUsersToApprove().remove(user);
      clanRepository.save(clan);
      userRepository.save(user);
    } else {
      throw new InsufficientStorage("Clan is full!");
    }
  }

  public void removeUser(UserModel user, ClanModel clan) {
    if (clan.getUsersList().contains(user)) {
      clan.getUsersList().remove(user);
      user.setClan(null);
    } else {
      throw new NotFound("There is no such user in clan.");
    }
  }

  public List<ClanModel> getAllClans() {
    return clanRepository.findAll();
  }

  @Transactional
  public void sendResources(UserModel sender, ResourcesTransferInput resourcesTransferInput) {
    UserModel reciver = userRepository.findByNick(resourcesTransferInput.reciverNick);
    if (sender == null || reciver == null) {
      throw new Unauthorized("User not found");
    }
    if (sender.getClan() == null || reciver.getClan() == null) {
      throw new Forbidden("Some user is not in clan");
    }
    if (!sender.getClan().equals(reciver.getClan())) {
      throw new Forbidden("Users are not in the same clan");
    }

    InventoryModel send = sender.getInventory();
    HashMap<Item, Integer> hashSend = send.getInventory();
    Integer goldSender, woodSender, foodSender, goldReciver, woodReciver, foodReciver;
    goldSender = hashSend.get(Item.GOLD) - resourcesTransferInput.gold;
    foodSender = hashSend.get(Item.FOOD) - resourcesTransferInput.food;
    woodSender = hashSend.get(Item.WOOD) - resourcesTransferInput.wood;
    if (goldSender < 0 || foodSender < 0 || woodSender < 0) {
      throw new BadRequest("Not enough resources");
    }
    InventoryModel recive = reciver.getInventory();
    HashMap<Item, Integer> hashRecive = recive.getInventory();
    goldReciver = hashRecive.get(Item.GOLD) + resourcesTransferInput.gold;
    foodReciver = hashRecive.get(Item.FOOD) + resourcesTransferInput.food;
    woodReciver = hashRecive.get(Item.WOOD) + resourcesTransferInput.wood;

    if (goldReciver > Constants.RESOURCE_LIMIT || foodReciver > Constants.RESOURCE_LIMIT
        || woodReciver > Constants.RESOURCE_LIMIT) {
      throw new BadRequest("To much resources sent");
    }
    hashSend.put(Item.GOLD, goldSender);
    hashSend.put(Item.FOOD, foodSender);
    hashSend.put(Item.WOOD, woodSender);
    send.setInventory(hashSend);
    hashRecive.put(Item.GOLD, goldReciver);
    hashRecive.put(Item.FOOD, foodReciver);
    hashRecive.put(Item.WOOD, woodReciver);
    recive.setInventory(hashRecive);

    inventoryRepository.save(send);
    inventoryRepository.save(recive);
  }

  public List<UserModel> getUsersInClan(String clanId) {
    ClanModel clan = this.clanRepository.findById(clanId).orElse(null);
    return clan.getUsersList();
  }

  public void addUserToClan(String clanId, UserModel user) {
    ClanModel clan = this.clanRepository.findById(clanId).orElse(null);
    clan.getUsersList().add(user);
    clanRepository.save(clan);
  }

}