package com.example.griddominion.models.api.output;

import java.util.HashMap;

import com.example.griddominion.models.db.InventoryModel;
import com.example.griddominion.utils.Item;

public class InventoryOutput {
  public Long id;
  public HashMap<Item, Integer> inventoryHashMap;

  public InventoryOutput(InventoryModel inventoryModel) {
    this.id = inventoryModel.getId();
    this.inventoryHashMap = inventoryModel.getInventory();
  }

}