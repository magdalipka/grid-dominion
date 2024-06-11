package com.example.griddominion.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.griddominion.factories.BuildingOutputFactory;
import com.example.griddominion.models.api.input.MinionDropCollectInput;
import com.example.griddominion.models.api.input.TerritoryIdInput;
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
import com.example.griddominion.repositories.BuildingRepository;
import com.example.griddominion.repositories.InventoryRepository;
import com.example.griddominion.repositories.TerritoryRepository;
import com.example.griddominion.repositories.UserRepository;
import com.example.griddominion.utils.Constants;
import com.example.griddominion.utils.Item;
import com.example.griddominion.utils.errors.NotFound;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
public class TerritoryService {

  Logger logger = LoggerFactory.getLogger(UserService.class);

  @Autowired
  TerritoryRepository territoryRepository;
  @Autowired
  UserRepository userRepository;
  @Autowired
  InventoryRepository inventoryRepository;
  @Autowired
  BuildingRepository buildingRepository;

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
  @Scheduled(cron = "0 * * * * ?")
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
        TowerModel tower = (TowerModel) buildings.stream().filter(building -> building instanceof TowerModel)
            .findFirst()
            .orElse(null);
        InventoryModel inventory = owner.getInventory();
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
        if (tower != null) {
          items.put(Item.MINIONS,
              (int) Math.min(Constants.RESOURCE_LIMIT, current * 1.2));
        }
        inventory.setInventory(items);
        inventoryRepository.save(inventory);
      }
    }
  }

  public FightOutput invade(TerritoryModel territory, UserModel user) {

    var inventory = user.getInventory();
    var items = inventory.getInventory();

    if (territory.getOwner() == null) {
      territory.setOwner(user);
      var territoryres = territoryRepository.save(territory);
      return new FightOutput(true, territoryres.getMinions(), inventory.getInventory().get(Item.MINIONS));
    }

    var minions = items.get(Item.MINIONS);
    if (minions == null) {
      minions = 0;
    }

    TowerModel tower = (TowerModel) territory.getBuildings().stream()
        .filter(building -> building instanceof TowerModel).findFirst()
        .orElse(null);

    var multiplier = tower != null ? (tower.getLevel()) + 1 : 1;

    if (territory.getMinions() * multiplier >= minions) {
      items.put(Item.MINIONS, 0);
      var inventoryres = inventoryRepository.save(inventory);
      territory.setMinions(territory.getMinions() - (int) (minions / multiplier));
      var territoryres = territoryRepository.save(territory);

      return new FightOutput(false, territoryres.getMinions(), inventoryres.getInventory().get(Item.MINIONS));
    } else {
      territory.setMinions(0);
      territory.setOwner(user);
      var territoryres = territoryRepository.save(territory);
      items.put(Item.MINIONS, minions - (territory.getMinions() * multiplier));
      var inventoryres = inventoryRepository.save(inventory);
      return new FightOutput(true, territoryres.getMinions(), inventoryres.getInventory().get(Item.MINIONS));
    }

  }

  public TerritoryModel getById(Integer id) {
    var territory = territoryRepository.findById(id);
    this.logger.warn(territory.toString());
    return territory.orElse(null);
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

  public TerritoryModel dropCollectMinions(TerritoryModel territory, UserModel user, MinionDropCollectInput input) {
    var inventory = user.getInventory();
    var items = inventory.getInventory();

    var transfer = input.collect - input.drop;

    items.put(Item.MINIONS, items.get(Item.MINIONS) + transfer);
    territory.setMinions(territory.getMinions() - transfer);

    inventoryRepository.save(inventory);
    var res = territoryRepository.save(territory);

    return res;
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

}
