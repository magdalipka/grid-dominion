package com.example.griddominion.models.db;

import java.util.ArrayList;
import java.util.List;

import com.example.griddominion.utils.Constants;
import jakarta.persistence.*;

@Entity
@Table(name = "clans")
public class ClanModel {
  @Id
  private String id;

  @Column(name = "name")
  private String name;

  @Column(name = "admin_id")
  private String adminId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "admin_id", nullable = false, insertable = false, updatable = false)
  private UserModel admin;

  @Column(name = "is_private", nullable = true)
  private Boolean isPrivate;

  @Column(name = "level", nullable = true)
  private Integer level;

  @Column(name = "experience", nullable = true)
  private Integer experience;

  @Column(name = "experience_to_level_up", nullable = true)
  private Integer experienceToLevelUp;

  @OneToMany(mappedBy = "clan", fetch = FetchType.LAZY)
  private List<UserModel> users;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name = "users_to_approve", joinColumns = @JoinColumn(name = "id"))
  @Column(name = "users_to_approve")
  private List<UserModel> usersToApprove;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isPrivate() {
    return this.isPrivate;
  }

  public void makePrivate() {
    this.isPrivate = true;
  }

  public void makePublic() {
    this.isPrivate = false;
  }

  public int getExperience() {
    return experience;
  }

  public void setExperience(int experience) {
    this.experience = experience;
  }

  public void earnExperience(int experience) {
    this.experience += experience;
    if (getExperience() >= getExperienceToLevelUp()) {
      if (getLevel() < Constants.MAX_CLAN_LEVEL) {
        setLevel(level + 1);
        setExperience(0);
        setExperienceToLevelUp((int) Math.round((Math.pow(level, 1.2)) * Constants.BASE_CLAN_EXPERIENCE));
      }
    }
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public int getExperienceToLevelUp() {
    return experienceToLevelUp;
  }

  public void setExperienceToLevelUp(int experienceToLevelUp) {
    this.experienceToLevelUp = experienceToLevelUp;
  }

  public List<UserModel> getUsersList() {
    return this.users;
  }

  public UserModel getAdmin() {
    return admin;
  }

  public void setAdminId(UserModel admin) {
    this.adminId = admin.getId();
  }

  public List<UserModel> getUsersToApprove() {
    return usersToApprove;
  }

  public void setUsersToApprove(List<UserModel> usersToApprove) {
    this.usersToApprove = usersToApprove;
  }
}