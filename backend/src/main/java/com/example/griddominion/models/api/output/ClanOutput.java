package com.example.griddominion.models.api.output;

import java.util.List;
import java.util.stream.Collectors;

import com.example.griddominion.models.db.ClanModel;

public class ClanOutput {
  public String id;
  public String name;
  public boolean isPrivate;
  public List<UserOutput> users;
  public List<UserOutput> usersToApprove;
  public String adminId;

  public ClanOutput(ClanModel clanModel) {
    this.id = clanModel.getId();
    this.name = clanModel.getName();
    this.isPrivate = clanModel.isPrivate();
    this.users = clanModel.getUsersList().stream().map(UserOutput::new).collect(Collectors.toList());
    this.usersToApprove = clanModel.getUsersToApprove().stream().map(UserOutput::new).collect(Collectors.toList());
    this.adminId = clanModel.getAdmin().getId();
  }

}
