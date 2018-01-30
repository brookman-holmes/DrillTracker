package com.brookmanholmes.drilltracker.presentation.model;

import android.support.annotation.NonNull;
import android.util.Pair;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by Brookman Holmes on 7/7/2017.
 */

public class DrillModel extends Model {
    public String name;
    public String description;
    public String imageUrl;
    public int maxScore;
    public int defaultTargetScore;
    public int obPositions;
    public int cbPositions;
    public Type drillType;
    public boolean purchased;
    public Collection<AttemptModel> attemptModels;

    public DrillModel(String id, String name, String description, String imageUrl, int maxScore, int defaultTargetScore, int obPositions, int cbPositions, Type drillType, boolean purchased, Collection<AttemptModel> attemptModels) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.maxScore = maxScore;
        this.defaultTargetScore = defaultTargetScore;
        this.obPositions = obPositions;
        this.cbPositions = cbPositions;
        this.drillType = drillType;
        this.purchased = purchased;
        this.attemptModels = attemptModels;
    }

    public DrillModel(DrillModel model) {
        this.id = model.id;
        this.name = model.name;
        this.description = model.description;
        this.imageUrl = model.imageUrl;
        this.maxScore = model.maxScore;
        this.defaultTargetScore = model.defaultTargetScore;
        this.drillType = model.drillType;
        this.attemptModels = model.attemptModels;
        this.purchased = model.purchased;
        this.obPositions = model.obPositions;
        this.cbPositions = model.cbPositions;
    }

    public DrillModel(DrillModel model, int cbPosition, int obPosition) {
        this(model);
        attemptModels = getAttemptsByBallPosition(model.attemptModels, cbPosition, obPosition);
    }

    public static Collection<AttemptModel> getAttemptsByEnglish(Collection<AttemptModel> attempts, EnumSet<English> englishes) {
        List<AttemptModel> result = new ArrayList<>();
        for (AttemptModel attempt : attempts) {
            if (englishes.contains(English.ANY))
                result.add(attempt);
            else if (attempt.extras.containsKey(AimDrillModel.ENGLISH)) {
                if (englishes.contains(English.values()[attempt.extras.get(AimDrillModel.ENGLISH)]))
                    result.add(attempt);
            }
        }

        return result;
    }

    public static Collection<AttemptModel> getAttemptsByBallPosition(Collection<AttemptModel> attempts, int cbPosition, int obPosition) {
        List<AttemptModel> result = new ArrayList<>();

        for (AttemptModel attempt : attempts) {
            if (obPosition == 0 || attempt.obPosition == obPosition) {
                if (cbPosition == 0 || attempt.cbPosition == cbPosition)
                    result.add(attempt);
            }
        }

        return result;
    }

    public static Collection<AttemptModel> getSessionAttempts(Collection<AttemptModel> attempts) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        if (cal.get(Calendar.HOUR_OF_DAY) < 5)
            cal.add(Calendar.HOUR, -24);
        else cal.set(Calendar.HOUR_OF_DAY, 0);
        Date sessionTime = cal.getTime();

        Collection<AttemptModel> sessionAttempts = new ArrayList<>();
        for (AttemptModel model : attempts)

            if (model.date.after(sessionTime))
                sessionAttempts.add(model);

        return sessionAttempts;
    }

    public static List<AttemptModel> getAttemptsBetween(Collection<AttemptModel> attempts, Date date1, Date date2) {
        List<AttemptModel> result = new ArrayList<>();
        for (AttemptModel model : attempts) {
            if (model.date.after(date1) && model.date.before(date2))
                result.add(model);
        }

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrillModel that = (DrillModel) o;

        if (maxScore != that.maxScore) return false;
        if (defaultTargetScore != that.defaultTargetScore) return false;
        if (obPositions != that.obPositions) return false;
        if (cbPositions != that.cbPositions) return false;
        if (purchased != that.purchased) return false;
        if (!name.equals(that.name)) return false;
        if (!description.equals(that.description)) return false;
        if (!imageUrl.equals(that.imageUrl)) return false;
        if (drillType != that.drillType) return false;
        return attemptModels.equals(that.attemptModels);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + imageUrl.hashCode();
        result = 31 * result + maxScore;
        result = 31 * result + defaultTargetScore;
        result = 31 * result + obPositions;
        result = 31 * result + cbPositions;
        result = 31 * result + drillType.hashCode();
        result = 31 * result + (purchased ? 1 : 0);
        result = 31 * result + attemptModels.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "DrillModel{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", maxScore=" + maxScore +
                ", defaultTargetScore=" + defaultTargetScore +
                ", obPositions=" + obPositions +
                ", cbPositions=" + cbPositions +
                ", drillType=" + drillType +
                ", purchased=" + purchased +
                ", attemptModels=" + attemptModels +
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

    public static class Dates {
        public static Date ALL_TIME = new Date(0);
        public static Date TODAY = getDateInPast(Calendar.HOUR_OF_DAY, 0);
        public static Date LAST_WEEK = getDateInPast(Calendar.WEEK_OF_YEAR, -1);
        public static Date LAST_MONTH = getDateInPast(Calendar.MONTH, -1);
        public static Date LAST_THREE_MONTHS = getDateInPast(Calendar.MONTH, -3);
        public static Date LAST_SIX_MONTHS = getDateInPast(Calendar.MONTH, -6);

        private static Date getDateInPast(int field, int value) {
            Calendar cal = Calendar.getInstance(TimeZone.getDefault());
            cal.setTime(new Date());
            cal.add(field, value);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            return cal.getTime();
        }
    }

    public static class AttemptModel implements Comparable<AttemptModel>{
        public int score;
        public int target;
        public int obPosition;
        public int cbPosition;
        public String dateString;
        public Date date;
        public Map<String, Integer> extras = new HashMap<>();

        public AttemptModel(int score, int target, Date date, int obPosition, int cbPosition, Map<String, Integer> extras) {
            this.score = score;
            this.target = target;
            this.date = date;
            dateString = DateFormat.getDateInstance().format(date);
            this.obPosition = obPosition;
            this.cbPosition = cbPosition;
            this.extras.putAll(extras);
        }

        public AttemptModel(int score, int target, Date date, int obPosition, int cbPosition, Pair<String, Integer>... pairs) {
            this.score = score;
            this.target = target;
            this.date = date;
            dateString = DateFormat.getDateInstance().format(date);
            this.obPosition = obPosition;
            this.cbPosition = cbPosition;
            for (Pair<String, Integer> extra : pairs) {
                this.extras.put(extra.first, extra.second);
            }
        }

        public static AttemptModel copy(AttemptModel model) {
            return new AttemptModel(
                    model.score,
                    model.target,
                    model.date,
                    model.obPosition,
                    model.cbPosition,
                    model.extras);
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
            if (obPosition != that.obPosition) return false;
            if (cbPosition != that.cbPosition) return false;
            if (!dateString.equals(that.dateString)) return false;
            if (!date.equals(that.date)) return false;
            return extras.equals(that.extras);
        }

        @Override
        public int hashCode() {
            int result = score;
            result = 31 * result + target;
            result = 31 * result + obPosition;
            result = 31 * result + cbPosition;
            result = 31 * result + dateString.hashCode();
            result = 31 * result + date.hashCode();
            result = 31 * result + extras.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "AttemptModel{" +
                    "score=" + score +
                    ", target=" + target +
                    ", obPosition=" + obPosition +
                    ", cbPosition=" + cbPosition +
                    ", dateString='" + dateString + '\'' +
                    ", date=" + date +
                    ", extras=" + extras +
                    '}';
        }
    }
}
