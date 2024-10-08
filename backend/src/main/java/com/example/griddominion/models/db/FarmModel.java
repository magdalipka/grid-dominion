package com.example.griddominion.models.db;

import com.example.griddominion.utils.Constants;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Farm")
public class FarmModel extends ResourceBuildingModel {

  @Override
  public String getType() {
    return "Farm";
  }

  @Override
  public int getGoldCost() {
    return (int) Math.pow(Constants.INITIAL_GOLD_COST_FARM,
        Math.pow(Constants.UPGRADE_COST_RESOURCE_BUILDING_MULTIPLIER, level));
  }

  @Override
  public int getWoodCost() {
    return (int) Math.pow(Constants.INITIAL_WOOD_COST_FARM,
        Math.pow(Constants.UPGRADE_COST_RESOURCE_BUILDING_MULTIPLIER, level));
  }

  @Override
  public int getFoodCost() {
    return (int) Math.pow(Constants.INITIAL_FOOD_COST_FARM,
        Math.pow(Constants.UPGRADE_COST_RESOURCE_BUILDING_MULTIPLIER, level));
  }

}
