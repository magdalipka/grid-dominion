package com.example.griddominion.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.griddominion.models.db.SessionModel;


public interface SessionRepository extends JpaRepository<SessionModel, String> {
}