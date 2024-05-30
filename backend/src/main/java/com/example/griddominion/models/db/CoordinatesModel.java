package com.example.griddominion.models.db;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class CoordinatesModel {
    @Column(name = "x")
    private double x;

    @Column(name = "y")
    private double y;



    // Constructors
    public CoordinatesModel() {}

    public CoordinatesModel(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Getters and setters
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
