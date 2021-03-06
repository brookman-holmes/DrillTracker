package com.brookmanholmes.drilltracker.domain.interactor;

import androidx.annotation.Nullable;

import com.brookmanholmes.drilltracker.domain.Drill;
import com.brookmanholmes.drilltracker.domain.repository.DrillRepository;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Brookman Holmes on 7/14/2017.
 * This class is an implementation of {@link UseCase} that represents a use case for adding a new
 * {@link Drill} to the repository
 */

public class AddDrill extends UseCase<Drill, AddDrill.Params> {
    private static final String TAG = AddDrill.class.getSimpleName();
    private final DrillRepository drillRepository;

    public AddDrill(DrillRepository drillRepository) {
        this.drillRepository = drillRepository;
    }

    @Override
    Observable<Drill> buildUseCaseObservable(Params params) {
        Drill drill = new Drill(
                params.id,
                params.name,
                params.description,
                params.imageUrl,
                params.type,
                params.maxScore,
                params.targetScore,
                params.obPositions,
                params.cbPositions,
                params.targetPositions,
                params.purchased,
                params.patterns

        );
        if (params.imageUrl == null) {
            return drillRepository.addDrill(drill, params.image);
        } else {
            return drillRepository.addDrill(drill);
        }
    }

    public static final class Params {
        private String id;
        private final String name;
        private final String description;
        @Nullable
        private byte[] image;
        @Nullable
        private String imageUrl;
        private final int maxScore;
        private final int targetScore;
        private final Drill.Type type;
        private final boolean purchased;
        private final int obPositions;
        private final int cbPositions;
        private final int targetPositions;
        private final List<List<Integer>> patterns = new ArrayList<>();

        Params(String name, String description, @Nullable byte[] image, DrillModel.Type type, int maxScore, int targetScore, int obPositions, int cbPositions, int targetPositions, boolean purchased) {
            this.name = name;
            this.description = description;
            this.image = image;
            this.maxScore = maxScore;
            this.targetScore = targetScore;
            this.purchased = purchased;
            this.type = map(type);
            this.cbPositions = cbPositions;
            this.obPositions = obPositions;
            this.targetPositions = targetPositions;
        }

        Params(String name, String description, @Nullable String imageUrl, DrillModel.Type type, int maxScore, int targetScore, int obPositions, int cbPositions, int targetPositions, boolean purchased) {
            this.name = name;
            this.description = description;
            this.imageUrl = imageUrl;
            this.maxScore = maxScore;
            this.targetScore = targetScore;
            this.type = map(type);
            this.purchased = purchased;
            this.obPositions = obPositions;
            this.cbPositions = cbPositions;
            this.targetPositions = targetPositions;
        }

        public static Params create(String name, String description, byte[] image, DrillModel.Type type, int maxScore, int targetScore, int obPositions, int cbPositions, int targetPositions, boolean purchased) {
            return new Params(name, description, image, type, maxScore, targetScore, obPositions, cbPositions, targetPositions, purchased);
        }

        public static Params create(String name, String description, String imageUrl, DrillModel.Type type, int maxScore, int targetScore, int obPositions, int cbPositions, int targetPositions, boolean purchased) {
            return new Params(name, description, imageUrl, type, maxScore, targetScore, obPositions, cbPositions, targetPositions, purchased);
        }

        public static Params create(String id, String name, String description, String imageUrl, DrillModel.Type type, int maxScore, int targetScore, int obPositions, int cbPositions, int targetPositions, boolean purchased) {
            Params params = new Params(name, description, imageUrl, type, maxScore, targetScore, obPositions, cbPositions, targetPositions, purchased);
            params.id = id;
            return params;
        }

        private Drill.Type map(DrillModel.Type type) {
            switch (type) {
                case ANY:
                    return Drill.Type.ANY;
                case AIMING:
                    return Drill.Type.AIMING;
                case BANKING:
                    return Drill.Type.BANKING;
                case KICKING:
                    return Drill.Type.KICKING;
                case PATTERN:
                    return Drill.Type.PATTERN;
                case POSITIONAL:
                    return Drill.Type.POSITIONAL;
                case SAFETY:
                    return Drill.Type.SAFETY;
                case SPEED:
                    return Drill.Type.SPEED;
                default:
                    throw new IllegalArgumentException("No such type: " + type);
            }
        }
    }
}
