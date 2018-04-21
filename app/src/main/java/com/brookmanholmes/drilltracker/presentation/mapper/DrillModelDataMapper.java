package com.brookmanholmes.drilltracker.presentation.mapper;

import com.brookmanholmes.drilltracker.domain.Drill;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Brookman Holmes on 7/7/2017.
 */

public class DrillModelDataMapper {
    private DrillModelDataMapper() {

    }

    public static Drill.Attempt transform(DrillModel.AttemptModel model) {
        return new Drill.Attempt(
                model.score,
                model.target,
                model.date,
                model.obPosition,
                model.cbPosition,
                model.extras
        );
    }

    public static Drill transform(DrillModel model) {
        return new Drill(
                model.id,
                model.name,
                model.description,
                model.imageUrl,
                Drill.Type.values()[model.drillType.ordinal()],
                model.maxScore,
                model.defaultTargetScore,
                model.obPositions,
                model.cbPositions,
                model.targetPositions,
                model.purchased
        );
    }

    public static DrillModel transform(Drill drill) {
        return new DrillModel(
                drill.getId(),
                drill.getName(),
                drill.getDescription(),
                drill.getImageUrl(),
                drill.getMaxScore(),
                drill.getDefaultTargetScore(),
                drill.getObPositions(),
                drill.getCbPositions(),
                drill.getTargetPositions(),
                DrillModel.Type.values()[drill.getType().ordinal()],
                drill.isPurchased(),
                transformAttempts(drill.getAttempts())
        );
    }

    private static DrillModel.AttemptModel transform(Drill.Attempt attempt) {
        return new DrillModel.AttemptModel(
                attempt.getScore(),
                attempt.getTarget(),
                attempt.getDate(),
                attempt.getObPosition(),
                attempt.getCbPosition(),
                attempt.getExtras()
        );
    }

    private static List<DrillModel.AttemptModel> transformAttempts(List<Drill.Attempt> attemptCollection) {
        List<DrillModel.AttemptModel> attemptModels;
        if (attemptCollection != null && !attemptCollection.isEmpty()) {
            attemptModels = new ArrayList<>();
            for (Drill.Attempt attempt : attemptCollection) {
                attemptModels.add(transform(attempt));
            }
        } else {
            attemptModels = Collections.emptyList();
        }

        return attemptModels;
    }

    public static List<DrillModel> transform(List<Drill> drillCollection) {
        List<DrillModel> drillModelCollection;
        if (drillCollection != null && !drillCollection.isEmpty()) {
            drillModelCollection = new ArrayList<>();
            for (Drill drill : drillCollection) {
                drillModelCollection.add(transform(drill));
            }
        } else {
            drillModelCollection = Collections.emptyList();
        }

        return drillModelCollection;
    }
}
