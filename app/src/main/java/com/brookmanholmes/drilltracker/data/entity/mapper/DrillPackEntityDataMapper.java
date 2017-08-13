package com.brookmanholmes.drilltracker.data.entity.mapper;

import com.brookmanholmes.drilltracker.data.entity.DrillPackEntity;
import com.brookmanholmes.drilltracker.domain.DrillPack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Mapper class used to transform {@link DrillPackEntity} (in the data layer) to {@link DrillPack} in the
 * domain layer.
 */
public class DrillPackEntityDataMapper {
    public DrillPackEntityDataMapper() {
    }

    /**
     * Transform a {@link DrillPackEntity} into a {@link DrillPack}
     *
     * @param entity The entity to transform into a drill
     */
    public DrillPack transform(DrillPackEntity entity) {
        final DrillPack drillPack = new DrillPack();
        drillPack.name = entity.name;
        drillPack.price = entity.price;
        drillPack.description = entity.description;
        drillPack.sku = entity.sku;
        drillPack.url = entity.url;
        return drillPack;
    }

    /**
     * Transform a list of {@link DrillPackEntity} into a list of {@link DrillPack}
     * @param drillPackEntities The entities to transform
     */
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
