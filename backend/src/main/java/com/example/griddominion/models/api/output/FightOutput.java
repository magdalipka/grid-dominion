package com.example.griddominion.models.api.output;

import java.util.ArrayList;
import java.util.List;

import com.example.griddominion.models.db.MinionModel;
import com.example.griddominion.models.db.TowerModel;

public class FightOutput {
    public boolean win;
    public List<MinionOutput> leftMinions;
    public TowerOutput towerOutput;

    public FightOutput(boolean win, List<MinionModel> minions, TowerModel tower){
        this.win = win;
        towerOutput = new TowerOutput(tower);
        leftMinions = new ArrayList<>();
        for(MinionModel minion : minions){
            leftMinions.add(new MinionOutput(minion));
        }
    }
    public FightOutput(boolean win, List<MinionOutput> minions, TowerOutput tower){
        this.win = win;
        towerOutput = tower;
        leftMinions = minions;
    }
}
