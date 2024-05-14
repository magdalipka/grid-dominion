package com.example.griddominion.models.db;


import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "sessions")
public class SessionModel {
  @Id
  private String id;

  @Column(name = "user_id")
  private String userId;

  @Column(name = "created_at")
  private @CreatedDate LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Column(name = "expires_at")
  private LocalDateTime expiresAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
  private UserModel user;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
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

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt() {
    this.updatedAt = LocalDateTime.now();
  }

  public void setUpdatedAt(LocalDateTime updatedDate) {
    this.updatedAt = updatedDate;
  }

  public LocalDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(LocalDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  public void setExpiresAt() {
    this.expiresAt = LocalDateTime.now().plusDays(7);
  }

  public void setExpiresAt(int days) {
    this.expiresAt = LocalDateTime.now().plusDays(days);
  }

  public Boolean isExpired() {
    return LocalDateTime.now().isAfter(expiresAt);
  }

}