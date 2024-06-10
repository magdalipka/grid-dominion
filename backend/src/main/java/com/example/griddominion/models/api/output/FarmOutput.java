package com.example.griddominion.models.api.output;

import com.example.griddominion.models.db.FarmModel;

public class FarmOutput extends BuildingOutput {
  public double bonus;

  public FarmOutput(FarmModel farm) {
    super(farm);
    this.bonus = farm.getBonus();
  }

}