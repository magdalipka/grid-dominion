package com.example.griddominion.models.api.output;

import com.example.griddominion.models.db.UserModel;

public class UserOutput {
  public String id;
  public String nick;
  public String clanId;

  public UserOutput(UserModel userModel) {
    this.id = userModel.getId();
    this.nick = userModel.getNick();
    this.clanId = userModel.getClan() != null ? userModel.getClan().getId() : null;
  }

}