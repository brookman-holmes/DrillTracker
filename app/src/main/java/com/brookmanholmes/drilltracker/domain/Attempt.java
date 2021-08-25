package com.brookmanholmes.drilltracker.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Attempt {
    private final int distanceResult;
    private final int speedRequired;
    private final int spinRequired;
    private final int englishResult;
    private final int speedResult;
    private final int spinResult;
    private final int thicknessResult;
    private final int englishRequired;
    private final int shotResult;
    private final int targetPosition;
    private final List<Integer> pattern;
    private final int score;
    private final int target;
    private final Date date;
    private final int obPosition;
    private final int cbPosition;

    public Attempt(int distanceResult, int speedRequired, int spinRequired, int englishResult, int spinResult,
                   int speedResult, int thicknessResult, int englishRequired, int shotResult, int targetPosition,
                   int score, int target, Date date, int obPosition, int cbPosition, List<Integer> pattern) {
        this.distanceResult = distanceResult;
        this.speedRequired = speedRequired;
        this.spinRequired = spinRequired;
        this.englishResult = englishResult;
        this.speedResult = speedResult;
        this.spinResult = spinResult;
        this.thicknessResult = thicknessResult;
        this.englishRequired = englishRequired;
        this.shotResult = shotResult;
        this.targetPosition = targetPosition;
        this.score = score;
        this.target = target;
        this.date = new Date(date.getTime());
        this.obPosition = obPosition;
        this.cbPosition = cbPosition;
        this.pattern = new ArrayList<>(pattern);
    }

    public int getDistanceResult() {
        return distanceResult;
    }

    public int getSpeedRequired() {
        return speedRequired;
    }

    public int getSpinRequired() {
        return spinRequired;
    }

    public int getSpinResult() {
        return spinResult;
    }

    public int getEnglishResult() {
        return englishResult;
    }

    public int getSpeedResult() {
        return speedResult;
    }

    public int getThicknessResult() {
        return thicknessResult;
    }

    public int getEnglishRequired() {
        return englishRequired;
    }

    public int getShotResult() {
        return shotResult;
    }

    public int getTargetPosition() {
        return targetPosition;
    }

    public List<Integer> getPattern() {
        return pattern;
    }

    public int getScore() {
        return score;
    }

    public int getTarget() {
        return target;
    }

    public Date getDate() {
        return date;
    }

    public int getObPosition() {
        return obPosition;
    }

    public int getCbPosition() {
        return cbPosition;
    }

    @Override
    public String toString() {
        return "Attempt{" +
                "distanceResult=" + distanceResult +
                ", speedUsed=" + speedRequired +
                ", vSpinResult=" + spinRequired +
                ", hSpinResult=" + englishResult +
                ", speedSpinResult=" + speedResult +
                ", englishUsed=" + englishRequired +
                ", shotResult=" + shotResult +
                ", targetPosition=" + targetPosition +
                ", pattern=" + pattern +
                ", score=" + score +
                ", target=" + target +
                ", date=" + date +
                ", obPosition=" + obPosition +
                ", cbPosition=" + cbPosition +
                '}';
    }
}
