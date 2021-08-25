package com.brookmanholmes.drilltracker.presentation.model;

/**
 * Created by brookman on 1/6/18.
 */

public class SafetyDrillModel {
    // thickness is vertical spin for safeties
    private final int attempts;
    private int speedHard, speedSoft, speedCorrect;
    private int spinLess, spinMore, spinCorrect;
    private int thicknessThin, thicknessThick, thicknessCorrect;

    public SafetyDrillModel(DrillModel drillModel) {
        /*
        attempts = drillModel.getAttemptModels().size();
        for (AttemptModel attemptModel : drillModel.getAttemptModels()) {
            addHSpinResult(attemptModel.englishResult);
            addSpeedResult(attemptModel.speedResult);
            addThicknessResult(attemptModel.spinResult);
        }

         */
        attempts = 0;
    }

    private void addSpeedResult(SpinResult spinResult) {
        switch (spinResult) {
            case TOO_LITTLE:
                speedSoft++;
                break;
            case CORRECT:
                speedCorrect++;
                break;
            case TOO_MUCH:
                speedHard++;
                break;
        }
    }

    private void addThicknessResult(SpinResult spinResult) {
        switch (spinResult) {
            case TOO_LITTLE:
                thicknessThin++;
                break;
            case CORRECT:
                thicknessCorrect++;
                break;
            case TOO_MUCH:
                thicknessThick++;
                break;
        }
    }

    private void addHSpinResult(SpinResult spinResult) {
        switch (spinResult) {
            case TOO_LITTLE:
                spinLess++;
                break;
            case CORRECT:
                spinCorrect++;
                break;
            case TOO_MUCH:
                spinMore++;
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
}
