package com.example.griddominion.models.api.output;

import com.example.griddominion.models.db.FarmModel;

public class FarmOutput extends BuildingOutput {
    private double bonus;

    public FarmOutput(long id, int level, int goldCost, int woodCost, int foodCost, double bonus){
        super(id,level,goldCost,woodCost,foodCost);
        this.bonus = bonus;
    }

    public FarmOutput(FarmModel farm){
        super(farm);
        this.bonus = farm.getBonus();
    }
    
}