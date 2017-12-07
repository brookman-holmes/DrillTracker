package com.brookmanholmes.drilltracker.domain.interactor;

import com.brookmanholmes.drilltracker.data.entity.mapper.DrillEntityDataMapper;
import com.brookmanholmes.drilltracker.domain.Drill;
import com.brookmanholmes.drilltracker.domain.repository.DrillRepository;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;

import io.reactivex.Observable;

/**
 * Created by Brookman Holmes on 7/30/2017.
 * This class is an implementation of {@link UseCase} that represents a use case for updating a {@link Drill}
 * in the repository
 */

public class UpdateDrill extends UseCase<Drill, UpdateDrill.Params> {
    private final DrillRepository drillRepository;

    public UpdateDrill(DrillRepository drillRepository) {
        this.drillRepository = drillRepository;
    }

    @Override
    Observable<Drill> buildUseCaseObservable(Params params) {
        if (params.image != null)
            return drillRepository.updateDrill(params.name, params.description, params.id, params.image, params.type, params.maxScore, params.defaultTargetScore);
        else if (params.imageUrl != null)
            return drillRepository.updateDrill(new Drill(params.id, params.name, params.description, params.imageUrl, Drill.Type.valueOf(params.type), params.maxScore, params.defaultTargetScore, false));
        else throw new IllegalStateException("Both image and imageUrl are null");
    }

    public static class Params {
        String name;
        String id;
        String description;
        int maxScore;
        int defaultTargetScore;
        String type;
        byte[] image;
        String imageUrl;

        private Params(String name, String description, String id, byte[] image, DrillModel.Type type, int maxScore, int targetScore) {
            DrillEntityDataMapper mapper = new DrillEntityDataMapper();
            this.name = name;
            this.id = id;
            this.description = description;
            this.maxScore = maxScore;
            this.defaultTargetScore = targetScore;
            this.type = mapper.transform(type);
            this.image = image;
            this.defaultTargetScore = targetScore;
        }

        public static Params create(String name, String description, String id, byte[] image, DrillModel.Type type, int maxScore, int targetScore) {
            return new Params(name, description, id, image, type, maxScore, targetScore);
        }

        public static Params create(String name, String description, String id, String imageUrl, DrillModel.Type type, int maxScore, int targetScore) {
            Params param = new Params(name, description, id, null, type, maxScore, targetScore);
            param.imageUrl = imageUrl;
            return param;
        }
    }
}
