package com.example.griddominion.models.api.output;

import com.example.griddominion.models.db.LumberMillModel;

public class LumberMillOutput extends BuildingOutput {
    private double bonus;

    public LumberMillOutput(long id, int level, int goldCost, int woodCost, int foodCost, double bonus){
        super(id,level,goldCost,woodCost,foodCost);
        this.bonus = bonus;
    }

    public LumberMillOutput(LumberMillModel lumberMill){
        super(lumberMill);
        this.bonus = lumberMill.getBonus();
    }
    
}