package com.brookmanholmes.drilltracker.presentation.model;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

public class PositionalDrillModel {
    private static final String POSITIONAL_TYPES = "positionalTypes";
    private static final String TARGET_POSITION = "targetPosition";

    private final int attempts;
    private int speedHard, speedSoft, speedCorrect;
    private int vSpinMore, vSpinLess, vSpinCorrect;
    private int hSpinMore, hSpinLess, hSpinCorrect;
    private int distanceZero, distanceSix, distanceTwelve, distanceEighteen, distanceTwentyFour;

    private PositionalDrillModel(Collection<DrillModel.AttemptModel> attemptModels) {
        attempts = attemptModels.size();

        for (DrillModel.AttemptModel attemptModel : attemptModels) {
            Integer code = attemptModel.extras.get(POSITIONAL_TYPES);
            EnumSet<PositionalTypes> positionalTypes = decode(code == null ? 0 : code);
            for (PositionalTypes type : positionalTypes) {
                addPositionalAttempt(type);
            }
        }
    }

    private static int encode(EnumSet<PositionalTypes> set) {
        int result = 0;

        for (PositionalTypes val : set) {
            result |= 1 << val.ordinal();
        }

        return result;
    }

    private static EnumSet<PositionalTypes> decode(int code) {
        EnumSet<PositionalTypes> result = EnumSet.noneOf(PositionalTypes.class);
        while (code != 0) {
            int ordinal = Integer.numberOfTrailingZeros(code);
            code ^= Integer.lowestOneBit(code);
            result.add(PositionalTypes.values()[ordinal]);
        }

        return result;
    }

    public static DrillModel.AttemptModel createAttempt(int cbPosition, int targetPosition, EnumSet<PositionalTypes> positionalTypes) {
        return new DrillModel.AttemptModel(0, 0, new Date(), 0, cbPosition,
                new Pair<>(POSITIONAL_TYPES, encode(positionalTypes)),
                new Pair<>(TARGET_POSITION, targetPosition));
    }

    public static PositionalDrillModel filterByTargetPosition(Collection<DrillModel.AttemptModel> attemptModels, int targetPosition) {
        List<DrillModel.AttemptModel> filteredAttempts = new ArrayList<>();
        for (DrillModel.AttemptModel attempt : attemptModels) {
            if (attempt.extras.containsKey(TARGET_POSITION)) {
                if (targetPosition == 0 || attempt.extras.get(TARGET_POSITION) == targetPosition) {
                    filteredAttempts.add(attempt);
                }
            }
        }

        return new PositionalDrillModel(filteredAttempts);
    }

    private void addPositionalAttempt(PositionalTypes type) {
        switch (type) {
            case SPEED_HARD:
                speedHard++;
                break;
            case SPEED_SOFT:
                speedSoft++;
                break;
            case SPEED_CORRECT:
                speedCorrect++;
                break;
            case V_SPIN_LESS:
                vSpinLess++;
                break;
            case V_SPIN_MORE:
                vSpinMore++;
                break;
            case V_SPIN_CORRECT:
                vSpinCorrect++;
                break;
            case H_SPIN_LESS:
                hSpinLess++;
                break;
            case H_SPIN_MORE:
                hSpinMore++;
                break;
            case H_SPIN_CORRECT:
                hSpinCorrect++;
                break;
            case ZERO_INCHES:
                distanceZero++;
                break;
            case SIX_INCHES:
                distanceSix++;
                break;
            case TWELVE_INCHES:
                distanceTwelve++;
                break;
            case EIGHTEEN_INCHES:
                distanceEighteen++;
                break;
            case TWENTY_FOUR_INCHES:
                distanceTwentyFour++;
                break;
        }
    }

    public int getAttempts() {
        return attempts;
    }

    public int getSpeedHard() {
        return speedHard;
    }

    public int getSpeedSoft() {
        return speedSoft;
    }

    public int getSpeedCorrect() {
        return speedCorrect;
    }

    public int getvSpinMore() {
        return vSpinMore;
    }

    public int getvSpinLess() {
        return vSpinLess;
    }

    public int getvSpinCorrect() {
        return vSpinCorrect;
    }

    public int gethSpinMore() {
        return hSpinMore;
    }

    public int gethSpinLess() {
        return hSpinLess;
    }

    public int gethSpinCorrect() {
        return hSpinCorrect;
    }

    public int getDistanceZero() {
        return distanceZero;
    }

    public int getDistanceSix() {
        return distanceSix;
    }

    public int getDistanceTwelve() {
        return distanceTwelve;
    }

    public int getDistanceEighteen() {
        return distanceEighteen;
    }

    public int getDistanceTwentyFour() {
        return distanceTwentyFour;
    }

    public enum PositionalTypes {
        SPEED_HARD,
        SPEED_SOFT,
        SPEED_CORRECT,
        V_SPIN_LESS,
        V_SPIN_MORE,
        V_SPIN_CORRECT,
        H_SPIN_LESS,
        H_SPIN_MORE,
        H_SPIN_CORRECT,
        ZERO_INCHES,
        SIX_INCHES,
        TWELVE_INCHES,
        EIGHTEEN_INCHES,
        TWENTY_FOUR_INCHES
    }
}
