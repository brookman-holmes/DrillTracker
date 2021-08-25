package com.brookmanholmes.drilltracker.domain.interactor;

import com.brookmanholmes.drilltracker.domain.Drill;
import com.brookmanholmes.drilltracker.domain.repository.DrillRepository;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Brookman Holmes on 4/13/2020
 * This class is an implementation of {@link UseCase} that represents a use case for adding a new
 * pattern to a drill
 */
public class AddPattern extends UseCase<Drill, Pair<String, List<Integer>>> {
    private final DrillRepository drillRepository;

    public AddPattern(DrillRepository drillRepository) {
        this.drillRepository = drillRepository;
    }

    @Override
    Observable<Drill> buildUseCaseObservable(Pair<String, List<Integer>> stringListPair) {
        return null;
    }
}
