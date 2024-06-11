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

import java.util.List;


@Service
public class MinionService {
    @Autowired
    MinionRepository minionRepository;
    @Autowired
    private TerritoryRepository territoryRepository;
    @Autowired
    private UserService userService;

    public void setDestination(String minionId, Integer territoryId, String callerSid) {
        MinionModel minion = minionRepository.findById(minionId).orElseThrow(() -> new NotFound("Minion not found"));

        // Check if caller is minion's owner
        if (userService.getUserBySessionToken(callerSid).getId().equals(getMinionById(minionId).getOwner().getId()) == false) {
            throw new Unauthorized("You are not authorized to move this minion");
        }

        TerritoryModel targetTerritory = territoryRepository.findById(territoryId).orElseThrow(() -> new NotFound("Target territory not found"));

        minion.setDestinationTerritory(targetTerritory);
    }


    @Transactional
    @Scheduled(fixedRate = Constants.MINIONS_STEP_TICK_MS)
    public void performStepForAll() {
        List<MinionModel> allMinions = minionRepository.findAll();

        for (MinionModel minion : allMinions) {
            TerritoryModel currentTerritory = minion.getTerritory();
            TerritoryModel destinationTerritory = minion.getDestinationTerritory();

            // Check if the minion has a destination territory and it's not the same as the current territory
            if (destinationTerritory != null && !currentTerritory.equals(destinationTerritory)) {
                // Calculate the direction towards the destination territory
                int dx = (destinationTerritory.getId() % 50) - (currentTerritory.getId() % 50);
                int dy = (destinationTerritory.getId() / 50) - (currentTerritory.getId() / 50);
                int d;

                // Move the minion to an adjacent territory closer to the destination
                if (Math.abs(dx) > Math.abs(dy)) {
                    // Move in the x-axis direction
                    if (dx > 0) {
                        // Move right
                        d = 1;
                    } else {
                        // Move left
                        d = -1;
                    }
                    if(getTerritoryById(currentTerritory.getId() + d).getOwner() != null) {
                        if (getTerritoryById(currentTerritory.getId() + d).getOwner().getId().equals(minion.getOwner().getId()) == false){
                            // Move in the y-axis direction
                            if (dy > 0) {
                                // Move down
                                d = 50;
                            } else {
                                // Move up
                                d = -50;
                            }
                        }
                    }
                } else {
                    // Move in the y-axis direction
                    if (dy > 0) {
                        // Move down
                        d = 50;
                    } else {
                        // Move up
                        d = -50;
                    }
                    if(getTerritoryById(currentTerritory.getId() + d).getOwner() != null) {
                        if (getTerritoryById(currentTerritory.getId() + d).getOwner().getId().equals(minion.getOwner().getId()) == false){
                            // Move in the x-axis direction
                            if (dx > 0) {
                                // Move right
                                d = 1;
                            } else {
                                // Move left
                                d = -1;
                            }
                        }
                    }
                }

                if(getTerritoryById(currentTerritory.getId() + d).getOwner() != null) {
                    if (getTerritoryById(currentTerritory.getId() + d).getOwner().getId().equals(minion.getOwner().getId()) == false) {
                        minion.setDestinationTerritory(null);
                        continue;
                    }
                }

                minion.getTerritory().getMinions().remove(minion);
                minion.setTerritory(getTerritoryById(currentTerritory.getId() + d));
                getTerritoryById(currentTerritory.getId() + d).getMinions().add(minion);

            } else {
                // If the minion doesn't have a destination or it's already in the destination territory, clear the destination
                minion.setDestinationTerritory(null);
            }
        }
    }

    // Helper method to get territory by ID
    private TerritoryModel getTerritoryById(int id) {
        return territoryRepository.findById(id).orElse(null);
    }






    public MinionModel getMinionById(String minionId) {
        MinionModel minion = this.minionRepository.findById(minionId).orElse(null);

        if (minion == null) {
            throw new NotFound("Minion not found");
        }

        return minion;
    }

}