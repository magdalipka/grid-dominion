package com.example.griddominion.repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.griddominion.models.db.InventoryModel;
import com.example.griddominion.models.db.UserModel;

public interface InventoryRepository extends JpaRepository<InventoryModel, Long> {
    InventoryModel findByUserId(UserModel user);
}
