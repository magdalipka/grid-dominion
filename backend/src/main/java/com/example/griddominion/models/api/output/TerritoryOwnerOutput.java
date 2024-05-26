package com.example.griddominion.models.api.output;

import com.example.griddominion.models.db.TerritoryModel;

public class TerritoryOwnerOutput {
    public int id;
    public String ownerNick;
    public TerritoryOwnerOutput(int id, String ownerNick) {
        this.id = id;
        this.ownerNick = ownerNick;
    }
    public TerritoryOwnerOutput(TerritoryModel territoryModel) {
        this.id = territoryModel.getId();
        this.ownerNick = (territoryModel.getOwner() != null) ? territoryModel.getOwner().getNick() : null;
    }    
}