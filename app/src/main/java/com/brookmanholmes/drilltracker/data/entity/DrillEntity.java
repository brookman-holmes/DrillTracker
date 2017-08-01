package com.brookmanholmes.drilltracker.data.entity;

import com.brookmanholmes.drilltracker.domain.Drill;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Brookman Holmes on 7/9/2017.
 */

public class DrillEntity {
    public String id;
    public String name;
    public String imageUrl;
    public String description;
    public int maxScore;
    public int targetScore;
    public String type;
    public Map<String, AttemptEntity> attempts = new HashMap<>();

    public DrillEntity() {
    }

    public DrillEntity(String name, String description, String id, String imageUrl, String type, int maxScore, int targetScore) {
        this.name = name;
        this.id = id;
        this.imageUrl = imageUrl;
        this.maxScore = maxScore;
        this.targetScore = targetScore;
        this.description = description;
        this.type = type;
    }

    public static class AttemptEntity {
        public int score;
        public int target;
        public long date;
    }

    @Override
    public String toString() {
        return "DrillEntity{" +
                "id='" + id + '\'' +
                "\n name='" + name + '\'' +
                "\n imageUrl='" + imageUrl + '\'' +
                "\n description='" + description + '\'' +
                "\n maxScore=" + maxScore +
                "\n targetScore=" + targetScore +
                "\n type='" + type + '\'' +
                "\n attempts=" + attempts +
                '}';
    }
}
