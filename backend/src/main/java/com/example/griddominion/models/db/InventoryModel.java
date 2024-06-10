package com.example.griddominion.models.db;

import com.example.griddominion.utils.Item;
import jakarta.persistence.*;

import java.util.HashMap;
import java.util.List;

@Entity
@Table(name = "inventories")
public class InventoryModel {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "inventoryHashMap")
  private HashMap<Item, Integer> inventoryHashMap;

  @OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<MinionModel> minions;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private UserModel user;

  public InventoryModel() {
  }

  public HashMap<Item, Integer> getInventory() {
    return inventoryHashMap;
  }

  public void setInventory(HashMap<Item, Integer> inventoryHashMap) {
    this.inventoryHashMap = inventoryHashMap;
  }

  public UserModel getUser() {
    return user;
  }

  public void setUser(UserModel user) {
    this.user = user;
  }

  public Long getId() {
    return this.id;
  }

  public List<MinionModel> getMinions() {
    return minions;
  }

  public void setMinions(List<MinionModel> minions) {
    this.minions = minions;
  }
}