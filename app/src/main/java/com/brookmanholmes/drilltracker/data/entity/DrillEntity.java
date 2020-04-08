package com.brookmanholmes.drilltracker.data.entity;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    public final int cbPositions;
    public final int obPositions;
    public int targetPositions;
    public String type;
    public final Map<String, AttemptEntity> attempts = new HashMap<>();
    public Map<String, String> patterns = new HashMap<>();

    public DrillEntity() {
        cbPositions = 1;
        obPositions = 1;
    }

    public DrillEntity(String name, String description, String id, String imageUrl, String type,
                       int maxScore, int targetScore, int obPositions, int cbPositions, int targetPositions,
                       Map<String, String> patterns) {
        this.name = name;
        this.id = id;
        this.imageUrl = imageUrl;
        this.maxScore = maxScore;
        this.targetScore = targetScore;
        this.description = description;
        this.type = type;
        this.cbPositions = cbPositions;
        this.obPositions = obPositions;
        this.targetPositions = targetPositions;
        this.patterns = patterns;
    }

    public DrillEntity(String name, String description, String id, String imageUrl, String type, int maxScore, int targetScore, int obPositions, int cbPositions, int targetPositions, boolean purchased, Map<String, String> patterns) {
        this(name, description, id, imageUrl, type, maxScore, targetScore, obPositions, cbPositions, targetPositions, patterns);
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
        result.put("targetPositions", entity.targetPositions);
        result.put("patterns", entity.patterns);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DrillEntity that = (DrillEntity) o;
        return purchased == that.purchased &&
                maxScore == that.maxScore &&
                targetScore == that.targetScore &&
                cbPositions == that.cbPositions &&
                obPositions == that.obPositions &&
                targetPositions == that.targetPositions &&
                Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(imageUrl, that.imageUrl) &&
                Objects.equals(description, that.description) &&
                Objects.equals(type, that.type) &&
                Objects.equals(patterns, that.patterns) &&
                Objects.equals(attempts, that.attempts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, imageUrl, purchased, description, maxScore, targetScore, cbPositions, obPositions, targetPositions, type, patterns, attempts);
    }

    @NonNull
    @Override
    public String toString() {
        return "DrillEntity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", patterns=" + patterns.get("pattern1") +
                '}';
    }

    public static class AttemptEntity {
        public int score;
        public int target;
        public long date;
        public int obPosition;
        public int cbPosition;
        public int targetPosition;
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
