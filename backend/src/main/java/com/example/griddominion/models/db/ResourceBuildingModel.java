package com.example.griddominion.models.db;

import java.util.HashMap;

import com.example.griddominion.utils.Constants;
import com.example.griddominion.utils.Item;
import com.example.griddominion.utils.errors.BadRequest;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Recource")
public abstract class ResourceBuildingModel extends BuildingModel {
  @Column(name = "bonus")
  protected double bonus;

  public double getBonus() {
    return bonus;
  }

  public void setBonus(double bonus) {
    this.bonus = bonus;
  }

  @Override
  public InventoryModel upgrade(InventoryModel inventoryModel) {
    HashMap<Item, Integer> resources = inventoryModel.getInventory();
    int gold = resources.get(Item.GOLD) - getGoldCost();
    int wood = resources.get(Item.WOOD) - getWoodCost();
    int food = resources.get(Item.FOOD) - getFoodCost();

    if (food < 0 || wood < 0 || gold < 0) {
      throw new BadRequest("Not enough resources");
    }
    resources.put(Item.GOLD, gold);
    resources.put(Item.FOOD, food);
    resources.put(Item.WOOD, wood);
    inventoryModel.setInventory(resources);
    bonus += Constants.RESOURCE_BONUS;
    level++;
    return inventoryModel;
  }

  @Override
  public void reset() {
    level = 0;
    bonus = 0;
  }

  @Override
  public InventoryModel repair(InventoryModel inventoryModel) {
      return inventoryModel;
  }
}