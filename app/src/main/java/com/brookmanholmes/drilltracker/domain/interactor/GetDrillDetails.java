package com.brookmanholmes.drilltracker.domain.interactor;

import android.support.v4.util.Preconditions;

import com.brookmanholmes.drilltracker.domain.Drill;
import com.brookmanholmes.drilltracker.domain.executor.PostExecutionThread;
import com.brookmanholmes.drilltracker.domain.executor.ThreadExecutor;
import com.brookmanholmes.drilltracker.domain.repository.DrillRepository;

import io.reactivex.Observable;

/**
 * Created by Brookman Holmes on 7/11/2017.
 */

public class GetDrillDetails extends UseCase<Drill, GetDrillDetails.Params> {
    private final DrillRepository drillRepository;

    public GetDrillDetails(DrillRepository drillRepository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.drillRepository = drillRepository;
    }

    @Override
    Observable<Drill> buildUseCaseObservable(Params params) {
        Preconditions.checkNotNull(params);
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
