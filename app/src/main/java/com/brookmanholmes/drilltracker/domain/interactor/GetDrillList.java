package com.brookmanholmes.drilltracker.domain.interactor;

import com.brookmanholmes.drilltracker.domain.Drill;
import com.brookmanholmes.drilltracker.domain.executor.PostExecutionThread;
import com.brookmanholmes.drilltracker.domain.executor.ThreadExecutor;
import com.brookmanholmes.drilltracker.domain.repository.DrillRepository;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Brookman Holmes on 7/7/2017.
 */

public class GetDrillList extends UseCase<List<Drill>, GetDrillList.Params>{
    private final DrillRepository drillRepository;

    public GetDrillList(DrillRepository drillRepository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.drillRepository = drillRepository;
    }

    @Override
    Observable<List<Drill>> buildUseCaseObservable(Params params) {
        return this.drillRepository.observeDrills(params.filter);
    }

    public static class Params {
        private DrillModel.Type filter;

        private Params(DrillModel.Type filter) {
            this.filter = filter;
        }

        public static Params newInstance(DrillModel.Type filter) {
            return new Params(filter);
        }
    }
}
