package com.brookmanholmes.drilltracker.domain.interactor;

import com.brookmanholmes.drilltracker.domain.Drill;
import com.brookmanholmes.drilltracker.domain.repository.DrillPackRepository;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Brookman Holmes on 8/16/2017.
 */

public class GetDrillPackDrillsList extends UseCase<List<Drill>, GetDrillPackDrillsList.Params> {
    DrillPackRepository repository;

    public GetDrillPackDrillsList(DrillPackRepository drillPackRepository) {
        super();
        this.repository = drillPackRepository;
    }

    @Override
    Observable<List<Drill>> buildUseCaseObservable(Params params) {
        return repository.observeDrillPack(params.id);
    }

    public static final class Params {
        private final String id;

        private Params(String id) {
            this.id = id;
        }

        public static Params forDrillPack(String id) {
            return new Params(id);
        }
    }
}
