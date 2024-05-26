package com.example.griddominion.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.griddominion.models.db.TerritoryModel;

public interface TerritoryRepository extends JpaRepository<TerritoryModel, Integer> {
}
