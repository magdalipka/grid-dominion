package com.example.griddominion.models.api.output;

import com.example.griddominion.models.db.MinionModel;

public class MinionOutput {
  public String id;
  public Integer level;
  public Integer experience;
  public Integer experienceToLevelUp;
  public double movementSpeed;
  public double attackDamage;
  public double attackSpeed;
  public double hp;
  public String owner;
  public Integer territory;

  public MinionOutput(MinionModel minionModel) {
    this.id = minionModel.getId();
    this.level = minionModel.getLevel();
    this.experience = minionModel.getExperience();
    this.experienceToLevelUp = minionModel.getExperienceToLevelUp();
    this.movementSpeed = minionModel.getMovementSpeed();
    this.attackDamage = minionModel.getAttackDamage();
    this.attackSpeed = minionModel.getAttackSpeed();
    this.hp = minionModel.getHp();
    this.owner = minionModel.getOwner().getId();
    this.territory = minionModel.getTerritory().getId();
  }

  public MinionOutput(String id, Integer level, Integer experience, Integer experienceToLevelUp,
      double movementSpeed, double attackDamage, double attackSpeed, double hp,
      String owner, Integer territory) {
    this.id = id;
    this.level = level;
    this.experience = experience;
    this.experienceToLevelUp = experienceToLevelUp;
    this.movementSpeed = movementSpeed;
    this.attackDamage = attackDamage;
    this.attackSpeed = attackSpeed;
    this.hp = hp;
    this.territory = territory;
  }
}
