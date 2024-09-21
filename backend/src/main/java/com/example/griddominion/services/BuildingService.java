package com.example.griddominion.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.griddominion.factories.BuildingOutputFactory;
import com.example.griddominion.models.api.input.BuildingUpgradeInput;
import com.example.griddominion.models.api.output.BuildingOutput;
import com.example.griddominion.models.api.output.MinionOutput;
import com.example.griddominion.models.db.BuildingModel;
import com.example.griddominion.models.db.InventoryModel;
import com.example.griddominion.models.db.MinionModel;
import com.example.griddominion.models.db.TerritoryModel;
import com.example.griddominion.models.db.TowerModel;
import com.example.griddominion.models.db.UserModel;
import com.example.griddominion.repositories.BuildingRepository;
import com.example.griddominion.repositories.InventoryRepository;
import com.example.griddominion.repositories.MinionRepository;
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
    @Autowired
    MinionRepository minionRepository;

    @Transactional
    public BuildingOutput upgradeBuilding(BuildingUpgradeInput buildingUpgradeInput){
        TerritoryModel territoryModel = territoryRepository.findById(buildingUpgradeInput.territoryId).orElse(null);
        if(territoryModel ==  null){
            throw new NotFound("There is no such trritoy");
        }
        UserModel owner = territoryModel.getOwner();
        if(owner == null || owner.getId() != buildingUpgradeInput.userId){
            //throw new Unauthorized("You are not owner");
        }
        BuildingModel building = buildingRepository.findById(buildingUpgradeInput.buildingId).orElse(null);
        if(building == null || !territoryModel.getBuildings().contains(building)){
            //throw new Unauthorized("That is not building from this territory");
        }
        inventoryRepository.save(building.upgrade(owner.getInventory()));
        buildingRepository.save(building);
        return BuildingOutputFactory.createOutput(building);
    }

    @Transactional
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

    @Transactional
    public MinionOutput createMinionInTower(BuildingUpgradeInput input) {
        TerritoryModel territoryModel = territoryRepository.findById(input.territoryId).orElse(null);
        if (territoryModel == null) {
            throw new NotFound("There is no such territory");
        }
        UserModel owner = territoryModel.getOwner();
        if (owner == null || !owner.getId().equals(input.userId)) {
            throw new Unauthorized("You are not the owner of this territory");
        }
        TowerModel tower = (TowerModel) buildingRepository.findById(input.buildingId)
                .filter(building -> building instanceof TowerModel)
                .orElseThrow(() -> new NotFound("Tower not found"));

        Pair<MinionModel, InventoryModel> creationResult = tower.createMinion(owner.getInventory());
        MinionModel minion = creationResult.getFirst();
        InventoryModel updatedInventory = creationResult.getSecond();

        inventoryRepository.save(updatedInventory);
        minionRepository.save(minion);

        return new MinionOutput(minion);
    }
}
