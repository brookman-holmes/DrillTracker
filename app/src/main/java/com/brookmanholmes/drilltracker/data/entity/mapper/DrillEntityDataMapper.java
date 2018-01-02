package com.brookmanholmes.drilltracker.data.entity.mapper;

import com.brookmanholmes.drilltracker.data.entity.DrillEntity;
import com.brookmanholmes.drilltracker.domain.Drill;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Mapper class used to transform {@link DrillEntity} (in the data layer) to {@link Drill} in the
 * domain layer.
 */
public class DrillEntityDataMapper {
    public DrillEntityDataMapper() {
    }

    /**
     * Transform a {@link DrillEntity.AttemptEntity} into an {@link Drill.Attempt}.
     *
     * @param attempt Object to be transformed.
     */
    public DrillEntity.AttemptEntity transform(Drill.Attempt attempt) {
        final DrillEntity.AttemptEntity entity = new DrillEntity.AttemptEntity();
        entity.score = attempt.getScore();
        entity.date = attempt.getDate().getTime();
        entity.target = attempt.getTarget();

        return entity;
    }

    /**
     * Transform a {@link DrillEntity} into an {@link Drill}.
     *
     * @param entity Object to be transformed.
     */
    public Drill transform(DrillEntity entity) {
        final Drill drill = new Drill(entity.id, entity.name, entity.description, entity.imageUrl, transform(entity.type), entity.maxScore, entity.targetScore, entity.purchased);
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

    /**
     * Transform a list of {@link DrillEntity} into an list of {@link Drill}.
     *
     * @param drillEntityList Object to be transformed.
     */
    public List<Drill> transform(Collection<DrillEntity> drillEntityList) {
        final List<Drill> drillList = new ArrayList<>();
        for (DrillEntity entity : drillEntityList) {
            if (isEntityValid(entity))
                drillList.add(transform(entity));
        }

        return drillList;
    }

    /**
     * Transform a {@link Drill.Type} into a String
     *
     * @param type The type to transform
     */
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

    /**
     * Transform a {@link DrillModel.Type} into a string
     * @param type The type to transform
     */
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

    private boolean isEntityValid(DrillEntity entity) {
        return entity != null &&
                entity.description != null &&
                entity.attempts != null &&
                entity.id != null &&
                entity.imageUrl != null &&
                entity.name != null &&
                entity.type != null;
    }
}
