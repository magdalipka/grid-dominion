package com.example.griddominion.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.griddominion.models.db.MinionModel;

public interface MinionRepository extends JpaRepository<MinionModel, String> {

}
