package com.brookmanholmes.drilltracker.domain.interactor;

import com.brookmanholmes.drilltracker.domain.Drill;
import com.brookmanholmes.drilltracker.domain.repository.DrillRepository;
import com.brookmanholmes.drilltracker.presentation.mapper.DrillModelDataMapper;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;

import io.reactivex.Observable;

/**
 * Created by Brookman Holmes on 7/13/2017.
 * This class is an implementation of {@link UseCase} that represents a use case for adding an
 * {@link Drill.Attempt} to an existing {@link Drill}
 */
public class AddAttempt extends UseCase<Drill, AddAttempt.Params> {
    private final DrillRepository drillRepository;

    public AddAttempt(DrillRepository drillRepository) {
        this.drillRepository = drillRepository;
    }

    @Override
    Observable<Drill> buildUseCaseObservable(Params params) {
        return drillRepository.addAttempt(params.drillId, DrillModelDataMapper.transform(params.attempt));
    }

    public static final class Params {
        private final String drillId;
        private final DrillModel.AttemptModel attempt;

        private Params(String drillId, DrillModel.AttemptModel attempt) {
            this.drillId = drillId;
            this.attempt = attempt;
        }

        public static Params create(String drillId, DrillModel.AttemptModel attempt) {
            return new Params(drillId, attempt);
        }
    }
}
