package com.example.griddominion.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.griddominion.models.api.input.BuildingUpgradeInput;
import com.example.griddominion.models.api.output.BuildingOutput;
import com.example.griddominion.models.api.output.MinionOutput;
import com.example.griddominion.services.BuildingService;

@RestController
@RequestMapping("/buildings")
public class BuildingController {

  @Autowired
  BuildingService buildingService;

  @PostMapping("/upgrade")
  public ResponseEntity<BuildingOutput> upgradeBuilding(@RequestBody BuildingUpgradeInput input) {
    BuildingOutput buildingOutput = buildingService.upgradeBuilding(input);
    return ResponseEntity.ok(buildingOutput);
  }

  @PostMapping("/repair")
  public ResponseEntity<BuildingOutput> repairBuilding(@RequestBody BuildingUpgradeInput input) {
    BuildingOutput buildingOutput = buildingService.repairTower(input);
    return ResponseEntity.ok(buildingOutput);
  }

  @PostMapping("/tower/minion")
  public ResponseEntity<MinionOutput> creteMinion(@RequestBody BuildingUpgradeInput input) {
    MinionOutput minionOutput = buildingService.createMinionInTower(input);
    return ResponseEntity.ok(minionOutput);
  }
}
