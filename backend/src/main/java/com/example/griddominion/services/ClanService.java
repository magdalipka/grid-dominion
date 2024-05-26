package com.example.griddominion.services;


import java.util.UUID;

import com.example.griddominion.models.api.input.ClanCreationInput;
import com.example.griddominion.models.db.ClanModel;
import com.example.griddominion.repositories.ClanRepository;
import com.example.griddominion.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.griddominion.utils.errors.BadRequest;
import com.example.griddominion.utils.errors.NotFound;
import com.example.griddominion.utils.errors.ResourceConflict;
import com.example.griddominion.utils.errors.Unauthorized;

import org.springframework.dao.DataIntegrityViolationException;


@Service
public class ClanService {
    @Autowired
    ClanRepository clanRepository;

    public ClanModel createClan(ClanCreationInput input) {

        if (input.name == null) {
            throw new BadRequest("Clan name is not specified");
        }

        // check if clan name already exists here?

        String id = UUID.randomUUID().toString();

        ClanModel clan = new ClanModel();
        clan.setId(id);
        clan.setName(input.name);
        if(input.isPrivate) clan.makePrivate();
        else clan.makePublic();
        clan.setLevel(1);
        clan.setExperience(0);
        clan.setExperienceToLevelUp(Constants.BASE_CLAN_EXPERIENCE);
        clan.initUsersList();

        try {
            clanRepository.insert(clan.getId(), clan.getName(), clan.isPrivate(), clan.getLevel(), clan.getExperience(), clan.getExperienceToLevelUp());
        } catch (DataIntegrityViolationException e) {
            throw new ResourceConflict("Clan with name " + input.name + " already exists");
        }

        return clan;
    }

    public void deleteClan(String id) {
        ClanModel clan = clanRepository.findById(id).orElse(null);

        if (clan == null) {
            throw new Unauthorized("Clan not found");
        }

        clanRepository.delete(clan);
    }

    public ClanModel getClanById(String clanId) {
        ClanModel clan = this.clanRepository.findById(clanId).orElse(null);

        if (clan == null) {
            throw new NotFound("Clan not found");
        }

        return clan;
    }

}