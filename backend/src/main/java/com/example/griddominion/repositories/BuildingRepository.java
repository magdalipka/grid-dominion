package com.example.griddominion.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.griddominion.models.db.BuildingModel;

public interface BuildingRepository extends JpaRepository<BuildingModel, Long> {
}
