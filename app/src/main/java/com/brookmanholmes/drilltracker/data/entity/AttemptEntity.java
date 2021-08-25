package com.brookmanholmes.drilltracker.data.entity;

public class AttemptEntity {
    public int distanceResult;
    public int speedRequired;
    public int spinRequired;
    public int englishRequired;
    public int spinResult;
    public int speedResult;
    public int englishResult;
    public int thicknessResult;
    public int shotResult;
    public int targetPosition;
    public int score;
    public int target;
    public int obPosition;
    public int cbPosition;
    public long date;
    public String pattern;

    public AttemptEntity(int distanceResult, int speedRequired, int spinRequired, int englishRequired, int spinResult, int speedResult, int englishResult, int thicknessResult, int shotResult, int targetPosition, int score, int target, int obPosition, int cbPosition, long date, String pattern) {
        this.distanceResult = distanceResult;
        this.speedRequired = speedRequired;
        this.spinRequired = spinRequired;
        this.englishRequired = englishRequired;
        this.spinResult = spinResult;
        this.speedResult = speedResult;
        this.englishResult = englishResult;
        this.thicknessResult = thicknessResult;
        this.shotResult = shotResult;
        this.targetPosition = targetPosition;
        this.score = score;
        this.target = target;
        this.obPosition = obPosition;
        this.cbPosition = cbPosition;
        this.date = date;
        this.pattern = pattern;
    }

    public AttemptEntity() {

    }

    @Override
    public String toString() {
        return "AttemptEntity{" +
                "distanceResult=" + distanceResult +
                ", speedRequired=" + speedRequired +
                ", spinRequired=" + spinRequired +
                ", englishRequired=" + englishRequired +
                ", spinResult=" + spinResult +
                ", speedResult=" + speedResult +
                ", englishResult=" + englishResult +
                ", thicknessResult=" + thicknessResult +
                ", shotResult=" + shotResult +
                ", targetPosition=" + targetPosition +
                ", score=" + score +
                ", target=" + target +
                ", obPosition=" + obPosition +
                ", cbPosition=" + cbPosition +
                ", date=" + date +
                ", pattern='" + pattern + '\'' +
                '}';
    }
}
