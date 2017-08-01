package com.brookmanholmes.drilltracker.domain.interactor;

import com.brookmanholmes.drilltracker.domain.Drill;
import com.brookmanholmes.drilltracker.domain.executor.PostExecutionThread;
import com.brookmanholmes.drilltracker.domain.executor.ThreadExecutor;
import com.brookmanholmes.drilltracker.domain.repository.DrillRepository;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by Brookman Holmes on 7/13/2017.
 */
public final class AddAttempt extends UseCase<Drill, AddAttempt.Params> {
    private final DrillRepository drillRepository;

    public AddAttempt(DrillRepository drillRepository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.drillRepository = drillRepository;
    }

    @Override
    Observable<Drill> buildUseCaseObservable(Params params) {
        return drillRepository.addAttempt(params.drillId, params.attempt);
    }

    public static final class Params {
        private final String drillId;
        private final Drill.Attempt attempt;

        private Params(String drillId, Drill.Attempt attempt) {
            this.drillId = drillId;
            this.attempt = attempt;
        }

        public static Params create(String drillId, Drill.Attempt attempt) {
            return new Params(drillId, attempt);
        }
    }
}
