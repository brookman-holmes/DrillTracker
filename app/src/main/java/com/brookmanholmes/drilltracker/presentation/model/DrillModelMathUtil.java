package com.brookmanholmes.drilltracker.presentation.model;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by Brookman Holmes on 7/13/2017.
 */

public class DrillModelMathUtil {
    private static final String TAG = DrillModelMathUtil.class.getName();
    private int max = 0;
    private int attempts = 0;
    private float average = 0;
    private float median = 0;

    public DrillModelMathUtil(Collection<AttemptModel> attempts) {
        this.max = getMax(attempts);
        this.attempts = getAttempts(attempts);
        this.average = getAverage(attempts);
        this.median = getMedian(attempts);
    }


    public int getMax() {
        return max;
    }

    public int getAttempts() {
        return attempts;
    }

    public float getAverage() {
        return average;
    }

    public float getMedian() {
        return median;
    }

    private int getMax(Collection<AttemptModel> attempts) {
        int max = 0;
        for (AttemptModel attempt : attempts)
            if (attempt.getScore() > max)
                max = attempt.getScore();

        return max;
    }

    private int getAttempts(Collection<AttemptModel> attempts) {
        return attempts.size();
    }

    private float getAverage(Collection<AttemptModel> attempts) {
        float count = attempts.size();
        float sum = 0;

        for (AttemptModel attempt : attempts) {
            sum += attempt.getScore();
        }

        return count > 0 ? sum / count : 0;
    }

    private float getMedian(Collection<AttemptModel> attempts) {
        if (attempts.size() == 0)
            return 0;

        int[] array = new int[attempts.size()];
        int count = 0;
        for (AttemptModel attempt : attempts) {
            array[count] = attempt.getScore();
            count++;
        }
        Arrays.sort(array);

        if (array.length % 2 == 0) {
            int x, y;

            x = array[array.length / 2];
            y = array[(array.length / 2) - 1];

            return (x + y) / 2;
        } else {
            return array[(array.length - 1)/2];
        }
    }
}
