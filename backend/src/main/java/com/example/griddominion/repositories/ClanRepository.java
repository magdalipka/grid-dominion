package com.example.griddominion.repositories;

import java.util.List;

import com.example.griddominion.models.db.ClanModel;
import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import jakarta.transaction.Transactional;

public interface ClanRepository extends JpaRepository<ClanModel, String> {

  List<ClanModel> findAll(); // JPA generates method automatically?

}