package com.brookmanholmes.drilltracker.domain.interactor;

import com.brookmanholmes.drilltracker.domain.executor.PostExecutionThread;
import com.brookmanholmes.drilltracker.domain.executor.ThreadExecutor;
import com.brookmanholmes.drilltracker.domain.repository.DrillRepository;

import io.reactivex.Observable;

/**
 * Created by Brookman Holmes on 7/26/2017.
 */

public class DeleteDrill extends UseCase<Boolean, String> {
    private final DrillRepository drillRepository;

    public DeleteDrill(DrillRepository drillRepository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.drillRepository = drillRepository;
    }

    @Override
    Observable<Boolean> buildUseCaseObservable(String id) {
        drillRepository.deleteDrill(id);
        return Observable.empty();
    }
}
