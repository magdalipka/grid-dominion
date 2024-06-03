package com.example.griddominion.factories;

import com.example.griddominion.models.api.output.BuildingOutput;
import com.example.griddominion.models.api.output.FarmOutput;
import com.example.griddominion.models.api.output.GoldMineOutput;
import com.example.griddominion.models.api.output.LumberMillOutput;
import com.example.griddominion.models.api.output.TowerOutput;
import com.example.griddominion.models.db.BuildingModel;
import com.example.griddominion.models.db.FarmModel;
import com.example.griddominion.models.db.GoldMineModel;
import com.example.griddominion.models.db.LumberMillModel;
import com.example.griddominion.models.db.TowerModel;

public class BuildingOutputFactory {
    public static BuildingOutput createOutput(BuildingModel model) {
        if (model instanceof GoldMineModel) {
            return new GoldMineOutput((GoldMineModel) model);
        } else if (model instanceof LumberMillModel) {
            return new LumberMillOutput((LumberMillModel) model);
        } else if (model instanceof FarmModel) {
            return new FarmOutput((FarmModel) model);
        } else if (model instanceof TowerModel) {
            return new TowerOutput((TowerModel) model);
        }
        throw new IllegalArgumentException("Unknown BuildingModel type: " + model.getClass().getName());
    }
}
