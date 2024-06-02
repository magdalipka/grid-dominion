package com.example.griddominion.models.api.output;

import com.example.griddominion.models.db.InventoryModel;
import com.example.griddominion.models.db.UserModel;

public class UserOutput {
  public String id;
  public String nick;

  public UserOutput(UserModel userModel) {
    this.id = userModel.getId();
    this.nick = userModel.getNick();
  }

  public UserOutput(String id, String nick) {
    this.id = id;
    this.nick = nick;
  }

}