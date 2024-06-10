package com.example.griddominion.services;


import com.example.griddominion.models.db.MinionModel;
import com.example.griddominion.models.db.TerritoryModel;
import com.example.griddominion.repositories.MinionRepository;

import com.example.griddominion.repositories.TerritoryRepository;
import com.example.griddominion.utils.Constants;
import com.example.griddominion.utils.errors.NotFound;
import com.example.griddominion.utils.errors.Unauthorized;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;



@Service
public class MinionService {
    @Autowired
    MinionRepository minionRepository;
    @Autowired
    private TerritoryRepository territoryRepository;
    @Autowired
    private UserService userService;

    public void moveMinionToTerritory(String minionId, String territoryId, String callerSid) {
        MinionModel minion = minionRepository.findById(minionId).orElseThrow(() -> new NotFound("Minion not found"));

        // Check if caller is minion's owner
        if (userService.getUserBySessionToken(callerSid).getId().equals(getMinionById(minionId).getOwner().getId()) == false) {
            throw new Unauthorized("You are not authorized to move this minion");
        }

        TerritoryModel targetTerritory = territoryRepository.findById(territoryId).orElseThrow(() -> new NotFound("Target territory not found"));

        performMove(minion, targetTerritory);
    }

    // Method to be executed after 5 minutes
    @Transactional
    @Scheduled(initialDelay = Constants.MINION_MOVE_DELAY_MS, fixedDelay=Long.MAX_VALUE)
    public void performMove(MinionModel minion, TerritoryModel targetTerritory) {
        TerritoryModel originalTerritory = minion.getTerritory();
        originalTerritory.getMinions().remove(minion);

        targetTerritory.getMinions().add(minion);

        minion.setTerritory(targetTerritory);

        territoryRepository.save(originalTerritory);
        territoryRepository.save(targetTerritory);
        minionRepository.save(minion);
    }



    public MinionModel getMinionById(String minionId) {
        MinionModel minion = this.minionRepository.findById(minionId).orElse(null);

        if (minion == null) {
            throw new NotFound("Minion not found");
        }

        return minion;
    }

}