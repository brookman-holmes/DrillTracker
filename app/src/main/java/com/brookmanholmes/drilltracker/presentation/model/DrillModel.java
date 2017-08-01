package com.brookmanholmes.drilltracker.presentation.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Created by Brookman Holmes on 7/7/2017.
 */

public class DrillModel {
    public String id;
    public String name;
    public String description;
    public String imageUrl;
    public int maxScore;
    public int defaultTargetScore;
    public Type drillType;
    public Collection<AttemptModel> attemptModels;

    public static DrillModel getSessionModel(DrillModel model) {
        final DrillModel result = new DrillModel(model);
        result.attemptModels = result.getSessionAttempts();
        return result;
    }

    public DrillModel() {

    }

    public DrillModel(DrillModel model) {
        this.id = model.id;
        this.name = model.name;
        this.description = model.description;
        this.imageUrl = model.imageUrl;
        this.maxScore = model.maxScore;
        this.attemptModels = model.attemptModels;
        this.drillType = model.drillType;
    }

    public Collection<AttemptModel> getSessionAttempts() {
        Date date = new Date();
        date.setTime(date.getTime() - 86400000);
        Collection<AttemptModel> sessionAttempts = new ArrayList<>();
        for (AttemptModel model : attemptModels)

            if (model.date.after(date))
                sessionAttempts.add(model);

        return sessionAttempts;
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

    public static class AttemptModel implements Comparable<AttemptModel>{
        public int score;
        public int target;
        public String dateString;
        public Date date;

        public static AttemptModel copy(AttemptModel model) {
            AttemptModel copy = new AttemptModel();
            copy.score = model.score;
            copy.target = model.target;
            copy.dateString = model.dateString;
            copy.date = new Date(model.date.getTime());

            return copy;
        }

        @Override
        public int compareTo(@NonNull AttemptModel attemptModel) {
            return date.compareTo(attemptModel.date);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            AttemptModel that = (AttemptModel) o;

            if (score != that.score) return false;
            if (target != that.target) return false;
            if (!dateString.equals(that.dateString)) return false;
            return date.equals(that.date);

        }

        @Override
        public int hashCode() {
            int result = score;
            result = 31 * result + target;
            result = 31 * result + dateString.hashCode();
            result = 31 * result + date.hashCode();
            return result;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrillModel that = (DrillModel) o;

        if (maxScore != that.maxScore) return false;
        if (defaultTargetScore != that.defaultTargetScore) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (!name.equals(that.name)) return false;
        if (!description.equals(that.description)) return false;
        if (imageUrl != null ? !imageUrl.equals(that.imageUrl) : that.imageUrl != null)
            return false;
        return attemptModels.equals(that.attemptModels);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + name.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        result = 31 * result + maxScore;
        result = 31 * result + defaultTargetScore;
        result = 31 * result + attemptModels.hashCode();
        return result;
    }
}
