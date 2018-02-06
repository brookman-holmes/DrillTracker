package com.brookmanholmes.drilltracker.presentation.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by brookman on 1/25/18.
 */

public class SpeedDrillModel {
    public Map<Speed, Integer> speeds;

    public int sessionAttempts, lifetimeAttempts;
    public int sessionCorrect, lifetimeCorrect;
    public int sessionHard, lifetimeHard;
    public int sessionSoft, lifetimeSoft;
    public float sessionSuccessRate, lifetimeSuccessRate;
    public float sessionAvgError, lifetimeAvgError;

    private SpeedDrillModel() {
        speeds = new HashMap<>();
        generateZeroedMap();
    }

    public SpeedDrillModel(Collection<DrillModel.AttemptModel> attempts) {
        this();
        Collection<DrillModel.AttemptModel> sessionAttempts = DrillModel.getSessionAttempts(attempts);

        for (DrillModel.AttemptModel attempt : attempts) {
            for (Speed speed : Speed.values()) {
                if (attempt.extras.containsKey(speed.toString())) {
                    addSpeedAttempt(speed, attempt.extras.get(speed.toString()));
                }
            }
        }

        this.sessionAttempts = getAttempts(sessionAttempts);
        lifetimeAttempts = getAttempts(attempts);

        sessionCorrect = getCorrectHits(sessionAttempts);
        lifetimeCorrect = getCorrectHits(attempts);

        sessionHard = getHardHits(sessionAttempts);
        lifetimeHard = getHardHits(attempts);

        sessionSoft = getSoftHits(sessionAttempts);
        lifetimeSoft = getSoftHits(attempts);

        sessionSuccessRate = divide(sessionCorrect, this.sessionAttempts);
        lifetimeSuccessRate = divide(lifetimeCorrect, lifetimeAttempts);

        sessionAvgError = getAverageError(sessionAttempts);
        lifetimeAvgError = getAverageError(attempts);
    }

    private void generateZeroedMap() {
        for (Speed speed : Speed.values()) {
            speeds.put(speed, 0);
        }
    }

    private float getAverageError(Collection<DrillModel.AttemptModel> attempts) {
        float numOfAttempts = 0;
        float accumulatedError = 0;

        for (DrillModel.AttemptModel attempt : attempts) {
            for (Speed speed : Speed.values()) {
                if (attempt.extras.containsKey(speed.toString())) {
                    numOfAttempts += attempt.extras.get(speed.toString());
                    accumulatedError += Math.abs(speed.getDiamondOffset() * (float) attempt.extras.get(speed.toString()));
                }
            }
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

    private void addSpeedAttempt(Speed speed, int attempts) {
        if (speeds.containsKey(speed)) {
            speeds.put(speed, speeds.get(speed) + attempts);
        }
    }

    private int getAttempts(Collection<DrillModel.AttemptModel> attempts) {
        int result = 0;
        for (DrillModel.AttemptModel attempt : attempts) {
            for (Speed speed : Speed.values()) {
                if (attempt.extras.containsKey(speed.toString())) {
                    result += attempt.extras.get(speed.toString());
                }
            }
        }

        return result;
    }

    private int getCorrectHits(Collection<DrillModel.AttemptModel> attempts) {
        int result = 0;
        for (DrillModel.AttemptModel attempt : attempts) {
            if (attempt.extras.containsKey(Speed.CORRECT.toString())) {
                result += attempt.extras.get(Speed.CORRECT.toString());
            }
        }

        return result;
    }

    private int getSoftHits(Collection<DrillModel.AttemptModel> attempts) {
        int result = 0;
        for (DrillModel.AttemptModel attempt : attempts) {
            for (Speed speed : Speed.getSoftHits()) {
                if (attempt.extras.containsKey(speed.toString())) {
                    result += attempt.extras.get(speed.toString());
                }
            }
        }

        return result;
    }

    private int getHardHits(Collection<DrillModel.AttemptModel> attempts) {
        int result = 0;
        for (DrillModel.AttemptModel attempt : attempts) {
            for (Speed speed : Speed.getHardHits()) {
                if (attempt.extras.containsKey(speed.toString())) {
                    result += attempt.extras.get(speed.toString());
                }
            }
        }

        return result;
    }
}
