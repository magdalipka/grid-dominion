package com.example.griddominion.repositories;

import java.time.LocalDateTime;
import java.util.List;

import com.example.griddominion.models.db.ClanModel;
import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import jakarta.transaction.Transactional;

public interface ClanRepository extends JpaRepository<ClanModel, String> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO clans (id, name, isPrivate, level, experience, experienceToLevelUp) VALUES (?1, ?2, ?3, ?4, ?5, ?6)", nativeQuery = true)
    void insert(String id, String name, boolean isPrivate, int level, int experience, int experienceToLevelUp)
            throws DataIntegrityViolationException;

    List<ClanModel> findAll(); // JPA generates method automatically?


}