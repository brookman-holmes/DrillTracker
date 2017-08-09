package com.brookmanholmes.drilltracker.domain.interactor;

import com.brookmanholmes.drilltracker.domain.executor.PostExecutionThread;
import com.brookmanholmes.drilltracker.domain.executor.ThreadExecutor;
import com.brookmanholmes.drilltracker.domain.repository.DrillRepository;

import io.reactivex.Observable;

/**
 * Created by Brookman Holmes on 8/1/2017.
 */

public class DeleteAttempt extends UseCase<Void, String> {
    private final DrillRepository drillRepository;

    public DeleteAttempt(DrillRepository drillRepository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.drillRepository = drillRepository;
    }

    @Override
    Observable<Void> buildUseCaseObservable(String drillId) {
        drillRepository.removeLastAttempt(drillId);
        return Observable.empty();
    }
}
