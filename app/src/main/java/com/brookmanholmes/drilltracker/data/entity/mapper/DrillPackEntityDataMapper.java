package com.brookmanholmes.drilltracker.data.entity.mapper;

import com.brookmanholmes.drilltracker.data.entity.DrillPackEntity;
import com.brookmanholmes.drilltracker.domain.DrillPack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Brookman Holmes on 8/9/2017.
 */

public class DrillPackEntityDataMapper {
    public DrillPackEntityDataMapper() {
    }

    public DrillPack transform(DrillPackEntity entity) {
        final DrillPack drillPack = new DrillPack();
        drillPack.name = entity.name;
        drillPack.price = entity.price;
        drillPack.description = entity.description;
        drillPack.sku = entity.sku;
        drillPack.url = entity.url;
        return drillPack;
    }

    public List<DrillPack> transform(List<DrillPackEntity> drillPackEntities) {
        List<DrillPack> result;
        if (drillPackEntities != null && !drillPackEntities.isEmpty()) {
            result = new ArrayList<>();
            for (DrillPackEntity drillPackEntity : drillPackEntities) {
                result.add(transform(drillPackEntity));
            }
        } else {
            result = Collections.emptyList();
        }

        return result;

    }
}
