package com.example.griddominion.models.db;

import java.time.LocalDateTime;

import com.example.griddominion.utils.Constants;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "users")
public class UserModel {
  @Id
  private String id;

  @Column(name = "hashed_password")
  private String hashedPassword;

  @Column(name = "created_at")
  private @CreatedDate LocalDateTime createdAt;

  @Column(name = "nick")
  private String nick;

  @Column(name = "coordinates")
  private CoordinatesModel coordinates;

  @Column(name = "level", nullable = true)
  private Integer level;

  @Column(name = "experience", nullable = true)
  private Integer experience;

  @Column(name = "experience_to_level_up", nullable = true)
  private Integer experienceToLevelUp;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private InventoryModel inventory;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "clanId")
  private ClanModel clan;

  public UserModel() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getHashedPassword() {
    return hashedPassword;
  }

  public void setHashedPassword(String hashedPassword) {
    this.hashedPassword = hashedPassword;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt() {
    this.createdAt = LocalDateTime.now();
  }

  public void setCreatedAt(LocalDateTime createdDate) {
    this.createdAt = createdDate;
  }

  public String getNick() {
    return nick;
  }

  public void setNick(String nick) {
    this.nick = nick;
  }

  public CoordinatesModel getCoordinates() {
    return coordinates;
  }

  public void setCoordinates(CoordinatesModel coordinates) {
    this.coordinates = coordinates;
  }

  public Integer getExperience() {
    return experience;
  }

  public void setExperience(int experience) {
    this.experience = experience;
  }

  public void earnExperience(int experience) {
    this.experience += experience;
    if (getExperience() >= getExperienceToLevelUp()) {
      if (getLevel() < Constants.MAX_USER_LEVEL) {
        setLevel(level + 1);
        setExperience(0);
        setExperienceToLevelUp((int) Math.round((Math.pow(level, 1.2)) * Constants.BASE_EXPERIENCE));
      }
    }
  }

  public Integer getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public Integer getExperienceToLevelUp() {
    return experienceToLevelUp;
  }

  public void setExperienceToLevelUp(int experienceToLevelUp) {
    this.experienceToLevelUp = experienceToLevelUp;
  }

  public InventoryModel getInventory() {
    return inventory;
  }

  public void setInventory(InventoryModel inventory) {
    this.inventory = inventory;
  }

  public ClanModel getClan() {
    return clan;
  }

  public void setClan(ClanModel clan) {
    this.clan = clan;
  }

  @Override
  public String toString() {
    return "UserModel [createdDate=" + createdAt + ", hashedPassword=" + hashedPassword + ", id=" + id + ", nick="
        + nick + "]";
  }

}