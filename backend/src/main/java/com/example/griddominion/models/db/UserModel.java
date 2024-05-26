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

  @Column(name = "level")
  private int level;

  @Column(name = "experience")
  private int experience;

  @Column(name = "experienceToLevelUp")
  private int experienceToLevelUp;

  @ManyToOne
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

  public int getExperience(){
    return experience;
  }

  public void setExperience(int experience){
    this.experience = experience;
  }

  public void earnExperience(int experience){
    this.experience += experience;
    if(getExperience() >= getExperienceToLevelUp()){
      if(getLevel() < Constants.MAX_USER_LEVEL) {
        setLevel(level + 1);
        setExperience(0);
        setExperienceToLevelUp((int) Math.round((Math.pow(level, 1.2)) * Constants.BASE_EXPERIENCE));
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