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

    private DrillPackModel transform(DrillPack drillPack) {
        DrillPackModel model = new DrillPackModel(
                drillPack.sku,
                drillPack.name,
                drillPack.price,
                drillPack.description,
                drillPack.sku,
                null,
                false,
                ""
        );

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
                result.add(model.getSku());
            }
        } else {
            result = Collections.emptyList();
        }

        return result;
    }
}
