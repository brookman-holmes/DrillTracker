package com.brookmanholmes.drilltracker.presentation.mapper;

import com.brookmanholmes.drilltracker.domain.DrillPack;
import com.brookmanholmes.drilltracker.presentation.model.DrillPackModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Brookman Holmes on 8/9/2017.
 */

public class DrillPackModelDataMapper {
    public DrillPackModelDataMapper() {
    }

    public DrillPackModel transform(DrillPack drillPack) {
        DrillPackModel model = new DrillPackModel();
        model.name = drillPack.name;
        model.price = drillPack.price;
        model.description = drillPack.description;
        model.sku = drillPack.sku;
        model.url = drillPack.url;
        model.id = drillPack.sku;

        return model;
    }

    public List<DrillPackModel> transform(List<DrillPack> drillPacks) {
        List<DrillPackModel> result;
        if (drillPacks != null && !drillPacks.isEmpty()) {
            result = new ArrayList<>();
            for (DrillPack drillPack : drillPacks) {
                result.add(transform(drillPack));
            }
        } else {
            result = Collections.emptyList();
        }

        return result;
    }

    public List<String> getSkus(List<DrillPackModel> models) {
        List<String> result;

        if (models != null && !models.isEmpty()) {
            result = new ArrayList<>();
            for (DrillPackModel model : models) {
                result.add(model.sku);
            }
        } else {
            result = Collections.emptyList();
        }

        return result;
    }
}
