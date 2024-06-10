package com.example.griddominion.models.api.output;

import com.example.griddominion.models.db.BuildingModel;

public abstract class BuildingOutput {
  public long id;
  public Integer level;
  public Integer goldCost;
  public Integer woodCost;
  public Integer foodCost;
  public String type;

  public BuildingOutput(BuildingModel buildingModel) {
    this.id = buildingModel.getId();
    this.level = buildingModel.getLevel();
    this.goldCost = buildingModel.getGoldCost();
    this.woodCost = buildingModel.getWoodCost();
    this.foodCost = buildingModel.getFoodCost();
    this.type = buildingModel.getType();
  }
}
