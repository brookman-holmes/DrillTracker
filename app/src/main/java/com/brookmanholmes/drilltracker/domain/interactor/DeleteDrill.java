package com.brookmanholmes.drilltracker.domain.interactor;

import com.brookmanholmes.drilltracker.domain.repository.DrillRepository;

import io.reactivex.Observable;

/**
 * Created by Brookman Holmes on 7/26/2017.
 * This class is an implementation of {@link UseCase} that represents a use case for removing a {@link com.brookmanholmes.drilltracker.domain.Drill}
 * from the repository
 */

public class DeleteDrill extends UseCase<Boolean, String> {
    private final DrillRepository drillRepository;

    public DeleteDrill(DrillRepository drillRepository) {
        this.drillRepository = drillRepository;
    }

    @Override
    Observable<Boolean> buildUseCaseObservable(String id) {
        drillRepository.deleteDrill(id);
        return Observable.empty();
    }
}
