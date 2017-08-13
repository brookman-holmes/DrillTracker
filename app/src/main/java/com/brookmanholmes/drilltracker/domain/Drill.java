package com.brookmanholmes.drilltracker.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * A class that represents a Drill in the domain layer
 */
public class Drill {
    private String id;
    private String name;
    private String imageUrl;
    private String description;
    private Type type;
    private int maxScore;
    private int defaultTargetScore;
    private List<Attempt> attempts = new ArrayList<>();

    public Drill(String name, String description, String imageUrl, Type type, int maxScore, int defaultTargetScore) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
        this.maxScore = maxScore;
        this.defaultTargetScore = defaultTargetScore;
        this.type = type;
    }

    public Drill(String id, String name, String description, String imageUrl, Type type, int maxScore, int defaultTargetScore) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
        this.maxScore = maxScore;
        this.defaultTargetScore = defaultTargetScore;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public List<Attempt> getAttempts() {
        return attempts;
    }

    public void setAttempts(Collection<Attempt> attempts) {
        this.attempts = new ArrayList<>(attempts);
    }

    public int getDefaultTargetScore() {
        return defaultTargetScore;
    }

    public void addAttempt(Attempt attempt) {
        this.attempts.add(attempt);
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Drill{" +
                "id='" + id + '\'' +
                "\n name='" + name + '\'' +
                "\n imageUrl='" + imageUrl + '\'' +
                "\n maxScore=" + maxScore +
                "\n defaultTargetScore=" + defaultTargetScore +
                '}';
    }

    public enum Type {
        ANY,
        AIMING,
        BANKING,
        KICKING,
        PATTERN,
        POSITIONAL,
        SAFETY,
        SPEED
    }

    public static class Attempt {
        private int score;
        private int target;
        private Date date;

        public Attempt(int score, int target, Date date) {
            this.score = score;
            this.target = target;
            this.date = date;
        }

        public int getScore() {
            return score;
        }

        public int getTarget() {
            return target;
        }

        public Date getDate() {
            return date;
        }

        @Override
        public String toString() {
            return "Attempt{" +
                    "score=" + score +
                    "\n target=" + target +
                    "\n date=" + date +
                    '}';
        }
    }
}
