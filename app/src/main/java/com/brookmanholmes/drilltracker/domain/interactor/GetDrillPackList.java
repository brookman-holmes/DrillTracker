package com.brookmanholmes.drilltracker.domain.interactor;

import com.brookmanholmes.drilltracker.domain.DrillPack;
import com.brookmanholmes.drilltracker.domain.repository.DrillPackRepository;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Brookman Holmes on 8/9/2017.
 * This class is an implementation of {@link UseCase} that represents a use case for getting a list
 * of {@link DrillPack}
 */

public class GetDrillPackList extends UseCase<List<DrillPack>, Void> {
    private final DrillPackRepository repository;

    public GetDrillPackList(DrillPackRepository repository) {
        this.repository = repository;
    }

    @Override
    Observable<List<DrillPack>> buildUseCaseObservable(Void aVoid) {
        return repository.observeDrillPacks();
    }
}
