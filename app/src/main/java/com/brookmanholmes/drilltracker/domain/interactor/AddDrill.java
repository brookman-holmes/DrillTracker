package com.brookmanholmes.drilltracker.domain.interactor;

import com.brookmanholmes.drilltracker.data.entity.mapper.DrillEntityDataMapper;
import com.brookmanholmes.drilltracker.domain.Drill;
import com.brookmanholmes.drilltracker.domain.executor.PostExecutionThread;
import com.brookmanholmes.drilltracker.domain.executor.ThreadExecutor;
import com.brookmanholmes.drilltracker.domain.repository.DrillRepository;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by Brookman Holmes on 7/14/2017.
 */

public class AddDrill extends UseCase<Drill, AddDrill.Params> {
    private final DrillRepository drillRepository;

    public AddDrill(DrillRepository drillRepository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.drillRepository = drillRepository;
    }

    @Override
    Observable<Drill> buildUseCaseObservable(Params params) {
        return drillRepository.addDrill(params.name, params.description, params.image, params.type, params.maxScore, params.targetScore);
    }

    public static final class Params {
        private String name;
        private String description;
        private byte[] image;
        private int maxScore;
        private int targetScore;
        private String type;

        public Params(String name, String description, byte[] image, String type, int maxScore, int targetScore) {
            this.name = name;
            this.description = description;
            this.image = image;
            this.maxScore = maxScore;
            this.targetScore = targetScore;
            this.type = type;
        }

        public static Params create(String name, String description, byte[] image, DrillModel.Type type, int maxScore, int targetScore) {
            DrillEntityDataMapper mapper = new DrillEntityDataMapper();
            return new Params(name, description, image, mapper.transform(type), maxScore, targetScore);
        }
    }
}
