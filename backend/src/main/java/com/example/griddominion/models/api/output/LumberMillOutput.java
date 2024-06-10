package com.example.griddominion.models.api.output;

import com.example.griddominion.models.db.LumberMillModel;

public class LumberMillOutput extends BuildingOutput {
  public double bonus;

  public LumberMillOutput(LumberMillModel lumberMill) {
    super(lumberMill);
    this.bonus = lumberMill.getBonus();
  }

}