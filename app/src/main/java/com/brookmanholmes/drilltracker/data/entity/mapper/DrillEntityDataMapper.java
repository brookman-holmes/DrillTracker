package com.brookmanholmes.drilltracker.data.entity.mapper;

import com.brookmanholmes.drilltracker.data.entity.DrillEntity;
import com.brookmanholmes.drilltracker.domain.Drill;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        return new DrillEntity.AttemptEntity(
                attempt.getScore(),
                attempt.getTarget(),
                attempt.getDate().getTime(),
                attempt.getObPosition(),
                attempt.getCbPosition(),
                attempt.getExtras()
        );
    }

    public Map<String, String> transform(List<List<Integer>> patterns) {
        Map<String, String> result = new HashMap<>();
        int count = 0;
        for (List<Integer> pattern : patterns) {
            String string = "";
            for (int i = 0; i < pattern.size(); i++) {
                string += pattern.get(i);
                if (i + 1 != pattern.size())
                    string += ",";
            }
            result.put("pattern" + count++, string);
        }

        return result;
    }

    /**
     * Transform a {@link DrillEntity} into an {@link Drill}.
     *
     * @param entity Object to be transformed.
     */
    public Drill transform(DrillEntity entity) {
        final Drill drill = new Drill(
                entity.id,
                entity.name,
                entity.description,
                entity.imageUrl,
                transform(entity.type),
                entity.maxScore,
                entity.targetScore,
                entity.obPositions,
                entity.cbPositions,
                entity.targetPositions,
                entity.purchased,
                transform(entity.patterns)
        );
        drill.setAttempts(transformAttempts(entity.attempts.values()));
        return drill;
    }

    private Drill.Attempt transform(DrillEntity.AttemptEntity attemptEntity) {
        return new Drill.Attempt(attemptEntity.score, attemptEntity.target, new Date(attemptEntity.date), attemptEntity.obPosition, attemptEntity.cbPosition, attemptEntity.extras);
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
     * Transforms a list of patterns encoded as integers into a list of a list of integers
     * A pattern would be encoded as: 1,2,3,4,5 etc. with , used as a delimiter
     *
     * @param patterns The list of patterns
     * @return A list of a list of integers representing a list of patterns for a run out
     */
    private List<List<Integer>> transform(Map<String, String> patterns) {
        List<List<Integer>> result = new ArrayList<>();

        for (String pattern : patterns.values()) {
            List<Integer> list = new ArrayList<>();
            String[] strings = StringUtils.split(pattern, ",");
            for (String string : strings) {
                list.add(Integer.valueOf(string));
            }
            result.add(list);
        }

        return result;
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
