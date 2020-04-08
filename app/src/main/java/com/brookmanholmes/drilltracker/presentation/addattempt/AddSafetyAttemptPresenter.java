package com.brookmanholmes.drilltracker.presentation.addattempt;

import com.brookmanholmes.drilltracker.data.repository.datasource.DataStoreFactory;
import com.brookmanholmes.drilltracker.domain.interactor.AddAttempt;
import com.brookmanholmes.drilltracker.domain.interactor.DefaultObserver;
import com.brookmanholmes.drilltracker.presentation.base.Presenter;
import com.brookmanholmes.drilltracker.presentation.model.SafetyDrillModel;

import java.util.EnumSet;

/**
 * Created by Brookman Holmes on 7/28/2017.
 */
class AddSafetyAttemptPresenter implements Presenter {
    private final AddAttempt addAttempt;

    AddSafetyAttemptPresenter() {
        addAttempt = new AddAttempt(DataStoreFactory.getDrillRepo());
    }

    AddSafetyAttemptPresenter(AddAttempt addAttempt) {
        this.addAttempt = addAttempt;
    }

    void addAttempt(String drillId, EnumSet<SafetyDrillModel.SafetyTypes> safetyTypes, int obPosition, int cbPosition) {
        addAttempt.execute(
                new DefaultObserver<>(),
                AddAttempt.Params.create(drillId, SafetyDrillModel.createAttempt(obPosition, cbPosition, safetyTypes))
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
