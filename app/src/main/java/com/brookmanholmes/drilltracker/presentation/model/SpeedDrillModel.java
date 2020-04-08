package com.brookmanholmes.drilltracker.presentation.model;

import android.util.Pair;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by brookman on 1/25/18.
 */

public class SpeedDrillModel {
    private static final String TWO_DIAMOND_SOFT = "TWO_DIAMOND_SOFT";
    private static final String ONE_DIAMOND_SOFT = "DIAMOND_SOFT";
    private static final String HALF_DIAMOND_SOFT = "HALF_DIAMOND_SOFT";
    private static final String QUARTER_DIAMOND_SOFT = "QUARTER_DIAMOND_SOFT";
    private static final String CORRECT = "CORRECT";
    private static final String QUARTER_DIAMOND_HARD = "QUARTER_DIAMOND_HARD";
    private static final String HALF_DIAMOND_HARD = "HALF_DIAMOND_HARD";
    private static final String ONE_DIAMOND_HARD = "DIAMOND_HARD";
    private static final String TWO_DIAMOND_HARD = "TWO_DIAMOND_HARD";

    public final Map<Speed, Integer> speeds;

    public int sessionAttempts, lifetimeAttempts;
    public int sessionCorrect, lifetimeCorrect;
    public int sessionHard, lifetimeHard;
    public int sessionSoft, lifetimeSoft;
    public float sessionSuccessRate, lifetimeSuccessRate;
    public float sessionAvgError, lifetimeAvgError;

    public static DrillModel.AttemptModel createAttempt(int obPosition, int cbPosition, int twoDiamondsSoftAttempts,
                                                        int diamondSoftAttempts, int halfDiamondSoftAttempts, int quarterDiamondSoftAttempts,
                                                        int correctSpeedAttempts, int quarterDiamondHardAttempts, int halfDiamondHardAttempts,
                                                        int diamondHardAttempts, int twoDiamondsHardAttempts) {
        return new DrillModel.AttemptModel(
                0,
                0,
                new Date(),
                obPosition,
                cbPosition,
                new Pair<>(TWO_DIAMOND_HARD, twoDiamondsHardAttempts),
                new Pair<>(ONE_DIAMOND_HARD, diamondHardAttempts),
                new Pair<>(HALF_DIAMOND_HARD, halfDiamondHardAttempts),
                new Pair<>(QUARTER_DIAMOND_HARD, quarterDiamondHardAttempts),
                new Pair<>(CORRECT, correctSpeedAttempts),
                new Pair<>(QUARTER_DIAMOND_SOFT, quarterDiamondSoftAttempts),
                new Pair<>(HALF_DIAMOND_SOFT, halfDiamondSoftAttempts),
                new Pair<>(ONE_DIAMOND_SOFT, diamondSoftAttempts),
                new Pair<>(TWO_DIAMOND_SOFT, twoDiamondsSoftAttempts)
        );
    }

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
