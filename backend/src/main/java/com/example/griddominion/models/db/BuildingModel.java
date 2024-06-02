package com.example.griddominion.models.db;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@Table(name = "buildings")
public abstract class BuildingModel {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  protected Long id;

  @Column(name = "level", nullable = true)
  protected Integer level;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "territory_id", nullable = false, insertable = false, updatable = false)
  private TerritoryModel territory;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public TerritoryModel getTerritory() {
    return this.territory;
  }

  public void setTerritory(TerritoryModel territory) {
    this.territory = territory;
  }

  public abstract InventoryModel upgrade(InventoryModel inventoryModel);

  public abstract int getGoldCost();

  public abstract int getWoodCost();

  public abstract int getFoodCost();

  public abstract void reset();

}
