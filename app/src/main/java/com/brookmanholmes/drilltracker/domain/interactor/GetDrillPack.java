package com.brookmanholmes.drilltracker.domain.interactor;

import com.brookmanholmes.drilltracker.domain.DrillPack;
import com.brookmanholmes.drilltracker.domain.executor.PostExecutionThread;
import com.brookmanholmes.drilltracker.domain.executor.ThreadExecutor;
import com.brookmanholmes.drilltracker.domain.repository.DrillPackRepository;

import io.reactivex.Observable;

/**
 * Created by Brookman Holmes on 8/9/2017.
 */

public class GetDrillPack extends UseCase<DrillPack, GetDrillPack.Params> {
    private final DrillPackRepository repository;

    public GetDrillPack(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, DrillPackRepository repository) {
        super(threadExecutor, postExecutionThread);
        this.repository = repository;
    }

    @Override
    Observable<DrillPack> buildUseCaseObservable(Params params) {
        return repository.observeDrillPack(params.sku);
    }

    public static class Params {
        private String sku;

        public Params(String sku) {
            this.sku = sku;
        }
    }
}
