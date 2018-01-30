package com.brookmanholmes.drilltracker.presentation.model;

import java.util.Collection;
import java.util.EnumSet;

import static com.brookmanholmes.drilltracker.presentation.model.DrillModel.Dates.LAST_MONTH;
import static com.brookmanholmes.drilltracker.presentation.model.DrillModel.Dates.LAST_SIX_MONTHS;
import static com.brookmanholmes.drilltracker.presentation.model.DrillModel.Dates.LAST_THREE_MONTHS;
import static com.brookmanholmes.drilltracker.presentation.model.DrillModel.Dates.LAST_WEEK;
import static com.brookmanholmes.drilltracker.presentation.model.DrillModel.Dates.TODAY;

/**
 * Created by brookman on 1/12/18.
 */

public class AimDrillModel {
    public static final String ATTEMPTS = "attempts";
    public static final String MAKES = "makes";
    public static final String OVER_CUTS = "over_cuts";
    public static final String UNDER_CUTS = "under_cuts";
    public static final String ENGLISH = "english";
    public final int lifetimeAttempts;
    public final int lifetimeMakes;
    public final int lifetimeOverCuts;
    public final int lifetimeUnderCuts;
    public final int sessionAttempts;
    public final int sessionMakes;
    public final int sessionOverCuts;
    public final int sessionUnderCuts;
    public final float allTimeAverage;
    public final float sixMonthAverage;
    public final float threeMonthAverage;
    public final float oneMonthAverage;
    public final float weekAverage;
    public final float sessionAverage;
    public final int allTimeAverageAttempts;
    public final int sixMonthAverageAttempts;
    public final int threeMonthAverageAttempts;
    public final int oneMonthAverageAttempts;
    public final int weekAverageAttempts;
    public final int sessionAverageAttempts;
    public float targetScore;

    public AimDrillModel(Collection<DrillModel.AttemptModel> attempts, EnumSet<English> englishes, int targetScore) {
        this.targetScore = (float) targetScore / (float) 100;

        Collection<DrillModel.AttemptModel> attemptModels = DrillModel.getAttemptsByEnglish(attempts, englishes);
        Collection<DrillModel.AttemptModel> sessionAttemptModels = DrillModel.getSessionAttempts(attemptModels);

        sessionAttempts = getAttempts(sessionAttemptModels);
        sessionMakes = getMakes(sessionAttemptModels);
        sessionUnderCuts = getUnderCuts(sessionAttemptModels);
        sessionOverCuts = getOverCuts(sessionAttemptModels);
        lifetimeAttempts = getAttempts(attemptModels);
        lifetimeMakes = getMakes(attemptModels);
        lifetimeOverCuts = getOverCuts(attemptModels);
        lifetimeUnderCuts = getUnderCuts(attemptModels);
        allTimeAverage = getAverage(attemptModels);
        sixMonthAverage = getAverage(DrillModel.getAttemptsBetween(attemptModels, LAST_SIX_MONTHS, LAST_THREE_MONTHS));
        threeMonthAverage = getAverage(DrillModel.getAttemptsBetween(attemptModels, LAST_THREE_MONTHS, LAST_MONTH));
        oneMonthAverage = getAverage(DrillModel.getAttemptsBetween(attemptModels, LAST_MONTH, LAST_WEEK));
        weekAverage = getAverage(DrillModel.getAttemptsBetween(attemptModels, LAST_WEEK, TODAY));
        sessionAverage = getAverage(DrillModel.getSessionAttempts(attemptModels));

        allTimeAverageAttempts = getAttempts(attemptModels);
        sixMonthAverageAttempts = getAttempts(DrillModel.getAttemptsBetween(attemptModels, LAST_SIX_MONTHS, LAST_THREE_MONTHS));
        threeMonthAverageAttempts = getAttempts(DrillModel.getAttemptsBetween(attemptModels, LAST_THREE_MONTHS, LAST_MONTH));
        oneMonthAverageAttempts = getAttempts(DrillModel.getAttemptsBetween(attemptModels, LAST_MONTH, LAST_WEEK));
        weekAverageAttempts = getAttempts(DrillModel.getAttemptsBetween(attemptModels, LAST_WEEK, TODAY));
        sessionAverageAttempts = getAttempts(DrillModel.getSessionAttempts(attemptModels));
    }

    public AimDrillModel(DrillModel model, English english) {
        this(model, EnumSet.of(english));
    }

    public AimDrillModel(DrillModel model, EnumSet<English> englishes) {
        this(model.attemptModels, englishes, model.defaultTargetScore);
    }


    private float getAverage(Collection<DrillModel.AttemptModel> attempts) {
        if (getAttempts(attempts) == 0)
            return 0;
        else {
            return (float) getMakes(attempts) / (float) getAttempts(attempts);
        }
    }

    private int getMakes(Collection<DrillModel.AttemptModel> attempts) {
        int result = 0;
        for (DrillModel.AttemptModel attempt : attempts) {
            if (attempt.extras.containsKey(AimDrillModel.MAKES)) {
                result += attempt.extras.get(AimDrillModel.MAKES);
            }
        }

        return result;
    }

    private int getAttempts(Collection<DrillModel.AttemptModel> attemptModels) {
        int result = 0;
        for (DrillModel.AttemptModel attempt : attemptModels) {
            if (attempt.extras.containsKey(AimDrillModel.ATTEMPTS)) {
                result += attempt.extras.get(AimDrillModel.ATTEMPTS);
            }
        }

        return result;
    }

    private int getOverCuts(Collection<DrillModel.AttemptModel> attemptModels) {
        int result = 0;
        for (DrillModel.AttemptModel attempt : attemptModels) {
            if (attempt.extras.containsKey(AimDrillModel.OVER_CUTS)) {
                result += attempt.extras.get(AimDrillModel.OVER_CUTS);
            }
        }

        return result;
    }

    private int getUnderCuts(Collection<DrillModel.AttemptModel> attemptModels) {
        int result = 0;
        for (DrillModel.AttemptModel attempt : attemptModels) {
            if (attempt.extras.containsKey(AimDrillModel.UNDER_CUTS)) {
                result += attempt.extras.get(AimDrillModel.UNDER_CUTS);
            }
        }

        return result;
    }
}
