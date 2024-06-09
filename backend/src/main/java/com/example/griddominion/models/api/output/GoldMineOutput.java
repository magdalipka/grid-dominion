package com.example.griddominion.models.api.output;

import com.example.griddominion.models.db.GoldMineModel;

public class GoldMineOutput extends BuildingOutput {
    public double bonus;

    public GoldMineOutput(long id, int level, int goldCost, int woodCost, int foodCost, double bonus){
        super(id,level,goldCost,woodCost,foodCost);
        this.bonus = bonus;
    }

    public GoldMineOutput(GoldMineModel goldMine){
        super(goldMine);
        this.bonus = goldMine.getBonus();
    }
    
}
