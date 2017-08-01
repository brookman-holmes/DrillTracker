package com.brookmanholmes.drilltracker.domain.interactor;

import com.brookmanholmes.drilltracker.domain.Drill;
import com.brookmanholmes.drilltracker.domain.executor.PostExecutionThread;
import com.brookmanholmes.drilltracker.domain.executor.ThreadExecutor;
import com.brookmanholmes.drilltracker.domain.repository.DrillRepository;

import io.reactivex.Observable;

/**
 * Created by Brookman Holmes on 7/14/2017.
 */

public class RemoveAttempt extends UseCase<Drill, RemoveAttempt.Params> {
    private final DrillRepository drillRepository;

    public RemoveAttempt(DrillRepository drillRepository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.drillRepository = drillRepository;
    }

    @Override
    Observable<Drill> buildUseCaseObservable(RemoveAttempt.Params params) {
        return drillRepository.removeAttempt(params.drillId);
    }

    public static final class Params {
        private final String drillId;

        private Params(String drillId) {
            this.drillId = drillId;
        }

        public static RemoveAttempt.Params create(String drillId) {
            return new RemoveAttempt.Params(drillId);
        }
    }
}
