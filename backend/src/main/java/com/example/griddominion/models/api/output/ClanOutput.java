package com.example.griddominion.models.api.output;


import com.example.griddominion.models.db.ClanModel;

public class ClanOutput {
    public String id;
    public boolean isPrivate;

    public ClanOutput(ClanModel clanModel){
        this.id = clanModel.getId();
        this.isPrivate = clanModel.isPrivate();
    }

    public ClanOutput(String id, boolean isPrivate){
        this.id = id;
        this.isPrivate = isPrivate;
    }
}
