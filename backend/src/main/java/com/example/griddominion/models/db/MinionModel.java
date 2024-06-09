package com.example.griddominion.models.db;

import jakarta.persistence.*;

@Entity
@Table(name = "minions")
public class MinionModel {
    @Id
    private String id;

    @Column(name = "level", nullable = true)
    private Integer level;

    @Column(name = "experience", nullable = true)
    private Integer experience;

    @Column(name = "experience_to_level_up", nullable = true)
    private Integer experienceToLevelUp;

    @Column(name = "movement_speed")
    private double movementSpeed;

    @Column(name = "attack_damage")
    private double attackDamage;

    @Column(name = "attack_speed")
    private double attackSpeed;

    @Column(name = "hp")
    private double hp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private UserModel owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "territory_id", nullable = true)
    private TerritoryModel territory;


    public MinionModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExperience() {
        return experience;
    }
    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getExperienceToLevelUp() {
        return experienceToLevelUp;
    }

    public void setExperienceToLevelUp(int experienceToLevelUp) {
        this.experienceToLevelUp = experienceToLevelUp;
    }

    public double getMovementSpeed() {
        return movementSpeed;
    }

    public void setMovementSpeed(double movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    public double getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(double damage) {
        this.attackDamage = damage;
    }

    public double getAttackSpeed() {
        return attackSpeed;
    }

    public void setAttackSpeed(double attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public double getHp() {
        return hp;
    }

    public void setHp(double hp) {
        this.hp = hp;
    }

    public UserModel getOwner() {
        return owner;
    }

    public void setOwner(UserModel owner) {
        this.owner = owner;
    }

    public TerritoryModel getTerritory() {
        return territory;
    }

    public void setTerritory(TerritoryModel territory) {
        this.territory = territory;
    }

    @Override
    public String toString() {
        return "MinionModel [id=" + id  + ", level=" + level + ", experience=" + experience
                + ", experienceToLevelUp=" + experienceToLevelUp + ", movementSpeed=" + movementSpeed + ", attackDamage=" + attackDamage
                + ", attackSpeed=" + attackSpeed + ", hp=" + hp + ", owner=" + owner + "]";
    }
}
