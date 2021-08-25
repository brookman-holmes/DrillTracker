package com.brookmanholmes.drilltracker.presentation.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class PatternDrillModel {
    public  int allTimeAverageAttempts;
    public  int sixMonthAverageAttempts;
    public  int threeMonthAverageAttempts;
    public  int oneMonthAverageAttempts;
    public  int weekAverageAttempts;
    public  int sessionAverageAttempts;
    public  float allTimeAverage;
    public  float sixMonthAverage;
    public  float threeMonthAverage;
    public  float oneMonthAverage;
    public  float weekAverage;
    public  float sessionAverage;
    public  int thisPatternAttempts;
    public  double thisPatternCompPct;
    public  double thisPatternRunLength;
    public  int allPatternAttempts;
    public  double allPatternCompPct;
    public  double allPatternRunLength;
    private final int maxScore;
    private final TransitionalModel transitionalModel;
    private final PatternMakesMissesModel patternMakesMissesModel;

    public PatternDrillModel(DrillModel model, List<Integer> pattern) {
        this.maxScore = model.getMaxScore();
        transitionalModel = new TransitionalModel(model.getMaxScore());
        patternMakesMissesModel = new PatternMakesMissesModel(model.getMaxScore());

        /*
        DrillModel sixMonthModel = model.filterByDate(LAST_SIX_MONTHS, LAST_THREE_MONTHS);
        DrillModel threeMonthModel = model.filterByDate(LAST_THREE_MONTHS, LAST_MONTH);
        DrillModel oneMonthModel = model.filterByDate(LAST_MONTH, LAST_WEEK);
        DrillModel weekModel = model.filterByDate(LAST_WEEK, TODAY);
        DrillModel sessionModel = model.filterBySession();

        allTimeAverage = getAverage(model.getAttemptModels());
        sixMonthAverage = getAverage(sixMonthModel.getAttemptModels());
        threeMonthAverage = getAverage(threeMonthModel.getAttemptModels());
        oneMonthAverage = getAverage(oneMonthModel.getAttemptModels());
        weekAverage = getAverage(weekModel.getAttemptModels());
        sessionAverage = getAverage(sessionModel.getAttemptModels());

        allTimeAverageAttempts = getAttempts(model.getAttemptModels());
        sixMonthAverageAttempts = getAttempts(sixMonthModel.getAttemptModels());
        threeMonthAverageAttempts = getAttempts(threeMonthModel.getAttemptModels());
        oneMonthAverageAttempts = getAttempts(oneMonthModel.getAttemptModels());
        weekAverageAttempts = getAttempts(weekModel.getAttemptModels());
        sessionAverageAttempts = getAttempts(sessionModel.getAttemptModels());

        int thisPatternAttempts = 0;
        int allPatternAttempts = 0;
        int thisPatternSuccesses = 0;
        int allPatternSuccesses = 0;
        int thisPatternTotalBallsMade = 0;
        int allPatternTotalBallsMade = 0;

        for (AttemptModel attemptModel : model.getAttemptModels()) {
            allPatternAttempts++;
            if (attemptModel.score == maxScore)
                allPatternSuccesses++;
            allPatternTotalBallsMade += attemptModel.score;
            if (true) { //attemptMatchesPattern(attemptModel, pattern)) {
                thisPatternAttempts++;
                if (attemptModel.score == maxScore)
                    thisPatternSuccesses++;
                thisPatternTotalBallsMade += attemptModel.score;

                List<PatternEntry> patternEntries = new ArrayList<>(); //createPatternEntryListFromExtras(attemptModel.extras);
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

         */
    }

    public PatternDrillModel(DrillModel model) {
        this(model, new ArrayList<>());
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

    private float getAverage(Collection<AttemptModel> attempts) {
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

    private int getMakes(Collection<AttemptModel> attempts) {
        int result = 0;
        for (AttemptModel attempt : attempts) {
            result += attempt.getScore();
        }

        return result;
    }

    private int getAttempts(Collection<AttemptModel> attemptModels) {
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
            if (transitionalRatings.size() > positionInPattern) {
                if (transitionalRatings.get(positionInPattern).size() > rating - 1) {
                    transitionalRatings.get(positionInPattern).get(rating - 1).count += 1;
                } else {
                    Timber.i("transitionalRatings.get(positionInPattern).size() = %s and rating - 1 = %s", transitionalRatings.get(positionInPattern).size(), rating - 1);
                }
            } else {
                Timber.i("transitionalRatings.size() = %s and positionInPattern = %s", transitionalRatings.size(), positionInPattern);
            }
        }

        int getTransitionalCount(int positionInPattern, int rating) {
            if (transitionalRatings.size() > positionInPattern) {
                if (transitionalRatings.get(positionInPattern).size() > rating - 1) {
                    return transitionalRatings.get(positionInPattern).get(rating - 1).count;
                } else {
                    return 0;
                }
            } else {
                return 0;
            }
        }

        float getAverageRating(int positionInPattern) {
            float count = 0;
            float sum = 0;
            for (int rating = 0; rating < 4; rating++) {
                if (transitionalRatings.size() > positionInPattern) {
                    sum += transitionalRatings.get(positionInPattern).get(rating).count *
                            transitionalRatings.get(positionInPattern).get(rating).rating;
                    count += transitionalRatings.get(positionInPattern).get(rating).count;
                }
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
            if (pattern.size() > positionInPattern)
                return pattern.get(positionInPattern);
            else return new PatternPlayEntryModel();
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
