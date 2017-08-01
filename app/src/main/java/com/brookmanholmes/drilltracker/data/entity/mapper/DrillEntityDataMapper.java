package com.brookmanholmes.drilltracker.data.entity.mapper;

import com.brookmanholmes.drilltracker.data.entity.DrillEntity;
import com.brookmanholmes.drilltracker.domain.Drill;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by Brookman Holmes on 7/9/2017.
 */

public class DrillEntityDataMapper {
    public DrillEntityDataMapper() {
    }

    public DrillEntity.AttemptEntity transform(Drill.Attempt attempt) {
        final DrillEntity.AttemptEntity entity = new DrillEntity.AttemptEntity();
        entity.score = attempt.getScore();
        entity.date = attempt.getDate().getTime();
        entity.target = attempt.getTarget();

        return entity;
    }

    public Drill transform(DrillEntity entity) {
        final Drill drill = new Drill(entity.id, entity.name, entity.description, entity.imageUrl, transform(entity.type), entity.maxScore, entity.targetScore);
        drill.setAttempts(transformAttempts(entity.attempts.values()));
        return drill;
    }

    private Drill.Attempt transform(DrillEntity.AttemptEntity attemptEntity) {
        Drill.Attempt attempt = new Drill.Attempt(attemptEntity.score, attemptEntity.target, new Date(attemptEntity.date));
        return attempt;
    }

    private List<Drill.Attempt> transformAttempts(Collection<DrillEntity.AttemptEntity> attemptEntities) {
        final List<Drill.Attempt> attempts = new ArrayList<>();
        for (DrillEntity.AttemptEntity attemptEntity : attemptEntities) {
            final Drill.Attempt attempt = transform(attemptEntity);
            if (attempt != null)
                attempts.add(attempt);
        }

        return attempts;
    }

    public List<Drill> transform(Collection<DrillEntity> drillEntityList) {
        final List<Drill> drillList = new ArrayList<>();
        for (DrillEntity entity : drillEntityList) {
            final Drill drill = transform(entity);
            if (drill != null)
                drillList.add(drill);
        }

        return drillList;
    }

    public String transform(Drill.Type type) {
        switch (type) {
            case POSITIONAL:
                return "POSITIONAL";
            case SAFETY:
                return "SAFETY";
            case AIMING:
                return "AIMING";
            case PATTERN:
                return "PATTERN";
            case KICKING:
                return "KICKING";
            case BANKING:
                return "BANKING";
            case SPEED:
                return "SPEED";
            case ANY:
                return "ANY";
            default:
                throw new IllegalArgumentException("Drill.Type " + type + " does not exist");
        }
    }

    public String transform(DrillModel.Type type) {
        switch (type) {
            case POSITIONAL:
                return "POSITIONAL";
            case SAFETY:
                return "SAFETY";
            case AIMING:
                return "AIMING";
            case PATTERN:
                return "PATTERN";
            case KICKING:
                return "KICKING";
            case BANKING:
                return "BANKING";
            case SPEED:
                return "SPEED";
            case ANY:
                return "ANY";
            default:
                throw new IllegalArgumentException("Drill.Type " + type + " does not exist");
        }
    }

    private Drill.Type transform(String type) {
        switch(type) {
            case "POSITIONAL":
                return Drill.Type.POSITIONAL;
            case "SAFETY":
                return Drill.Type.SAFETY;
            case "AIMING":
                return Drill.Type.AIMING;
            case "PATTERN":
                return Drill.Type.PATTERN;
            case "KICKING":
                return Drill.Type.KICKING;
            case "BANKING":
                return Drill.Type.BANKING;
            case "SPEED":
                return Drill.Type.SPEED;
            case "ANY":
                return Drill.Type.ANY;
            default:
                throw new IllegalArgumentException("Drill.Type " + type + " does not exist");
        }
    }
}
