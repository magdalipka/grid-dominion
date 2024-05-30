package com.example.griddominion.services;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.griddominion.models.api.input.TerritoryOwnerInput;
import com.example.griddominion.models.api.output.TerritoryOutput;
import com.example.griddominion.models.api.output.TerritoryOwnerOutput;
import com.example.griddominion.models.db.TerritoryModel;
import com.example.griddominion.models.db.UserModel;
import com.example.griddominion.models.db.InventoryModel;
import com.example.griddominion.repositories.InventoryRepository;
import com.example.griddominion.repositories.TerritoryRepository;
import com.example.griddominion.repositories.UserRepository;
import com.example.griddominion.utils.Constants;
import com.example.griddominion.utils.Item;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
public class TerritoryService {
    @Autowired
    TerritoryRepository territoryRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    InventoryRepository inventoryRepository;

    @PostConstruct
    public void initTerritories(){
        if(territoryRepository.findAll().isEmpty()){
        
            Random random = new Random();

            for(int i = 0; i <150;i++){
                for(int j = 0; j<200; j++){
                    TerritoryModel territoryModel = new TerritoryModel();
                    territoryModel.setMaxLatitude(Constants.START_LATITUDE - (double)i*Constants.DIFF_LATITUDE);
                    territoryModel.setMinLatitude(Constants.START_LATITUDE - (double)(i+1)*Constants.DIFF_LATITUDE);
                    territoryModel.setMinLongitude(Constants.START_LONGITUDE + (double)j*Constants.DIFF_LONGITUDE);
                    territoryModel.setMaxLongitude(Constants.START_LONGITUDE+ (double)(j+1)*Constants.DIFF_LONGITUDE);
                    territoryModel.setGold(100 + random.nextInt(901));
                    territoryModel.setWood(100 + random.nextInt(901));
                    territoryModel.setFood(100 + random.nextInt(901));
                    territoryRepository.save(territoryModel);
                }
            }
        }
    }
    @Transactional
    @Scheduled(cron = "0 0/10 * * * ?")
    public void addResources(){
        List<TerritoryModel> territories = territoryRepository.findAll();

        for(TerritoryModel territory: territories){
            UserModel owner = territory.getOwner();
            if(owner!=null){
                InventoryModel inventory =  inventoryRepository.findByUserId(owner);
                HashMap<Item,Integer> items = inventory.getInventory();
                int current = items.get(Item.FOOD);
                items.put(Item.FOOD, Math.min(Constants.RESOURCE_LIMIT,current+territory.getFood()));
                current = items.get(Item.WOOD);
                items.put(Item.WOOD, Math.min(Constants.RESOURCE_LIMIT,current+territory.getWood()));
                current = items.get(Item.GOLD);
                items.put(Item.GOLD, Math.min(Constants.RESOURCE_LIMIT,current+territory.getGold()));
                inventory.setInventory(items);
                inventoryRepository.save(inventory);
            }
        }
    }

    public List<TerritoryOutput> getAllTerritories(){
        List<TerritoryModel> territories = territoryRepository.findAll();
        return territories.stream()
                          .map(territory -> new TerritoryOutput(territory))
                          .collect(Collectors.toList());
    }

    public List<TerritoryOwnerOutput> getAllTerritoryOwners(){
        List<TerritoryModel> territories = territoryRepository.findAll();
        return territories.stream()
                          .map(territory -> new TerritoryOwnerOutput(territory))
                          .collect(Collectors.toList());
    }

    public void upddateOwner(TerritoryOwnerInput territoryOwnerInput){
        UserModel user = userRepository.findById(territoryOwnerInput.userId).get();
        TerritoryModel territory = territoryRepository.findById(territoryOwnerInput.Id).get();
        territory.setOwner(user);
        territoryRepository.save(territory);
    }

}
