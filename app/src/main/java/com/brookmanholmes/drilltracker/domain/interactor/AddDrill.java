package com.brookmanholmes.drilltracker.domain.interactor;

import android.support.annotation.Nullable;

import com.brookmanholmes.drilltracker.data.entity.mapper.DrillEntityDataMapper;
import com.brookmanholmes.drilltracker.domain.Drill;
import com.brookmanholmes.drilltracker.domain.repository.DrillRepository;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;

import io.reactivex.Observable;

/**
 * Created by Brookman Holmes on 7/14/2017.
 * This class is an implementation of {@link UseCase} that represents a use case for adding a new
 * {@link Drill} to the repository
 */

public class AddDrill extends UseCase<Drill, AddDrill.Params> {
    private final DrillRepository drillRepository;

    public AddDrill(DrillRepository drillRepository) {
        this.drillRepository = drillRepository;
    }

    @Override
    Observable<Drill> buildUseCaseObservable(Params params) {
        if (params.imageUrl == null)
            return drillRepository.addDrill(params.name, params.description, params.image, params.type, params.maxScore, params.targetScore, params.purchased);
        else
            return drillRepository.addDrill(params.name, params.description, params.imageUrl, params.type, params.maxScore, params.targetScore, params.purchased);
    }

    public static final class Params {
        private String name;
        private String description;
        @Nullable
        private byte[] image;
        @Nullable
        private String imageUrl;
        private int maxScore;
        private int targetScore;
        private String type;
        private boolean purchased;

        public Params(String name, String description, byte[] image, String type, int maxScore, int targetScore, boolean purchased) {
            this.name = name;
            this.description = description;
            this.image = image;
            this.maxScore = maxScore;
            this.targetScore = targetScore;
            this.type = type;
            this.purchased = purchased;
        }

        public Params(String name, String description, String imageUrl, String type, int maxScore, int targetScore, boolean purchased) {
            this.name = name;
            this.description = description;
            this.imageUrl = imageUrl;
            this.maxScore = maxScore;
            this.targetScore = targetScore;
            this.type = type;
            this.purchased = purchased;
        }

        public static Params create(String name, String description, byte[] image, DrillModel.Type type, int maxScore, int targetScore, boolean purchased) {
            DrillEntityDataMapper mapper = new DrillEntityDataMapper();
            return new Params(name, description, image, mapper.transform(type), maxScore, targetScore, purchased);
        }

        public static Params create(String name, String description, String imageUrl, DrillModel.Type type, int maxScore, int targetScore, boolean purchased) {
            DrillEntityDataMapper mapper = new DrillEntityDataMapper();
            return new Params(name, description, imageUrl, mapper.transform(type), maxScore, targetScore, purchased);
        }
    }
}
