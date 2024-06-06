package com.example.griddominion.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.griddominion.factories.BuildingOutputFactory;
import com.example.griddominion.models.api.input.BuildingUpgradeInput;
import com.example.griddominion.models.api.output.BuildingOutput;
import com.example.griddominion.models.api.output.TowerOutput;
import com.example.griddominion.models.db.BuildingModel;
import com.example.griddominion.models.db.TerritoryModel;
import com.example.griddominion.models.db.TowerModel;
import com.example.griddominion.models.db.UserModel;
import com.example.griddominion.repositories.BuildingRepository;
import com.example.griddominion.repositories.InventoryRepository;
import com.example.griddominion.repositories.TerritoryRepository;
import com.example.griddominion.utils.errors.NotFound;
import com.example.griddominion.utils.errors.Unauthorized;

@Service
public class BuildingService {
    @Autowired
    BuildingRepository buildingRepository;
    @Autowired
    TerritoryRepository territoryRepository;
    @Autowired
    InventoryRepository inventoryRepository;

    public BuildingOutput upgradeBuilding(BuildingUpgradeInput buildingUpgradeInput){
        TerritoryModel territoryModel = territoryRepository.findById(buildingUpgradeInput.territoryId).orElse(null);
        if(territoryModel ==  null){
            throw new NotFound("There is no such trritoy");
        }
        UserModel owner = territoryModel.getOwner();
        if(owner == null || owner.getId() != buildingUpgradeInput.userId){
            throw new Unauthorized("You are not owner");
        }
        BuildingModel building = buildingRepository.findById(buildingUpgradeInput.buildingId).orElse(null);
        if(building == null || !territoryModel.getBuildings().contains(building)){
            throw new Unauthorized("That is not building from this territory");
        }
        inventoryRepository.save(building.upgrade(owner.getInventory()));
        buildingRepository.save(building);
        return BuildingOutputFactory.createOutput(building);
    }

    public BuildingOutput repairTower(BuildingUpgradeInput buildingUpgradeInput){
        TerritoryModel territoryModel = territoryRepository.findById(buildingUpgradeInput.territoryId).orElse(null);
        if(territoryModel ==  null){
            throw new NotFound("There is no such trritoy");
        }
        UserModel owner = territoryModel.getOwner();
        if(owner == null || owner.getId() != buildingUpgradeInput.userId){
            throw new Unauthorized("You are not owner");
        }
        BuildingModel building = buildingRepository.findById(buildingUpgradeInput.buildingId).orElse(null);
        if(building == null || !territoryModel.getBuildings().contains(building)){
            throw new Unauthorized("That is not building from this territory");
        }
        if(!(building instanceof TowerModel)){
            throw new Unauthorized("That is not tower");
        }
        inventoryRepository.save(building.repair(owner.getInventory()));
        buildingRepository.save(building);
        return BuildingOutputFactory.createOutput(building);
    }
}
