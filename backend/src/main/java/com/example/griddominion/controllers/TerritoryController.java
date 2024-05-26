package com.example.griddominion.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.griddominion.models.api.input.TerritoryOwnerInput;
import com.example.griddominion.models.api.output.TerritoryOutput;
import com.example.griddominion.models.api.output.TerritoryOwnerOutput;
import com.example.griddominion.services.TerritoryService;
import com.example.griddominion.utils.Headers;

import java.util.List;

@RestController
@RequestMapping("/territories")
public class TerritoryController {

    @Autowired
    TerritoryService territoryService;
    
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
    public ResponseEntity<Void> updateOwner(@RequestBody TerritoryOwnerInput input) {
        territoryService.upddateOwner(input);
        return ResponseEntity.ok().headers(new Headers().addSid("")).build();
    }
}
