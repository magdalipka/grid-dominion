package com.example.griddominion.models.db;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "territories")
public class TerritoryModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = true)
    private UserModel owner;

    @Column(name = "max_latitude")
    private double maxLatitude;

    @Column(name = "min_latitude")
    private double minLatitude;

    @Column(name = "max_longitude")
    private double maxLongitude;

    @Column(name = "min_longitude")
    private double minLongitude;

    @Column(name = "gold")
    private int gold;

    @Column(name = "wood")
    private int wood;

    @Column(name = "food")
    private int food;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserModel getOwner() {
        return owner;
    }

    public void setOwner(UserModel owner) {
        this.owner = owner;
    }

    public double getMaxLatitude() {
        return maxLatitude;
    }

    public void setMaxLatitude(double maxLatitude) {
        this.maxLatitude = maxLatitude;
    }

    public double getMinLatitude() {
        return minLatitude;
    }

    public void setMinLatitude(double minLatitude) {
        this.minLatitude = minLatitude;
    }

    public double getMaxLongitude() {
        return maxLongitude;
    }

    public void setMaxLongitude(double maxLongitude) {
        this.maxLongitude = maxLongitude;
    }

    public double getMinLongitude() {
        return minLongitude;
    }

    public void setMinLongitude(double minLongitude) {
        this.minLongitude = minLongitude;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getWood() {
        return wood;
    }

    public void setWood(int wood) {
        this.wood = wood;
    }

    public int getFood() {
        return food;
    }

    public void setFood(int food) {
        this.food = food;
    }

    @Override
    public String toString() {
        return "TerritoryModel [id=" + id + ", owner=" + owner.getNick() + ", maxLatitude=" + maxLatitude + ", minLatitude="
                + minLatitude + ", maxLongitude=" + maxLongitude + ", minLongitude=" + minLongitude + ", gold=" + gold
                + ", wood=" + wood + ", food=" + food + "]";
    }

}
