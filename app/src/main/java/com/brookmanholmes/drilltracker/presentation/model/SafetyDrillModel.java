package com.brookmanholmes.drilltracker.presentation.model;

import android.util.Pair;

import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;

/**
 * Created by brookman on 1/6/18.
 */

public class SafetyDrillModel {
    private static final String SAFETY_TYPES = "safetyTypes";
    private final int attempts;
    private int speedHard, speedSoft, speedCorrect;
    private int spinLess, spinMore, spinCorrect;
    private int thicknessThin, thicknessThick, thicknessCorrect;

    public SafetyDrillModel(Collection<DrillModel.AttemptModel> attemptModels) {
        attempts = attemptModels.size();
        for (DrillModel.AttemptModel attemptModel : attemptModels) {
            Integer code = attemptModel.extras.get(SAFETY_TYPES);
            EnumSet<SafetyTypes> safetyTypes = decode(code == null ? 0 : code);
            for (SafetyTypes type : safetyTypes) {
                addSafetyAttempt(type);
            }
        }
    }

    private static int encode(EnumSet<SafetyTypes> set) {
        int result = 0;

        for (SafetyTypes val : set) {
            result |= 1 << val.ordinal();
        }

        return result;
    }

    private static EnumSet<SafetyTypes> decode(int code) {
        EnumSet<SafetyTypes> result = EnumSet.noneOf(SafetyTypes.class);
        while (code != 0) {
            int ordinal = Integer.numberOfTrailingZeros(code);
            code ^= Integer.lowestOneBit(code);
            result.add(SafetyTypes.values()[ordinal]);
        }

        return result;
    }

    public static DrillModel.AttemptModel createAttempt(int obPosition, int cbPosition, EnumSet<SafetyTypes> safetyTypes) {
        return new DrillModel.AttemptModel(0, 0, new Date(), obPosition, cbPosition,
                new Pair<>(SAFETY_TYPES, encode(safetyTypes)));
    }

    private void addSafetyAttempt(SafetyTypes type) {
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
            case SPIN_LESS:
                spinLess++;
                break;
            case SPIN_MORE:
                spinMore++;
                break;
            case SPIN_CORRECT:
                spinCorrect++;
                break;
            case THICKNESS_THIN:
                thicknessThin++;
                break;
            case THICKNESS_THICK:
                thicknessThick++;
                break;
            case THICKNESS_CORRECT:
                thicknessCorrect++;
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

    public int getSpinLess() {
        return spinLess;
    }

    public int getSpinMore() {
        return spinMore;
    }

    public int getSpinCorrect() {
        return spinCorrect;
    }

    public int getThicknessThin() {
        return thicknessThin;
    }

    public int getThicknessThick() {
        return thicknessThick;
    }

    public int getThicknessCorrect() {
        return thicknessCorrect;
    }

    public enum SafetyTypes {
        SPEED_HARD,
        SPEED_SOFT,
        SPEED_CORRECT,
        SPIN_LESS,
        SPIN_MORE,
        SPIN_CORRECT,
        THICKNESS_THIN,
        THICKNESS_THICK,
        THICKNESS_CORRECT
    }
}
