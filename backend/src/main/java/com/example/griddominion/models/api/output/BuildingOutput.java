package com.example.griddominion.models.api.output;

import com.example.griddominion.models.db.BuildingModel;

public abstract class BuildingOutput {
    protected long id;
    protected int level;
    protected int goldCost;
    protected int woodCost;
    protected int foodCost;
    public BuildingOutput(long id, int level, int goldCost, int woodCost, int foodCost) {
        this.id = id;
        this.level = level;
        this.goldCost = goldCost;
        this.woodCost = woodCost;
        this.foodCost = foodCost;
    }
    public BuildingOutput(BuildingModel buildingModel){
        this.id = buildingModel.getId();
        this.level = buildingModel.getLevel();
        this.goldCost = buildingModel.getGoldCost();
        this.woodCost = buildingModel.getWoodCost();
        this.foodCost = buildingModel.getFoodCost();
    }
}
