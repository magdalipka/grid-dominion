package com.example.griddominion.models.api.output;

import com.example.griddominion.models.db.CoordinatesModel;
import com.example.griddominion.models.db.UserModel;

public class UserOutputWithCoordinates {
  public String id;
  public String nick;

  public CoordinatesModel coords;

  public UserOutputWithCoordinates(UserModel userModel) {
    this.id = userModel.getId();
    this.nick = userModel.getNick();
    this.coords = userModel.getCoordinates();
  }

  public UserOutputWithCoordinates(String id, String nick, CoordinatesModel coordinatesModel) {
    this.id = id;
    this.nick = nick;
    this.coords = coordinatesModel;
  }

}