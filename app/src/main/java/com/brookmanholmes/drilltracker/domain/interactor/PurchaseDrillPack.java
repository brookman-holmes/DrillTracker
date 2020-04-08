package com.brookmanholmes.drilltracker.domain.interactor;

import com.brookmanholmes.drilltracker.domain.Drill;
import com.brookmanholmes.drilltracker.domain.repository.DrillPackRepository;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by brookman on 9/12/17.
 */

public class PurchaseDrillPack extends UseCase<List<Drill>, PurchaseDrillPack.Params> {
    private final DrillPackRepository repository;

    public PurchaseDrillPack(DrillPackRepository drillPackRepository) {
        super();
        this.repository = drillPackRepository;
    }

    @Override
    Observable<List<Drill>> buildUseCaseObservable(Params params) {
        return repository.purchaseDrillPack(params.id);
    }

    public static final class Params {
        private final String id;

        private Params(String id) {
            this.id = id;
        }

        public static Params forDrillPack(String id) {
            return new Params(id);
        }
    }
}
