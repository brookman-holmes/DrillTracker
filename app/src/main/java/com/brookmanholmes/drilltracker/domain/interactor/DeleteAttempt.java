package com.brookmanholmes.drilltracker.domain.interactor;

import com.brookmanholmes.drilltracker.domain.repository.DrillRepository;

import io.reactivex.Observable;

/**
 * Created by Brookman Holmes on 8/1/2017.
 * This class is an implementation of {@link UseCase} that represents a use case for removing the last
 * {@link com.brookmanholmes.drilltracker.domain.Drill.Attempt} that was added to a drill
 */

public class DeleteAttempt extends UseCase<Void, String> {
    private final DrillRepository drillRepository;

    public DeleteAttempt(DrillRepository drillRepository) {
        this.drillRepository = drillRepository;
    }

    @Override
    Observable<Void> buildUseCaseObservable(String drillId) {
        drillRepository.removeLastAttempt(drillId);
        return Observable.empty();
    }
}
