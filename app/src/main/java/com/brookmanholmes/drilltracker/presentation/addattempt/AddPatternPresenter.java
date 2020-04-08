package com.brookmanholmes.drilltracker.presentation.addattempt;

import com.brookmanholmes.drilltracker.data.repository.datasource.DataStoreFactory;
import com.brookmanholmes.drilltracker.domain.interactor.AddAttempt;
import com.brookmanholmes.drilltracker.domain.interactor.DefaultObserver;
import com.brookmanholmes.drilltracker.presentation.base.Presenter;
import com.brookmanholmes.drilltracker.presentation.model.PatternDrillModel;
import com.brookmanholmes.drilltracker.presentation.model.PatternEntry;

import java.util.List;

public class AddPatternPresenter implements Presenter {
    private final AddAttempt addAttempt;

    AddPatternPresenter() {
        addAttempt = new AddAttempt(DataStoreFactory.getDrillRepo());
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        addAttempt.dispose();
    }

    public void addAttempt(String drillId, int targetScore, List<PatternEntry> entries) {
        addAttempt.execute(
                new DefaultObserver<>(),
                AddAttempt.Params.create(drillId, PatternDrillModel.createAttempt(targetScore, entries))
        );
    }
}
