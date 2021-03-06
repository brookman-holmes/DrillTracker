package com.brookmanholmes.drilltracker.domain;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A class that represents a Drill in the domain layer
 */
public class Drill {
    private final boolean purchased;
    private String id;
    private final String name;
    private final String imageUrl;
    private final String description;
    private final Type type;
    private final int maxScore;
    private final int defaultTargetScore;
    private final int cbPositions;
    private final int obPositions;
    private final int targetPositions;
    private List<Attempt> attempts = new ArrayList<>();
    private List<List<Integer>> patterns = new ArrayList<>();

    private Drill(String name, String description, String imageUrl, Type type, int maxScore, int defaultTargetScore, int obPositions, int cbPositions, int targetPositions, boolean purchased, List<List<Integer>> patterns) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
        this.maxScore = maxScore;
        this.defaultTargetScore = defaultTargetScore;
        this.type = type;
        this.purchased = purchased;
        this.obPositions = obPositions;
        this.cbPositions = cbPositions;
        this.targetPositions = targetPositions;
        this.patterns = patterns;
    }

    public Drill(String id, String name, String description, String imageUrl, Type type, int maxScore, int defaultTargetScore, int obPositions, int cbPositions, int targetPositions, boolean purchased, List<List<Integer>> patterns) {
        this(name, description, imageUrl, type, maxScore, defaultTargetScore, obPositions, cbPositions, targetPositions, purchased, patterns);
        this.id = id;
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

    public boolean isPurchased() {
        return purchased;
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

    public int getCbPositions() {
        return cbPositions;
    }

    public int getObPositions() {
        return obPositions;
    }

    public void addAttempt(Attempt attempt) {
        this.attempts.add(attempt);
    }

    public Type getType() {
        return type;
    }

    public List<List<Integer>> getPatterns() {
        return patterns;
    }

    @NonNull
    @Override
    public String toString() {
        return "Drill{" +
                "purchased=" + purchased +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", maxScore=" + maxScore +
                ", defaultTargetScore=" + defaultTargetScore +
                ", cbPositions=" + cbPositions +
                ", obPositions=" + obPositions +
                ", attempts=" + attempts +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Drill drill = (Drill) o;

        if (purchased != drill.purchased) return false;
        if (maxScore != drill.maxScore) return false;
        if (defaultTargetScore != drill.defaultTargetScore) return false;
        if (cbPositions != drill.cbPositions) return false;
        if (obPositions != drill.obPositions) return false;
        if (!Objects.equals(id, drill.id)) return false;
        if (!name.equals(drill.name)) return false;
        if (!imageUrl.equals(drill.imageUrl)) return false;
        if (!description.equals(drill.description)) return false;
        if (type != drill.type) return false;
        return attempts.equals(drill.attempts);
    }

    @Override
    public int hashCode() {
        int result = (purchased ? 1 : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + name.hashCode();
        result = 31 * result + imageUrl.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + maxScore;
        result = 31 * result + defaultTargetScore;
        result = 31 * result + cbPositions;
        result = 31 * result + obPositions;
        result = 31 * result + attempts.hashCode();
        return result;
    }

    public int getTargetPositions() {
        return targetPositions;
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
        private final int score;
        private final int target;
        private final Date date;
        private final int obPosition;
        private final int cbPosition;
        private final Map<String, Integer> extras;

        public Attempt(int score, int target, Date date, int obPosition, int cbPosition, Map<String, Integer> extras) {
            this.score = score;
            this.target = target;
            this.date = date;
            this.obPosition = obPosition;
            this.cbPosition = cbPosition;
            this.extras = extras;
        }

        public int getScore() {
            return score;
        }

        public int getTarget() {
            return target;
        }

        public Date getDate() {
            return new Date(date.getTime());
        }

        public int getObPosition() {
            return obPosition;
        }

        public int getCbPosition() {
            return cbPosition;
        }

        public Map<String, Integer> getExtras() {
            return extras;
        }

        @NonNull
        @Override
        public String toString() {
            return "Attempt{" +
                    "score=" + score +
                    ", target=" + target +
                    ", date=" + date +
                    ", obPosition=" + obPosition +
                    ", cbPosition=" + cbPosition +
                    '}';
        }
    }
}
