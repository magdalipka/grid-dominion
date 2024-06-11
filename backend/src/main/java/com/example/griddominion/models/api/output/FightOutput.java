package com.example.griddominion.models.api.output;

public class FightOutput {
  public boolean win;
  public int territoryMinions;
  public int inventoryMinions;

  public FightOutput(boolean win, Integer territory, Integer inventory) {
    this.win = win;
    territoryMinions = territory;
    inventoryMinions = inventory;
  }

}
