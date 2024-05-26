package com.example.griddominion.services;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.griddominion.models.api.input.TerritoryOwnerInput;
import com.example.griddominion.models.api.output.TerritoryOutput;
import com.example.griddominion.models.api.output.TerritoryOwnerOutput;
import com.example.griddominion.models.db.TerritoryModel;
import com.example.griddominion.models.db.UserModel;
import com.example.griddominion.repositories.TerritoryRepository;
import com.example.griddominion.repositories.UserRepository;

import jakarta.annotation.PostConstruct;

@Service
public class TerritoryService {
    @Autowired
    TerritoryRepository territoryRepository;
    @Autowired
    UserRepository userRepository;

    @PostConstruct
    public void initTerritories(){
        if(territoryRepository.findAll().isEmpty()){
            double start_latitude = 50.12489;
            double start_longitude = 19.75846;
            double diff_latitude = 0.001214;
            double diff_longitude = 0.001894;
            Random random = new Random();

            for(int i = 0; i <200;i++){
                for(int j = 0; j<150; j++){
                    TerritoryModel territoryModel = new TerritoryModel();
                    territoryModel.setMaxLatitude(start_latitude - i*diff_latitude);
                    territoryModel.setMinLatitude(start_latitude - (i+1)*diff_latitude);
                    territoryModel.setMinLongitude(start_longitude + i*diff_longitude);
                    territoryModel.setMaxLongitude(start_longitude + (i+1)*diff_longitude);
                    territoryModel.setGold(100 + random.nextInt(901));
                    territoryModel.setWood(100 + random.nextInt(901));
                    territoryModel.setFood(100 + random.nextInt(901));
                    territoryRepository.save(territoryModel);
                }
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
