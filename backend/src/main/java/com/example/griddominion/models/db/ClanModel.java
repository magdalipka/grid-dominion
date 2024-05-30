package com.example.griddominion.models.db;

import java.util.ArrayList;
import java.util.List;

import com.example.griddominion.utils.Constants;
import jakarta.persistence.*;

@Entity
@Table(name = "clans")
public class ClanModel {
    @Id
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "isPrivate")
    private boolean isPrivate;

    @Column(name = "level")
    private int level;

    @Column(name = "experience")
    private int experience;

    @Column(name = "experienceToLevelUp")
    private int experienceToLevelUp;

    @OneToMany(mappedBy = "clan", fetch = FetchType.LAZY)
    private List<UserModel> users;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPrivate(){
        return this.isPrivate;
    }

    public void makePrivate(){
        this.isPrivate = true;
    }

    public void makePublic(){
        this.isPrivate = false;
    }

    public int getExperience(){
        return experience;
    }

    public void setExperience(int experience){
        this.experience = experience;
    }

    public void earnExperience(int experience){
        this.experience += experience;
        if(getExperience() >= getExperienceToLevelUp()){
            if(getLevel() < Constants.MAX_CLAN_LEVEL) {
                setLevel(level + 1);
                setExperience(0);
                setExperienceToLevelUp((int) Math.round((Math.pow(level, 1.2)) * Constants.BASE_CLAN_EXPERIENCE));
            }
        }
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExperienceToLevelUp() {
        return experienceToLevelUp;
    }

    public void setExperienceToLevelUp(int experienceToLevelUp) {
        this.experienceToLevelUp = experienceToLevelUp;
    }

    public void initUsersList(){
        this.users = new ArrayList<>();
    }

    public List<UserModel> getUsersList(){
        return this.users;
    }

}