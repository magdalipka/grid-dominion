package com.example.griddominion.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.griddominion.models.api.input.TerritoryIdInput;
import com.example.griddominion.models.api.input.TerritoryOwnerInput;
import com.example.griddominion.models.api.output.BuildingOutput;
import com.example.griddominion.models.api.output.FightOutput;
import com.example.griddominion.models.api.output.TerritoryOutput;
import com.example.griddominion.models.api.output.TerritoryOwnerOutput;
import com.example.griddominion.services.TerritoryService;
import com.example.griddominion.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/territories")
public class TerritoryController {

  @Autowired
  TerritoryService territoryService;
  @Autowired
  private UserService userService;

  @GetMapping()
  public ResponseEntity<List<TerritoryOutput>> getAllTerritories() {
    List<TerritoryOutput> territories = territoryService.getAllTerritories();
    return ResponseEntity.ok(territories);
  }

  @GetMapping("/owners")
  public ResponseEntity<List<TerritoryOwnerOutput>> getAllTerritoryOwners() {
    List<TerritoryOwnerOutput> owners = territoryService.getAllTerritoryOwners();
    return ResponseEntity.ok(owners);
  }

  @PostMapping("/owner")
  public ResponseEntity<FightOutput> updateOwner(@RequestBody TerritoryOwnerInput input,
      @CookieValue("sid") String authToken) {
    var user = userService.getUserBySessionToken(authToken);
    FightOutput opt = territoryService.upddateOwner(input, user);
    return ResponseEntity.ok(opt);
  }

  @GetMapping("/buildings")
  public ResponseEntity<List<BuildingOutput>> getTerritoryBuildings(@RequestBody TerritoryIdInput input) {
    List<BuildingOutput> buildingOutputs = territoryService.getTerritoryBuildings(input);
    return ResponseEntity.ok(buildingOutputs);
  }
}
