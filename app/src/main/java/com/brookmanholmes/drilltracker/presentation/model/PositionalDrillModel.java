package com.brookmanholmes.drilltracker.presentation.model;

public class PositionalDrillModel {
    private int attempts;
    private int missOverCut, makeOverCut, make, makeUnderCut, missUnderCut;
    private int speedHard, speedSoft, speedCorrect;
    private int vSpinMore, vSpinLess, vSpinCorrect;
    private int hSpinMore, hSpinLess, hSpinCorrect;
    private int distanceZero, distanceSix, distanceTwelve, distanceEighteen, distanceTwentyFour;
    private int totalSuccessfulShots = 0; // includes distance and potting the ball

    public PositionalDrillModel(DrillModel drillModel) {
        /*
        attempts = drillModel.getAttemptModels().size();

        for (AttemptModel attempt : drillModel.getAttemptModels()) {
            if (isBallMade(attempt.shotResult)) {
                addSpeedSpinResult(attempt.speedResult);
                addHSpinResult(attempt.englishResult);
                addVSpinResult(attempt.spinResult);
                addDistanceResult(attempt.distanceResult);
                addShotResult(attempt.shotResult);

                if (attempt.distanceResult.equals(DistanceResult.ZERO))
                    totalSuccessfulShots++;
            }
        }

         */
    }

    private void addShotResult(ShotResult shotResult) {
        switch (shotResult) {
            case MISS_OVER_CUT:
                missOverCut++;
                break;
            case MAKE_OVER_CUT:
                makeOverCut++;
                break;
            case MAKE_CENTER:
                make++;
                break;
            case MAKE_UNDER_CUT:
                makeUnderCut++;
                break;
            case MISS_UNDER_CUT:
                missUnderCut++;
                break;
        }
    }

    private void addSpeedSpinResult(SpinResult spinResult) {
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

    private void addVSpinResult(SpinResult spinResult) {
        switch (spinResult) {
            case TOO_LITTLE:
                vSpinLess++;
                break;
            case CORRECT:
                vSpinCorrect++;
                break;
            case TOO_MUCH:
                vSpinMore++;
                break;
        }
    }

    private void addHSpinResult(SpinResult spinResult) {
        switch (spinResult) {
            case TOO_LITTLE:
                hSpinLess++;
                break;
            case CORRECT:
                hSpinCorrect++;
                break;
            case TOO_MUCH:
                hSpinMore++;
                break;
        }
    }

    private void addDistanceResult(DistanceResult distanceResult) {
        switch (distanceResult) {
            case ZERO:
                distanceZero++;
                break;
            case ONE_HALF:
                distanceSix++;
                break;
            case ONE:
                distanceTwelve++;
                break;
            case ONE_AND_HALF:
                distanceEighteen++;
                break;
            case OVER_ONE_AND_HALF:
                distanceTwentyFour++;
                break;
        }
    }

    public float getPottingAverage() {
        if (getAttempts() == 0) {
            return 0;
        } else {
            return (float) (getMake() + getMakeOverCut() + getMakeUnderCut()) / (float) getAttempts();
        }
    }

    public float getTotalSuccessAverage() {
        if (getAttempts() == 0)
            return 0;
        else return (float) totalSuccessfulShots / (float) getAttempts();
    }

    public int getMissOverCut() {
        return missOverCut;
    }

    public int getMakeOverCut() {
        return makeOverCut;
    }

    public int getMake() {
        return make;
    }

    public int getMakeUnderCut() {
        return makeUnderCut;
    }

    public int getMissUnderCut() {
        return missUnderCut;
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

    private boolean isBallMade(ShotResult shotResult) {
        return (shotResult.equals(ShotResult.MAKE_CENTER)
                || shotResult.equals(ShotResult.MAKE_OVER_CUT)
                || shotResult.equals(ShotResult.MAKE_UNDER_CUT));
    }
}
