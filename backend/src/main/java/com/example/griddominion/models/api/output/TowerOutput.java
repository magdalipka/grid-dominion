package com.example.griddominion.models.api.output;

import com.example.griddominion.models.db.TowerModel;

public class TowerOutput extends BuildingOutput {
  public int hpMax;
  public int hpCurr;
  public int attack;
  public int goldCostMinion;
  public int woodCostMinion;
  public int foodCostMinion;
  public int goldCostRepair;
  public int woodCostRepair;
  public int foodCostRepair;

  public TowerOutput(TowerModel tower) {
    super(tower);
    this.hpMax = tower.getHealth();
    this.hpCurr = tower.getHealthCurrent();
    this.attack = tower.getAttack();
    this.goldCostMinion = tower.getMinionFoodCost();
    this.woodCostMinion = tower.getMinionWoodCost();
    this.foodCostMinion = tower.getMinionFoodCost();
    this.goldCostRepair = tower.getGoldCostRepair();
    this.woodCostRepair = tower.getWoodCostRepair();
    this.foodCostRepair = tower.getFoodCostRepair();
  }

}
