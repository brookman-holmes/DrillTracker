package com.brookmanholmes.drilltracker.presentation.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by brookman on 1/25/18.
 */

public class SpeedDrillModel {
    private final Collection<DistanceResult> speeds = new ArrayList<>();
    public int sessionAttempts, lifetimeAttempts;
    public int sessionCorrect, lifetimeCorrect;
    public int sessionHard, lifetimeHard;
    public int sessionSoft, lifetimeSoft;
    public float sessionSuccessRate, lifetimeSuccessRate;
    public float sessionAvgError, lifetimeAvgError;

    public SpeedDrillModel(DrillModel drillModel) {
        /*
        for (AttemptModel attempt : drillModel.getAttemptModels()) {
            speeds.add(attempt.distanceResult);
        }

        DrillModel sessionDrillModels = drillModel.filterBySession();
        this.sessionAttempts = sessionDrillModels.getAttemptModels().size();
        lifetimeAttempts = drillModel.getAttemptModels().size();

        sessionCorrect = getCorrectHits(sessionDrillModels.getAttemptModels());
        lifetimeCorrect = getCorrectHits(drillModel.getAttemptModels());

        sessionHard = getHardHits(sessionDrillModels.getAttemptModels());
        lifetimeHard = getHardHits(drillModel.getAttemptModels());

        sessionSoft = getSoftHits(sessionDrillModels.getAttemptModels());
        lifetimeSoft = getSoftHits(drillModel.getAttemptModels());

        sessionSuccessRate = divide(sessionCorrect, this.sessionAttempts);
        lifetimeSuccessRate = divide(lifetimeCorrect, lifetimeAttempts);

        sessionAvgError = getAverageError(sessionDrillModels.getAttemptModels());
        lifetimeAvgError = getAverageError(drillModel.getAttemptModels());

         */
    }

    public int getSpeedResultFrequency(SpeedResult speedResult) {
        return Collections.frequency(speeds, speedResult);
    }

    private float getAverageError(Collection<AttemptModel> attempts) {
        float numOfAttempts = attempts.size();
        float accumulatedError = 0;

        for (AttemptModel attempt : attempts) {
            //accumulatedError += Math.abs(attempt.speedResult.getDiamondOffset());
        }

        if (numOfAttempts == 0) {
            return 0;
        } else return accumulatedError / numOfAttempts;
    }

    private float divide(int top, int bottom) {
        if (bottom == 0) {
            return 0;
        } else {
            return (float) top / (float) bottom;
        }
    }

    private int getCorrectHits(Collection<AttemptModel> attempts) {
        int result = 0;
        for (AttemptModel attempt : attempts) {
            if (attempt.getDistanceResult().equals(DistanceResult.ZERO))
                result++;
        }

        return result;
    }

    private int getSoftHits(Collection<AttemptModel> attempts) {
        int result = 0;
        for (AttemptModel attempt : attempts) {
            if (attempt.getSpeedResult().equals(SpinResult.TOO_LITTLE))
                result++;
        }

        return result;
    }

    private int getHardHits(Collection<AttemptModel> attempts) {
        int result = 0;
        for (AttemptModel attempt : attempts) {
            if (attempt.getSpeedResult().equals(SpinResult.TOO_MUCH))
                result++;
        }

        return result;
    }
}
