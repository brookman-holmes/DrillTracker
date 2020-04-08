package com.brookmanholmes.drilltracker.presentation.model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;

public class PatternEntry implements Serializable {
    private int positionInPattern = 0;
    private int ball = 0;
    private boolean made = false;
    private int rating = 0;

    /**
     * Constructor for an empty entry that's rotational
     *
     * @param ball
     */
    public PatternEntry(int ball) {
        this.positionInPattern = ball - 1;
        this.ball = ball;
    }

    /**
     * Constructor for non rotation game
     *
     * @param positionInPattern
     * @param ball
     * @param made
     * @param rating
     */
    public PatternEntry(int positionInPattern, int ball, boolean made, int rating) {
        this.positionInPattern = positionInPattern;
        this.ball = ball;
        this.made = made;
        this.rating = rating;
    }

    /**
     * Constructor for a rotation pattern
     *
     * @param ball
     */
    public PatternEntry(int ball, boolean made, int rating) {
        this(ball - 1, ball, made, rating);
    }

    public int getBall() {
        return ball;
    }

    public void setBall(int ball) {
        this.ball = ball;
    }

    public boolean isMade() {
        return made;
    }

    public void setMade(boolean made) {
        this.made = made;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getPositionInPattern() {
        return positionInPattern;
    }

    public void setPositionInPattern(int positionInPattern) {
        this.positionInPattern = positionInPattern;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatternEntry that = (PatternEntry) o;
        return ball == that.ball &&
                made == that.made &&
                rating == that.rating;
    }

    @Override
    public int hashCode() {

        return Objects.hash(ball, made, rating);
    }

    @NonNull
    @Override
    public String toString() {
        return "PatternEntry{" +
                "position=" + positionInPattern +
                ", ball=" + ball +
                ", made=" + made +
                ", rating=" + rating +
                '}';
    }
}
