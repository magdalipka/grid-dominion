package com.example.griddominion.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.example.griddominion.models.api.input.ResourcesTransferInput;
import com.example.griddominion.models.api.output.ClanOutput;
import com.example.griddominion.models.api.output.InventoryOutput;
import com.example.griddominion.services.InventoryService;
import com.example.griddominion.services.UserService;
import com.example.griddominion.utils.Headers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/inventories")
public class InventoryController {

  @Autowired
  private InventoryService inventoryService;

  @Autowired()
  private UserService userService;

  @GetMapping()
  public ResponseEntity<InventoryOutput> getMyInventory(@CookieValue("sid") String authToken) {
    var user = userService.getUserBySessionToken(authToken);
    var inventoryResponse = new InventoryOutput(user.getInventory());
    return ResponseEntity.ok(inventoryResponse);
  }

}
