package com.example.griddominion.services;

import com.example.griddominion.models.api.input.ClanCreationInput;
import com.example.griddominion.models.db.ClanModel;
import com.example.griddominion.models.db.InventoryModel;
import com.example.griddominion.models.db.UserModel;
import com.example.griddominion.repositories.ClanRepository;
import com.example.griddominion.repositories.InventoryRepository;
import com.example.griddominion.utils.Constants;
import com.example.griddominion.utils.Item;
import com.example.griddominion.utils.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class InventoryService {

  @Autowired
  InventoryRepository inventoryRepository;

  // TODO ?

  /*
   * public boolean addToInventory(InventoryModel inventoryModel, Item item,
   * Integer number){
   * if(inventoryModel.getInventory().containsKey(item)){
   * inventoryModel.getInventory().put(item,
   * inventoryModel.getInventory().get(item) + number);
   * }
   * else {
   * inventoryModel.getInventory().put(item, number);
   * }
   * 
   * // check for any possible errors?
   * }
   * 
   * public boolean removeFromInventory(InventoryModel inventoryModel, Item item,
   * Integer number){
   * if(inventoryModel.getInventory().containsKey(item)){
   * inventoryModel.getInventory().put(item,
   * inventoryModel.getInventory().get(item) - number);
   * if(inventoryModel.getInventory().get(item) < 0){
   * throw new InsufficientStorage("Can not remove more items than user possess");
   * inventoryModel.getInventory().put(item, 0);
   * }
   * }
   * else {
   * inventoryModel.getInventory().put(item, number);
   * }
   * 
   * // how to handle error if number is greater than user possess?
   * }
   */

}