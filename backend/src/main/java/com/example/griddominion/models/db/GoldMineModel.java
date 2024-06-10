package com.example.griddominion.models.db;

import com.example.griddominion.utils.Constants;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("GoldMine")
public class GoldMineModel extends ResourceBuildingModel {

  @Override
  public String getType() {
    return "Gold mine";
  }

  @Override
  public int getGoldCost() {
    return (int) Math.pow(Constants.INITIAL_GOLD_COST_GOLD_MINE,
        Math.pow(Constants.UPGRADE_COST_RESOURCE_BUILDING_MULTIPLIER, level));
  }

  @Override
  public int getWoodCost() {
    return (int) Math.pow(Constants.INITIAL_WOOD_COST_GOLD_MINE,
        Math.pow(Constants.UPGRADE_COST_RESOURCE_BUILDING_MULTIPLIER, level));
  }

  @Override
  public int getFoodCost() {
    return (int) Math.pow(Constants.INITIAL_FOOD_COST_GOLD_MINE,
        Math.pow(Constants.UPGRADE_COST_RESOURCE_BUILDING_MULTIPLIER, level));
  }

}
