package com.brookmanholmes.drilltracker.data.entity;

import android.support.annotation.Keep;

import java.util.HashMap;
import java.util.Map;

/**
 * Drill Entity used in the data layer
 */
@Keep
public class DrillEntity {
    public String id;
    public String name;
    public String imageUrl;
    public boolean purchased = false;
    public String description;
    public int maxScore;
    public int targetScore;
    public int cbPositions;
    public int obPositions;
    public String type;
    public Map<String, AttemptEntity> attempts = new HashMap<>();

    public DrillEntity() {
        cbPositions = 1;
        obPositions = 1;

    }

    public DrillEntity(String name, String description, String id, String imageUrl, String type, int maxScore, int targetScore, int obPositions, int cbPositions) {
        this.name = name;
        this.id = id;
        this.imageUrl = imageUrl;
        this.maxScore = maxScore;
        this.targetScore = targetScore;
        this.description = description;
        this.type = type;
        this.cbPositions = cbPositions;
        this.obPositions = obPositions;
    }

    public DrillEntity(String name, String description, String id, String imageUrl, String type, int maxScore, int targetScore, int obPositions, int cbPositions, boolean purchased) {
        this(name, description, id, imageUrl, type, maxScore, targetScore, obPositions, cbPositions);
        this.purchased = purchased;
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

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrillEntity that = (DrillEntity) o;

        if (purchased != that.purchased) return false;
        if (maxScore != that.maxScore) return false;
        if (targetScore != that.targetScore) return false;
        if (cbPositions != that.cbPositions) return false;
        if (obPositions != that.obPositions) return false;
        if (!id.equals(that.id)) return false;
        if (!name.equals(that.name)) return false;
        if (!imageUrl.equals(that.imageUrl)) return false;
        if (!description.equals(that.description)) return false;
        if (!type.equals(that.type)) return false;
        return attempts.equals(that.attempts);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + imageUrl.hashCode();
        result = 31 * result + (purchased ? 1 : 0);
        result = 31 * result + description.hashCode();
        result = 31 * result + maxScore;
        result = 31 * result + targetScore;
        result = 31 * result + cbPositions;
        result = 31 * result + obPositions;
        result = 31 * result + type.hashCode();
        result = 31 * result + attempts.hashCode();
        return result;
    }

    public static class AttemptEntity {
        public int score;
        public int target;
        public long date;
        public int obPosition;
        public int cbPosition;
        public Map<String, Integer> extras = new HashMap<>();

        public AttemptEntity(int score, int target, long date, int obPosition, int cbPosition, Map<String, Integer> extras) {
            this.score = score;
            this.target = target;
            this.date = date;
            this.obPosition = obPosition;
            this.cbPosition = cbPosition;
            this.extras = extras;
        }

        public AttemptEntity() {

        }
    }
}
