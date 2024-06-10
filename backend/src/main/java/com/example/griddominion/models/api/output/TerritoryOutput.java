package com.example.griddominion.models.api.output;

import java.util.List;
import java.util.stream.Collectors;

import com.example.griddominion.factories.BuildingOutputFactory;
import com.example.griddominion.models.db.TerritoryModel;

public class TerritoryOutput {
  public Integer id;
  public double maxLatitude;
  public double minLatitude;
  public double maxLongitude;
  public double minLongitude;
  public Integer gold;
  public Integer wood;
  public Integer food;
  public String ownerNick;
  public List<BuildingOutput> buildings;

  public TerritoryOutput(TerritoryModel territoryModel) {
    this.id = territoryModel.getId();
    this.maxLatitude = territoryModel.getMaxLatitude();
    this.minLatitude = territoryModel.getMinLatitude();
    this.maxLongitude = territoryModel.getMaxLongitude();
    this.minLongitude = territoryModel.getMinLongitude();
    this.gold = territoryModel.getGold();
    this.wood = territoryModel.getWood();
    this.food = territoryModel.getFood();
    this.ownerNick = (territoryModel.getOwner() != null) ? territoryModel.getOwner().getNick() : null;
    this.buildings = territoryModel.getBuildings().stream()
        .map(building -> BuildingOutputFactory.createOutput(building))
        .collect(Collectors.toList());
  }
}
