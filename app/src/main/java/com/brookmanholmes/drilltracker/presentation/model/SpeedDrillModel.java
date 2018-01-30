package com.brookmanholmes.drilltracker.presentation.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by brookman on 1/25/18.
 */

public class SpeedDrillModel {
    public int attempts;
    public Map<Speed, Integer> speeds;

    private SpeedDrillModel() {
        speeds = new HashMap<>();
        generateZeroedMap();
    }

    public SpeedDrillModel(Collection<DrillModel.AttemptModel> attempts) {
        this();
        this.attempts = attempts.size();

        for (DrillModel.AttemptModel attemptModel : attempts) {
            for (Speed speed : Speed.values()) {
                if (attemptModel.extras.containsKey(speed.toString())) {
                    addSpeedAttempt(speed, attemptModel.extras.get(speed.toString()));
                }
            }
        }
    }

    public static SpeedDrillModel generateDummyData() {
        SpeedDrillModel result = new SpeedDrillModel();
        Random random = new Random();


        for (Speed speed : Speed.values()) {
            int attempts = random.nextInt(10);
            result.attempts += attempts;
            result.speeds.put(speed, attempts);
        }

        return result;
    }

    private void generateZeroedMap() {
        for (Speed speed : Speed.values()) {
            speeds.put(speed, 0);
        }
    }

    private void addSpeedAttempt(Speed speed, int attempts) {
        if (speeds.containsKey(speed)) {
            speeds.put(speed, speeds.get(speed) + attempts);
        }
    }
}
