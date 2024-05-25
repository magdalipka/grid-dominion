package com.example.griddominion.models.api.output;

import com.example.griddominion.models.db.TerritoryModel;

public class TerritoryOutput {
    public int id;
    public double maxLatitude;
    public double minLatitude;
    public double maxLongitude;
    public double minLongitude;
    public int gold;
    public int wood;
    public int food;
    public String ownerNick;
    public TerritoryOutput(int id, double maxLatitude, double minLatitude, double maxLongitude, double minLongitude,
            int gold, int wood, int food, String ownerNick) {
        this.id = id;
        this.maxLatitude = maxLatitude;
        this.minLatitude = minLatitude;
        this.maxLongitude = maxLongitude;
        this.minLongitude = minLongitude;
        this.gold = gold;
        this.wood = wood;
        this.food = food;
        this.ownerNick = ownerNick;
    }
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
    }
}
