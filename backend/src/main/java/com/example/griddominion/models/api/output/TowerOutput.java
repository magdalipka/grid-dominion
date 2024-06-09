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

    public TowerOutput(long id, int level, int goldCost, int woodCost, int foodCost, int hpMax,int hpCurr,int attack,
    int goldCostMinion,int woodCostMinion,int foodCostMinion,int goldCostRepair,int woodCostRepair,int foodCostRepair){
        super(id,level,goldCost,woodCost,foodCost);
        this.hpMax = hpMax;
        this.hpCurr = hpCurr;
        this.attack = attack;
        this.goldCostMinion = goldCostMinion;
        this.woodCostMinion = woodCostMinion;
        this.goldCostRepair = goldCostRepair;
        this.woodCostRepair = woodCostRepair;
        this.foodCostRepair = foodCostRepair;
    }

    public TowerOutput(TowerModel tower){
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
