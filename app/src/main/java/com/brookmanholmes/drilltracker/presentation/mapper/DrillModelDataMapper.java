package com.brookmanholmes.drilltracker.presentation.mapper;

import com.brookmanholmes.drilltracker.domain.Drill;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Brookman Holmes on 7/7/2017.
 */

public class DrillModelDataMapper {
    public DrillModelDataMapper() {

    }

    public DrillModel transform(Drill drill) {
        final DrillModel drillModel = new DrillModel();
        drillModel.id = drill.getId();
        drillModel.name = drill.getName();
        drillModel.imageUrl = drill.getImageUrl();
        drillModel.maxScore = drill.getMaxScore();
        drillModel.attemptModels = transformAttempts(drill.getAttempts());
        drillModel.description = drill.getDescription();
        drillModel.defaultTargetScore = drill.getDefaultTargetScore();
        drillModel.drillType = DrillModel.Type.values()[drill.getType().ordinal()];
        drillModel.purchased = drill.isPurchased();
        return drillModel;
    }

    private DrillModel.AttemptModel transform(Drill.Attempt attempt) {
        final DrillModel.AttemptModel attemptModel = new DrillModel.AttemptModel();
        attemptModel.target = attempt.getTarget();
        attemptModel.score = attempt.getScore();
        attemptModel.dateString = DateFormat.getDateInstance().format(attempt.getDate());
        attemptModel.date = attempt.getDate();
        return attemptModel;
    }

    private List<DrillModel.AttemptModel> transformAttempts(List<Drill.Attempt> attemptCollection) {
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

    public List<DrillModel> transform(List<Drill> drillCollection) {
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
