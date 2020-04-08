package com.brookmanholmes.drilltracker.domain.interactor;

import com.brookmanholmes.drilltracker.domain.Drill;
import com.brookmanholmes.drilltracker.domain.repository.DrillRepository;

import io.reactivex.Observable;

/**
 * Created by Brookman Holmes on 7/11/2017.
 * This class is an implementation of {@link UseCase} that represents a use case for retrieving a
 * {@link Drill}
 */

public class GetDrillDetails extends UseCase<Drill, GetDrillDetails.Params> {
    private final DrillRepository drillRepository;

    public GetDrillDetails(DrillRepository drillRepository) {
        this.drillRepository = drillRepository;
    }

    @Override
    Observable<Drill> buildUseCaseObservable(Params params) {
        return drillRepository.observeDrill(params.drillId);
    }

    public static final class Params {
        private final String drillId;

        private Params(String drillId) {
            this.drillId = drillId;
        }

        public static Params forDrill(String drillId) {
            return new Params(drillId);
        }
    }
}
