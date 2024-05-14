package com.example.griddominion.repositories;

import java.time.LocalDateTime;

import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.griddominion.models.db.UserModel;

import jakarta.transaction.Transactional;

public interface UserRepository extends JpaRepository<UserModel, String> {
  Boolean existsByNick(String nick);

  UserModel findByNick(String nick);

  @Modifying
  @Transactional
  @Query(value = "INSERT INTO users (id, nick, hashed_password, created_at) VALUES (?1, ?2, ?3, ?4)", nativeQuery = true)
  void insert(String id, String nick, String hashedPassword, LocalDateTime createdAt)
      throws DataIntegrityViolationException;

}