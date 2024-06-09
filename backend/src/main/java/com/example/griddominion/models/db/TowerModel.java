package com.example.griddominion.models.db;

import java.util.HashMap;
import java.util.UUID;

import org.springframework.data.util.Pair;

import com.example.griddominion.utils.Constants;
import com.example.griddominion.utils.Item;
import com.example.griddominion.utils.errors.BadRequest;
import com.example.griddominion.utils.errors.Forbidden;

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
    public InventoryModel repair(InventoryModel inventoryModel) {
        HashMap<Item,Integer> resources = inventoryModel.getInventory();
       int gold =  resources.get(Item.GOLD)-getGoldCostRepair();
       int wood = resources.get(Item.WOOD)-getWoodCostRepair();
       int food = resources.get(Item.FOOD)-getFoodCostRepair();

       if(food < 0 || wood < 0 || gold < 0){
           throw new BadRequest("Not enough resources");
       }
       resources.put(Item.GOLD, gold);
       resources.put(Item.FOOD, food);
       resources.put(Item.WOOD, wood);
       inventoryModel.setInventory(resources);
       healthCurrent = healthMax;
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

    public int getGoldCostRepair() {
        return (int)(Math.pow(Constants.INITIAL_GOLD_COST_TOWER,Math.pow(Constants.UPGRADE_COST_TOWER_MULTIPLIER,level-1))*getHealthMiss());

    }

    public int getWoodCostRepair() {
        return (int)(Math.pow(Constants.INITIAL_WOOD_COST_TOWER,Math.pow(Constants.UPGRADE_COST_TOWER_MULTIPLIER,level-1))*getHealthMiss());

    }

    public int getFoodCostRepair() {
        return (int)(Math.pow(Constants.INITIAL_FOOD_COST_TOWER,Math.pow(Constants.UPGRADE_COST_TOWER_MULTIPLIER,level-1))*getHealthMiss());

    }

    private double getHealthMiss(){
        return 1.-(double)healthCurrent/(double)healthMax;
    }

    @Override
    public void reset(){
        attack = 0;
        healthCurrent = 0;
        healthMax = 0;
        level = 0;
    }
    public Pair<MinionModel, InventoryModel> createMinion(InventoryModel inventoryModel) {
        if(level == 0){
            throw new Forbidden("Tower must have some level");
        }
        if (!canCreateMinion(inventoryModel)) {
            throw new BadRequest("Not enough resources to create a minion");
        }


        HashMap<Item, Integer> resources = inventoryModel.getInventory();
        resources.put(Item.GOLD, resources.get(Item.GOLD) - getMinionGoldCost());
        resources.put(Item.WOOD, resources.get(Item.WOOD) - getMinionWoodCost());
        resources.put(Item.FOOD, resources.get(Item.FOOD) - getMinionFoodCost());
        inventoryModel.setInventory(resources);


        MinionModel minion = new MinionModel();
        minion.setId(UUID.randomUUID().toString());
        minion.setLevel(1); 
        minion.setExperience(0); 
        minion.setExperienceToLevelUp(50); 
        minion.setMovementSpeed(5.0); 
        minion.setAttackDamage(150); 
        minion.setAttackSpeed(1.0); 
        minion.setHp(600.0);
        minion.setOwner(this.getTerritory().getOwner()); 
        minion.setTerritory(this.getTerritory()); 

        return Pair.of(minion, inventoryModel);
    }

    private boolean canCreateMinion(InventoryModel inventoryModel) {
        HashMap<Item, Integer> resources = inventoryModel.getInventory();
        return resources.get(Item.GOLD) >= getMinionGoldCost() &&
               resources.get(Item.WOOD) >= getMinionWoodCost() &&
               resources.get(Item.FOOD) >= getMinionFoodCost();
    }

    public int getMinionGoldCost() {
        return (int)(Constants.MINION_GOLD_COST * (1 - 0.05*(level-1)));
    }

    public int getMinionWoodCost() {
        return (int)(Constants.MINION_WOOD_COST * (1 - 0.05*(level-1)));
    }

    public int getMinionFoodCost() {
        return (int)(Constants.MINION_FOOD_COST * (1 - 0.05*(level-1)));
    }
    
}
