package com.brookmanholmes.drilltracker.domain.interactor;

import com.brookmanholmes.drilltracker.domain.Attempt;
import com.brookmanholmes.drilltracker.domain.Drill;
import com.brookmanholmes.drilltracker.domain.repository.DrillRepository;
import com.brookmanholmes.drilltracker.presentation.mapper.DrillModelDataMapper;
import com.brookmanholmes.drilltracker.presentation.model.AttemptModel;

import io.reactivex.Observable;
import timber.log.Timber;

/**
 * Created by Brookman Holmes on 7/13/2017.
 * This class is an implementation of {@link UseCase} that represents a use case for adding an
 * {@link Attempt} to an existing {@link Drill}
 */
public class AddAttempt extends UseCase<Drill, AddAttempt.Params> {
    private final DrillRepository drillRepository;

    public AddAttempt(DrillRepository drillRepository) {
        this.drillRepository = drillRepository;
    }

    @Override
    Observable<Drill> buildUseCaseObservable(Params params) {
        Timber.d("AddAttemptUseCase AttemptModel: %s", params.attempt);
        return drillRepository.addAttempt(params.drillId, DrillModelDataMapper.transform(params.attempt));
    }

    public static final class Params {
        private final String drillId;
        private final AttemptModel attempt;

        private Params(String drillId, AttemptModel attempt) {
            this.drillId = drillId;
            this.attempt = attempt;
        }

        public static Params create(String drillId, AttemptModel attempt) {
            return new Params(drillId, attempt);
        }
    }
}
