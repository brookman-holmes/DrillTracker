package com.brookmanholmes.drilltracker.domain.interactor;

import com.brookmanholmes.drilltracker.domain.Drill;
import com.brookmanholmes.drilltracker.domain.repository.DrillRepository;
import com.brookmanholmes.drilltracker.presentation.model.Type;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Brookman Holmes on 7/7/2017.
 * This class is an implementation of {@link UseCase} that represents a use case for retrieving a list
 * of all {@link Drill}
 */

public class GetDrillList extends UseCase<List<Drill>, GetDrillList.Params>{
    private final DrillRepository drillRepository;

    public GetDrillList(DrillRepository drillRepository) {
        this.drillRepository = drillRepository;
    }

    @Override
    Observable<List<Drill>> buildUseCaseObservable(Params params) {
        return this.drillRepository.observeDrills(params.filter);
    }

    public static class Params {
        private final Type filter;

        private Params(Type filter) {
            this.filter = filter;
        }

        public static Params newInstance(Type filter) {
            return new Params(filter);
        }
    }
}
