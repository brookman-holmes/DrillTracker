package com.brookmanholmes.drilltracker.data.entity;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Drill Entity used in the data layer
 */
@Keep
public class DrillEntity {
    @Nullable
    public String id;
    @Nullable
    public String name;
    @Nullable
    public String imageUrl;
    public boolean purchased = false;
    @Nullable
    public String description;
    public int maxScore;
    public int targetScore;
    public final int cbPositions;
    public final int obPositions;
    public int targetPositions;
    @Nullable
    public String type;
    public int dataToCollect;

    public DrillEntity() {
        cbPositions = 1;
        obPositions = 1;
    }

    public DrillEntity copy() {
        return new DrillEntity(this);
    }

    public DrillEntity(DrillEntity entity) {
        this.id = entity.id;
        this.dataToCollect = entity.dataToCollect;
        this.cbPositions = entity.cbPositions;
        this.obPositions = entity.obPositions;
        this.targetPositions = entity.targetPositions;
        this.type = entity.type;
        this.name = entity.name;
        this.imageUrl = entity.imageUrl;
        this.purchased = entity.purchased;
        this.description = entity.description;
        this.maxScore = entity.maxScore;
        this.targetScore = entity.targetScore;
    }

    public DrillEntity(String id, String name, String imageUrl, boolean purchased, String description, int maxScore, int targetScore, int cbPositions, int obPositions, int targetPositions, String type, int dataToCollect) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.purchased = purchased;
        this.description = description;
        this.maxScore = maxScore;
        this.targetScore = targetScore;
        this.cbPositions = cbPositions;
        this.obPositions = obPositions;
        this.targetPositions = targetPositions;
        this.type = type;
        this.dataToCollect = dataToCollect;
    }

    public static Map<String, Object> toMap(DrillEntity entity) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", entity.id);
        result.put("name", entity.name);
        result.put("imageUrl", entity.imageUrl);
        result.put("purchased", entity.purchased);
        result.put("description", entity.description);
        result.put("maxScore", entity.maxScore);
        result.put("targetScore", entity.targetScore);
        result.put("cbPositions", entity.cbPositions);
        result.put("obPositions", entity.obPositions);
        result.put("type", entity.type);
        result.put("targetPositions", entity.targetPositions);
        result.put("dataToCollect", entity.dataToCollect);
        return result;
    }
}
