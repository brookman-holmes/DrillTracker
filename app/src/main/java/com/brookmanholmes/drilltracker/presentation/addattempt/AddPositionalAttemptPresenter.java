package com.brookmanholmes.drilltracker.presentation.addattempt;

import com.brookmanholmes.drilltracker.data.repository.datasource.DataStoreFactory;
import com.brookmanholmes.drilltracker.domain.interactor.AddAttempt;
import com.brookmanholmes.drilltracker.domain.interactor.DefaultObserver;
import com.brookmanholmes.drilltracker.presentation.base.Presenter;
import com.brookmanholmes.drilltracker.presentation.model.PositionalDrillModel;

import java.util.EnumSet;

class AddPositionalAttemptPresenter implements Presenter {
    private final AddAttempt addAttempt;

    public AddPositionalAttemptPresenter() {
        addAttempt = new AddAttempt(DataStoreFactory.getDrillRepo());
    }

    public AddPositionalAttemptPresenter(AddAttempt addAttempt) {
        this.addAttempt = addAttempt;
    }

    void addAttempt(String drillId, int cbPosition, int targetPosition, EnumSet<PositionalDrillModel.PositionalTypes> positionalTypes) {
        addAttempt.execute(
                new DefaultObserver<>(),
                AddAttempt.Params.create(drillId, PositionalDrillModel.createAttempt(cbPosition, targetPosition, positionalTypes))
        );
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
}
