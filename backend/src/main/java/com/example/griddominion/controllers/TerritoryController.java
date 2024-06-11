package com.example.griddominion.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.griddominion.models.api.input.MinionDropCollectInput;
import com.example.griddominion.models.api.input.TerritoryIdInput;
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

  @PostMapping("/{territory_id}/invade")
  public ResponseEntity<FightOutput> updateOwner(@PathVariable("territory_id") Integer territoryId,
      @CookieValue("sid") String authToken) {
    var user = userService.getUserBySessionToken(authToken);
    var territory = territoryService.getById(territoryId);
    FightOutput opt = territoryService.invade(territory, user);
    return ResponseEntity.ok(opt);
  }

  @GetMapping("/buildings")
  public ResponseEntity<List<BuildingOutput>> getTerritoryBuildings(@RequestBody TerritoryIdInput input) {
    List<BuildingOutput> buildingOutputs = territoryService.getTerritoryBuildings(input);
    return ResponseEntity.ok(buildingOutputs);
  }

  @PostMapping("/{territory_id}/minions")
  public ResponseEntity<?> dropCollectMinions(@PathVariable("territory_id") Integer territoryId,
      @CookieValue("sid") String authToken, @RequestBody MinionDropCollectInput input) {
    var user = userService.getUserBySessionToken(authToken);
    var territory = territoryService.getById(territoryId);
    if (territory.getOwner() == null || territory.getOwner().getId() != user.getId()) {
      return ResponseEntity.badRequest().body("Territory does not belong to user.");
    }
    var res = territoryService.dropCollectMinions(territory, user, input);
    return ResponseEntity.ok(new TerritoryOutput(res));
  }

}
