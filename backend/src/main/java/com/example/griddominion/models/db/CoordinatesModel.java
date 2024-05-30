package com.example.griddominion.models.db;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class CoordinatesModel {
    @Column(name = "x")
    private int x;

    @Column(name = "y")
    private int y;



    // Constructors
    public CoordinatesModel() {}

    public CoordinatesModel(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Getters and setters
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
