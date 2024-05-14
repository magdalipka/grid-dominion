package com.example.griddominion.models.db;



import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class UserModel {
  @Id
  private String id;

  @Column(name = "hashed_password")
  private String hashedPassword;

  @Column(name = "created_at")
  private @CreatedDate LocalDateTime createdAt;

  private String nick;

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

  @Override
  public String toString() {
    return "UserModel [createdDate=" + createdAt + ", hashedPassword=" + hashedPassword + ", id=" + id + ", nick="
        + nick + "]";
  }

}