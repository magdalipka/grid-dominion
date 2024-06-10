package com.example.griddominion.controllers;


import com.example.griddominion.repositories.MinionRepository;
import com.example.griddominion.services.MinionService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/minions")
public class MinionController {
    @Autowired()
    private MinionService minionService;
    @Autowired()
    private MinionRepository minionRepository;


    @PutMapping("/{minionId}/moveTo/{territoryId}")
    public void moveMinionToTerritory(@PathVariable String minionId, @PathVariable String territoryId, @CookieValue(value = "sid") String sid) {
        minionService.moveMinionToTerritory(minionId, territoryId, sid);
    }



}