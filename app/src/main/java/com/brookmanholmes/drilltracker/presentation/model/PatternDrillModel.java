package com.brookmanholmes.drilltracker.presentation.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.brookmanholmes.drilltracker.presentation.model.DrillModel.Dates.LAST_MONTH;
import static com.brookmanholmes.drilltracker.presentation.model.DrillModel.Dates.LAST_SIX_MONTHS;
import static com.brookmanholmes.drilltracker.presentation.model.DrillModel.Dates.LAST_THREE_MONTHS;
import static com.brookmanholmes.drilltracker.presentation.model.DrillModel.Dates.LAST_WEEK;
import static com.brookmanholmes.drilltracker.presentation.model.DrillModel.Dates.TODAY;

public class PatternDrillModel {
    public final int allTimeAverageAttempts;
    public final int sixMonthAverageAttempts;
    public final int threeMonthAverageAttempts;
    public final int oneMonthAverageAttempts;
    public final int weekAverageAttempts;
    public final int sessionAverageAttempts;
    public final float allTimeAverage;
    public final float sixMonthAverage;
    public final float threeMonthAverage;
    public final float oneMonthAverage;
    public final float weekAverage;
    public final float sessionAverage;
    public final int thisPatternAttempts;
    public final double thisPatternCompPct;
    public final double thisPatternRunLength;
    public final int allPatternAttempts;
    public final double allPatternCompPct;
    public final double allPatternRunLength;
    private final int maxScore;
    private final TransitionalModel transitionalModel;
    private final PatternMakesMissesModel patternMakesMissesModel;

    public PatternDrillModel(DrillModel model, List<Integer> pattern) {
        this.maxScore = model.maxScore;
        transitionalModel = new TransitionalModel(model.maxScore);
        patternMakesMissesModel = new PatternMakesMissesModel(model.maxScore);

        allTimeAverage = getAverage(model.attemptModels);
        sixMonthAverage = getAverage(DrillModel.getAttemptsBetween(model.attemptModels, LAST_SIX_MONTHS, LAST_THREE_MONTHS));
        threeMonthAverage = getAverage(DrillModel.getAttemptsBetween(model.attemptModels, LAST_THREE_MONTHS, LAST_MONTH));
        oneMonthAverage = getAverage(DrillModel.getAttemptsBetween(model.attemptModels, LAST_MONTH, LAST_WEEK));
        weekAverage = getAverage(DrillModel.getAttemptsBetween(model.attemptModels, LAST_WEEK, TODAY));
        sessionAverage = getAverage(DrillModel.getSessionAttempts(model.attemptModels));

        allTimeAverageAttempts = getAttempts(model.attemptModels);
        sixMonthAverageAttempts = getAttempts(DrillModel.getAttemptsBetween(model.attemptModels, LAST_SIX_MONTHS, LAST_THREE_MONTHS));
        threeMonthAverageAttempts = getAttempts(DrillModel.getAttemptsBetween(model.attemptModels, LAST_THREE_MONTHS, LAST_MONTH));
        oneMonthAverageAttempts = getAttempts(DrillModel.getAttemptsBetween(model.attemptModels, LAST_MONTH, LAST_WEEK));
        weekAverageAttempts = getAttempts(DrillModel.getAttemptsBetween(model.attemptModels, LAST_WEEK, TODAY));
        sessionAverageAttempts = getAttempts(DrillModel.getSessionAttempts(model.attemptModels));

        int thisPatternAttempts = 0;
        int allPatternAttempts = 0;
        int thisPatternSuccesses = 0;
        int allPatternSuccesses = 0;
        int thisPatternTotalBallsMade = 0;
        int allPatternTotalBallsMade = 0;

        for (DrillModel.AttemptModel attemptModel : model.attemptModels) {
            allPatternAttempts++;
            if (attemptModel.score == maxScore)
                allPatternSuccesses++;
            allPatternTotalBallsMade += attemptModel.score;
            if (attemptMatchesPattern(attemptModel, pattern)) {
                thisPatternAttempts++;
                if (attemptModel.score == maxScore)
                    thisPatternSuccesses++;
                thisPatternTotalBallsMade += attemptModel.score;

                List<PatternEntry> patternEntries = createPatternEntryListFromExtras(attemptModel.extras);
                for (int positionInPattern = 0; positionInPattern < patternEntries.size(); positionInPattern++) {
                    PatternEntry patternEntry = patternEntries.get(positionInPattern);
                    patternEntry.setMade(positionInPattern < attemptModel.score);
                    if (patternEntry.getRating() > 0) {
                        transitionalModel.addTransitionalRating(positionInPattern, patternEntry.getRating());
                    }
                }

                patternMakesMissesModel.addPatternAttempt(attemptModel.score);
            }
        }

        this.thisPatternAttempts = thisPatternAttempts;
        this.thisPatternCompPct = getAverage(thisPatternSuccesses, thisPatternAttempts);
        this.thisPatternRunLength = getAverage(thisPatternTotalBallsMade, thisPatternAttempts);
        this.allPatternAttempts = allPatternAttempts;
        this.allPatternCompPct = getAverage(allPatternSuccesses, allPatternAttempts);
        this.allPatternRunLength = getAverage(allPatternTotalBallsMade, allPatternAttempts);
    }

    public PatternDrillModel(DrillModel model) {
        this(model, new ArrayList<>());
    }

    public static DrillModel.AttemptModel createAttempt(int targetScore, List<PatternEntry> patternEntryList) {
        int score = 0;
        Map<String, Integer> extras = new HashMap<>();

        for (int i = 0; i < patternEntryList.size(); i++) {
            PatternEntry patternEntry = patternEntryList.get(i);

            if (patternEntry.isMade()) score++;

            // setting the pattern rating to 0 if the ball is not made because the recyclerview sets them to
            // 1 automatically =[[[
            extras.put(getExtraKey(i, patternEntry.getBall()), patternEntry.isMade() ? patternEntry.getRating() : 0);
        }

        return new DrillModel.AttemptModel(score, targetScore, new Date(), 0, 0, extras);
    }

    public static Collection<DrillModel.AttemptModel> getAttemptsWithPattern(Collection<DrillModel.AttemptModel> attempts, List<Integer> pattern) {
        Collection<DrillModel.AttemptModel> result = new ArrayList<>();
        for (DrillModel.AttemptModel attempt : attempts) {
            if (attemptMatchesPattern(attempt, pattern)) {
                result.add(attempt);
            }
        }

        return result;
    }

    private static boolean attemptMatchesPattern(DrillModel.AttemptModel attempt, List<Integer> pattern) {
        for (int i = 0; i < pattern.size(); i++) {
            if (!attempt.extras.containsKey(getExtraKey(i, pattern.get(i))))
                return false;
        }

        return true;
    }

    private static String getExtraKey(int patternPosition, int ballNumber) {
        return String.format("%s-%s", patternPosition, ballNumber);
    }

    static List<PatternEntry> createPatternEntryListFromExtras(Map<String, Integer> extras) {
        PatternEntry[] patternEntries = new PatternEntry[extras.size()];

        for (String key : extras.keySet()) {
            String[] output = key.split("-");
            int positionInPattern = Integer.parseInt(output[0]);
            int ball = Integer.parseInt(output[1]);
            patternEntries[positionInPattern] = new PatternEntry(ball, false, extras.get(key));
        }

        return Arrays.asList(patternEntries);
    }

    public int getMaxScore() {
        return maxScore;
    }

    private float getAverage(Collection<DrillModel.AttemptModel> attempts) {
        if (getAttempts(attempts) == 0)
            return 0;
        else {
            return (float) getMakes(attempts) / (float) (attempts.size());
        }
    }

    private float getAverage(float top, float bottom) {
        if (bottom == 0)
            return 0;
        else
            return top / bottom;
    }

    private int getMakes(Collection<DrillModel.AttemptModel> attempts) {
        int result = 0;
        for (DrillModel.AttemptModel attempt : attempts) {
            result += attempt.score;
        }

        return result;
    }

    private int getAttempts(Collection<DrillModel.AttemptModel> attemptModels) {
        return attemptModels.size();
    }

    public int getTransitionalCount(int positionInPattern, int rating) {
        return transitionalModel.getTransitionalCount(positionInPattern, rating);
    }

    public float getAverageRating(int positionInPattern) {
        return transitionalModel.getAverageRating(positionInPattern);
    }

    public int getMissesAtPosition(int positionInPattern) {
        return patternMakesMissesModel.getPatternPlayEntry(positionInPattern).misses;
    }

    public int getAttemptsAtPosition(int positionInPattern) {
        return patternMakesMissesModel.getPatternPlayEntry(positionInPattern).misses +
                patternMakesMissesModel.getPatternPlayEntry(positionInPattern).makes;
    }

    private static class Ratings {
        private final int rating;
        private int count = 0;

        Ratings(int rating) {
            this.rating = rating;
        }
    }

    private static class TransitionalModel {
        private final List<List<Ratings>> transitionalRatings = new ArrayList<>();

        TransitionalModel(int maxScore) {
            for (int i = 0; i < maxScore; i++) {
                List<Ratings> ratings = new ArrayList<>();
                for (int j = 1; j < 5; j++) {
                    ratings.add(new Ratings(j));
                }
                transitionalRatings.add(ratings);
            }
        }

        void addTransitionalRating(int positionInPattern, int rating) {
            transitionalRatings.get(positionInPattern).get(rating - 1).count += 1;
        }

        int getTransitionalCount(int positionInPattern, int rating) {
            return transitionalRatings.get(positionInPattern).get(rating - 1).count;
        }

        float getAverageRating(int positionInPattern) {
            float count = 0;
            float sum = 0;
            for (int rating = 0; rating < 4; rating++) {
                sum += transitionalRatings.get(positionInPattern).get(rating).count *
                        transitionalRatings.get(positionInPattern).get(rating).rating;
                count += transitionalRatings.get(positionInPattern).get(rating).count;
            }

            if (count == 0)
                return 0f;
            else return sum / count;
        }
    }

    private static class PatternMakesMissesModel {
        final List<PatternPlayEntryModel> pattern = new ArrayList<>();

        private PatternMakesMissesModel(int patternLength) {
            for (int i = 0; i < patternLength; i++) {
                pattern.add(new PatternPlayEntryModel());
            }
        }

        void addPatternAttempt(int score) {
            for (int i = 0; i < pattern.size(); i++) {
                if (i < score) {
                    pattern.get(i).makes++;
                } else if (i == score) {
                    pattern.get(i).misses++;
                }
            }
        }

        PatternPlayEntryModel getPatternPlayEntry(int positionInPattern) {
            return pattern.get(positionInPattern);
        }
    }

    private static class PatternPlayEntryModel {
        private int makes;
        private int misses;

        private PatternPlayEntryModel() {
            this.makes = 0;
            this.misses = 0;
        }

        @NonNull
        @Override
        public String toString() {
            return "PatternPlayEntryModel{" +
                    "makes=" + makes +
                    ", misses=" + misses +
                    '}';
        }
    }
}
