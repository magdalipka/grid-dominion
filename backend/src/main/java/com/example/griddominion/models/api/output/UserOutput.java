package com.example.griddominion.models.api.output;

import com.example.griddominion.models.db.InventoryModel;
import com.example.griddominion.models.db.UserModel;

public class UserOutput {
  public String id;
  public String nick;
  public InventoryModel inventory;

  public UserOutput(UserModel userModel) {
    this.id = userModel.getId();
    this.nick = userModel.getNick();
    this.inventory = userModel.getInventory();
  }

  public UserOutput(String id, String nick, InventoryModel inventory) {
    this.id = id;
    this.nick = nick;
    this.inventory = inventory;
  }

}