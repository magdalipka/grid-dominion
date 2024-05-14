package com.example.griddominion.models.api.input;

public class UserLoginInput {
  private String nick;
  private String password;

  public UserLoginInput(String nick, String password) {
    this.nick = nick;
    this.password = password;
  }

  public String getNick() {
    return nick;
  }

  public void setNick(String nick) {
    this.nick = nick;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
