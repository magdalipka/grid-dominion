package com.example.griddominion.models.db;

import java.util.HashMap;

import com.example.griddominion.utils.Constants;
import com.example.griddominion.utils.Item;
import com.example.griddominion.utils.errors.BadRequest;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Tower")
public class TowerModel extends BuildingModel {

    @Column(name = "hp_max")
    private int healthMax;

    @Column(name = "hp_current")
    private int healthCurrent;

    @Column(name = "attack")
    private int attack;

    public int getHealth() {
        return healthMax;
    }

    public void setHealth(int health) {
        this.healthMax = health;
    }

    public int getHealthCurrent() {
        return healthCurrent;
    }

    public void setHealthCurrent(int healthCurrent) {
        this.healthCurrent = healthCurrent;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    @Override
    public InventoryModel upgrade(InventoryModel inventoryModel) {
         HashMap<Item,Integer> resources = inventoryModel.getInventory();
        int gold =  resources.get(Item.GOLD)-getGoldCost();
        int wood = resources.get(Item.WOOD)-getWoodCost();
        int food = resources.get(Item.FOOD)-getFoodCost();

        if(food < 0 || wood < 0 || gold < 0){
            throw new BadRequest("Not enough resources");
        }
        resources.put(Item.GOLD, gold);
        resources.put(Item.FOOD, food);
        resources.put(Item.WOOD, wood);
        inventoryModel.setInventory(resources);
        healthMax = Constants.BASE_HP_MAX_TOWER * (int)Math.pow(2,level);
        healthCurrent = Constants.BASE_HP_MAX_TOWER * (int)Math.pow(2,level);
        attack =  Constants.BASE_ATTACK_TOWER *(int)Math.pow(2,level);
        level++;
        return inventoryModel;
    }

    @Override
    public int getGoldCost() {
        return (int)Math.pow(Constants.INITIAL_GOLD_COST_TOWER,Math.pow(Constants.UPGRADE_COST_TOWER_MULTIPLIER,level));

    }

    @Override
    public int getWoodCost() {
        return (int)Math.pow(Constants.INITIAL_WOOD_COST_TOWER,Math.pow(Constants.UPGRADE_COST_TOWER_MULTIPLIER,level));

    }

    @Override
    public int getFoodCost() {
        return (int)Math.pow(Constants.INITIAL_FOOD_COST_TOWER,Math.pow(Constants.UPGRADE_COST_TOWER_MULTIPLIER,level));

    }

    @Override
    public void reset(){
        attack = 0;
        healthCurrent = 0;
        healthMax = 0;
        level = 0;
    }
    
}
