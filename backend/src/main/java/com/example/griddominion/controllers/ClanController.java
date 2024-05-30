package com.example.griddominion.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.example.griddominion.models.api.input.ClanCreationInput;
import com.example.griddominion.models.api.input.ResourcesTransferInput;
import com.example.griddominion.models.api.output.ClanOutput;
import com.example.griddominion.services.ClanService;
import com.example.griddominion.utils.Headers;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clans")
public class ClanController {

    @Autowired
    private ClanService clanService;

    @PostMapping()
    public ResponseEntity<ClanOutput> createClan(@RequestBody ClanCreationInput input) {
        var clan = clanService.createClan(input);
        var clanOutput = new ClanOutput(clan);
        return ResponseEntity.ok().headers(
                new Headers().addSid(clan.getId())).body(clanOutput);
    }

    @GetMapping()
    public ResponseEntity<List<ClanOutput>> getAllClans() {
        var clans = clanService.getAllClans();
        List<ClanOutput> clanOutputs = clans.stream()
                .map(ClanOutput::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clanOutputs);
    }

    @PostMapping("/sendResources")
    public ResponseEntity<?> sendResources(@RequestBody ResourcesTransferInput resourcesTransferInput) {
            clanService.sendResources(resourcesTransferInput);
            return ResponseEntity.ok().body("Resources transferred successfully.");
    }
}
