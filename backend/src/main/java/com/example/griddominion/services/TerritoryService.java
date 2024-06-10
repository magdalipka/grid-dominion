package com.example.griddominion.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.griddominion.factories.BuildingOutputFactory;
import com.example.griddominion.models.api.input.TerritoryIdInput;
import com.example.griddominion.models.api.input.TerritoryOwnerInput;
import com.example.griddominion.models.api.output.BuildingOutput;
import com.example.griddominion.models.api.output.FightOutput;
import com.example.griddominion.models.api.output.TerritoryOutput;
import com.example.griddominion.models.api.output.TerritoryOwnerOutput;
import com.example.griddominion.models.db.TerritoryModel;
import com.example.griddominion.models.db.TowerModel;
import com.example.griddominion.models.db.UserModel;
import com.example.griddominion.models.db.BuildingModel;
import com.example.griddominion.models.db.FarmModel;
import com.example.griddominion.models.db.GoldMineModel;
import com.example.griddominion.models.db.InventoryModel;
import com.example.griddominion.models.db.LumberMillModel;
import com.example.griddominion.models.db.MinionModel;
import com.example.griddominion.repositories.BuildingRepository;
import com.example.griddominion.repositories.InventoryRepository;
import com.example.griddominion.repositories.MinionRepository;
import com.example.griddominion.repositories.TerritoryRepository;
import com.example.griddominion.repositories.UserRepository;
import com.example.griddominion.utils.Constants;
import com.example.griddominion.utils.Item;
import com.example.griddominion.utils.errors.NotFound;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
public class TerritoryService {
  @Autowired
  TerritoryRepository territoryRepository;
  @Autowired
  UserRepository userRepository;
  @Autowired
  InventoryRepository inventoryRepository;
  @Autowired
  BuildingRepository buildingRepository;
  @Autowired
  MinionRepository minionRepository;

  @PostConstruct
  public void initTerritories() {
    if (territoryRepository.findAll().isEmpty()) {
      // https://krakow.stat.gov.pl/cps/rde/xbcr/krak/ASSETS_07m01_01.pdf
      double north_most = 50.117;
      double south_most = 49.966;
      double west_most = 19.783;
      double east_most = 20.217;

      int squares = 50;
      double diff_latitude = (north_most - south_most) / squares;
      double diff_longitude = (east_most - west_most) / squares;

      Random random = new Random();

      for (int i = 0; i < squares; i++) {
        for (int j = 0; j < squares; j++) {
          TerritoryModel territoryModel = new TerritoryModel();
          territoryModel.setMinLatitude(south_most + i * diff_latitude);
          territoryModel.setMaxLatitude(south_most + ((i + 1) * diff_latitude));
          territoryModel.setMinLongitude(west_most + j * diff_longitude);
          territoryModel.setMaxLongitude(west_most + (j + 1) * diff_longitude);
          territoryModel.setGold(100 + random.nextInt(901));
          territoryModel.setWood(100 + random.nextInt(901));
          territoryModel.setFood(100 + random.nextInt(901));
          territoryRepository.save(territoryModel);

          var buildings = generateBuildings();
          for (var b : buildings) {
            b.setTerritory(territoryModel);
            buildingRepository.save(b);
          }
        }
      }
    }
  }

  private List<BuildingModel> generateBuildings() {
    List<BuildingModel> buildings = new ArrayList<BuildingModel>();

    GoldMineModel goldMine = new GoldMineModel();
    goldMine.setLevel(0);
    buildings.add(goldMine);

    LumberMillModel lumberMill = new LumberMillModel();
    lumberMill.setLevel(0);
    buildings.add(lumberMill);

    FarmModel farm = new FarmModel();
    farm.setLevel(0);
    buildings.add(farm);

    TowerModel tower = new TowerModel();
    tower.setLevel(0);
    buildings.add(tower);

    return buildings;
  }

  @Transactional
  @Scheduled(cron = "0 0/10 * * * ?")
  public void addResources() {
    List<TerritoryModel> territories = territoryRepository.findAll();

    for (TerritoryModel territory : territories) {
      UserModel owner = territory.getOwner();
      if (owner != null) {
        List<BuildingModel> buildings = territory.getBuildings();
        GoldMineModel goldMine = (GoldMineModel) buildings.stream()
            .filter(building -> building instanceof GoldMineModel).findFirst()
            .orElseThrow(() -> new NotFound("No gold mine"));
        LumberMillModel lumberMill = (LumberMillModel) buildings.stream()
            .filter(building -> building instanceof LumberMillModel).findFirst()
            .orElseThrow(() -> new NotFound("No lumber mill"));
        FarmModel farm = (FarmModel) buildings.stream().filter(building -> building instanceof FarmModel).findFirst()
            .orElseThrow(() -> new NotFound("No farm"));
        InventoryModel inventory = inventoryRepository.findByUserId(owner);
        HashMap<Item, Integer> items = inventory.getInventory();
        Integer current = items.get(Item.FOOD);
        items.put(Item.FOOD,
            (int) Math.min(Constants.RESOURCE_LIMIT, current + territory.getFood() * (farm.getBonus() + 1)));
        current = items.get(Item.WOOD);
        items.put(Item.WOOD,
            (int) Math.min(Constants.RESOURCE_LIMIT, current + territory.getWood() * (lumberMill.getBonus() + 1)));
        current = items.get(Item.GOLD);
        items.put(Item.GOLD,
            (int) Math.min(Constants.RESOURCE_LIMIT, current + territory.getGold() * (goldMine.getBonus() + 1)));
        inventory.setInventory(items);
        inventoryRepository.save(inventory);
      }
    }
  }

  public FightOutput upddateOwner(TerritoryOwnerInput territoryOwnerInput, UserModel user) {
    TerritoryModel territory = territoryRepository.findById(territoryOwnerInput.Id).get();
    if (user == null || territory == null)
      throw new NotFound("There is no such user or territory");
    TowerModel tower = (TowerModel) territory.getBuildings().stream()
        .filter(building -> building instanceof TowerModel).findFirst()
        .orElseThrow(() -> new NotFound("Tower not found"));
    List<MinionModel> attackers = user.getMinions();
    List<MinionModel> defenders = territory.getMinions();
    FightOutput opt = fight(attackers, defenders, tower);
    if (opt.win) {
      territory.setOwner(user);
      for (BuildingModel building : territory.getBuildings()) {
        building.reset();
        buildingRepository.save(building);
      }
      territoryRepository.save(territory);
    }
    return opt;

  }

  public List<TerritoryOutput> getAllTerritories() {
    List<TerritoryModel> territories = territoryRepository.findAll();
    return territories.stream()
        .map(territory -> new TerritoryOutput(territory))
        .collect(Collectors.toList());
  }

  public List<TerritoryOwnerOutput> getAllTerritoryOwners() {
    List<TerritoryModel> territories = territoryRepository.findAll();
    return territories.stream()
        .map(territory -> new TerritoryOwnerOutput(territory))
        .collect(Collectors.toList());
  }

  /*
   * public void upddateOwner(TerritoryOwnerInput territoryOwnerInput) {
   * UserModel user = userRepository.findById(territoryOwnerInput.userId).get();
   * TerritoryModel territory =
   * territoryRepository.findById(territoryOwnerInput.Id).get();
   * territory.setOwner(user);
   * territoryRepository.save(territory);
   * }
   */

  public List<BuildingOutput> getTerritoryBuildings(TerritoryIdInput territoryIdInput) {
    TerritoryModel territoryModel = territoryRepository.findById(territoryIdInput.id).orElse(null);
    if (territoryModel == null) {
      throw new NotFound("No such territory");
    }
    List<BuildingModel> buildingModels = territoryModel.getBuildings();
    List<BuildingOutput> buildingOutputs = new ArrayList<>();
    for (BuildingModel model : buildingModels) {
      buildingOutputs.add(BuildingOutputFactory.createOutput(model));
    }
    return buildingOutputs;
  }

  private FightOutput fight(List<MinionModel> atackers, List<MinionModel> defenders, TowerModel tower) {
    while (!atackers.isEmpty() && !defenders.isEmpty()) {

      MinionModel defender = defenders.isEmpty() ? null : defenders.get(0);
      for (MinionModel atackerATK : atackers) {
        defender.setHp(defender.getHp() - atackerATK.getAttackDamage());
        if (defender.getHp() <= 0) {
          minionRepository.delete(defender);
          defenders.remove(defender);
          defender = defenders.isEmpty() ? null : defenders.get(0);
          if (defender == null)
            break;
        }
      }

      MinionModel atacker = atackers.isEmpty() ? null : atackers.get(0);
      for (MinionModel defenderATK : defenders) {
        atacker.setHp(atacker.getHp() - defenderATK.getAttackDamage());
        if (atacker.getHp() <= 0) {
          minionRepository.delete(atacker);
          atackers.remove(atacker);
          atacker = atackers.isEmpty() ? null : atackers.get(0);
          if (atacker == null) {
            minionRepository.save(defender);
            buildingRepository.save(tower);
            return new FightOutput(false, defenders, tower);
          }
        }
      }

      atacker.setHp(atacker.getHp() - tower.getAttack());
      if (atacker.getHp() <= 0) {
        minionRepository.delete(atacker);
        atackers.remove(atacker);
      }

    }

    while (!atackers.isEmpty() && tower.getHealthCurrent() > 0) {
      for (MinionModel atackerATK : atackers) {
        tower.setHealthCurrent(tower.getHealthCurrent() - (int) atackerATK.getAttackDamage());
        if (tower.getHealth() <= 0)
          return new FightOutput(true, atackers, null);
      }
      MinionModel atacker = atackers.isEmpty() ? null : atackers.get(0);
      atacker.setHp(atacker.getHp() - tower.getAttack());
      if (atacker.getHp() <= 0) {
        minionRepository.delete(atacker);
        atackers.remove(atacker);
      }
    }

    if (tower.getHealthCurrent() > 0) {
      buildingRepository.save(tower);
      return new FightOutput(false, defenders, tower);
    }
    if (atackers.size() != 0) {
      minionRepository.save(atackers.get(0));
    }
    return new FightOutput(true, atackers, null);
  }

}
