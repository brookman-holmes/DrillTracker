package com.brookmanholmes.drilltracker.presentation.addattempt;

import com.brookmanholmes.drilltracker.data.repository.datasource.DataStoreFactory;
import com.brookmanholmes.drilltracker.domain.Drill;
import com.brookmanholmes.drilltracker.domain.interactor.AddAttempt;
import com.brookmanholmes.drilltracker.domain.interactor.DefaultObserver;
import com.brookmanholmes.drilltracker.presentation.base.Presenter;
import com.brookmanholmes.drilltracker.presentation.model.PositionalDrillModel;

import java.util.EnumSet;

class AddPositionalAttemptDialogPresenter implements Presenter {
    private AddAttempt addAttempt;

    public AddPositionalAttemptDialogPresenter() {
        addAttempt = new AddAttempt(DataStoreFactory.getDrillRepo());
    }

    public AddPositionalAttemptDialogPresenter(AddAttempt addAttempt) {
        this.addAttempt = addAttempt;
    }

    void addAttempt(String drillId, int cbPosition, int targetPosition, EnumSet<PositionalDrillModel.PositionalTypes> positionalTypes) {
        addAttempt.execute(
                new DefaultObserver<Drill>(),
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
