package com.example.griddominion.models.api.output;

import com.example.griddominion.models.db.TowerModel;

public class TowerOutput extends BuildingOutput {
    private int hpMax;
    private int hpCurr;
    private int attack;

    public TowerOutput(long id, int level, int goldCost, int woodCost, int foodCost, int hpMax,int hpCurr,int attack){
        super(id,level,goldCost,woodCost,foodCost);
        this.hpMax = hpMax;
        this.hpCurr = hpCurr;
        this.attack = attack;
    }

    public TowerOutput(TowerModel tower){
        super(tower);
        this.hpMax = tower.getHealth();
        this.hpCurr = tower.getHealthCurrent();
        this.attack = tower.getAttack();
    }
    
}
