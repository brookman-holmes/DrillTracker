package com.brookmanholmes.drilltracker.presentation.addattempt;

import com.brookmanholmes.drilltracker.data.repository.datasource.DataStoreFactory;
import com.brookmanholmes.drilltracker.domain.interactor.AddAttempt;
import com.brookmanholmes.drilltracker.domain.interactor.DefaultObserver;
import com.brookmanholmes.drilltracker.presentation.base.Presenter;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;

import java.util.Date;

/**
 * Created by Brookman Holmes on 7/28/2017.
 */
class AddAttemptPresenter implements Presenter {
    private final AddAttempt addAttempt;

    AddAttemptPresenter() {
        addAttempt = new AddAttempt(DataStoreFactory.getDrillRepo());
    }

    AddAttemptPresenter(AddAttempt addAttempt) {
        this.addAttempt = addAttempt;
    }

    void addAttempt(String drillId, int score, int target, int obPosition, int cbPosition) {
        addAttempt.execute(new DefaultObserver<>(),
                AddAttempt.Params.create(
                        drillId,
                        new DrillModel.AttemptModel(
                                score,
                                target,
                                new Date(),
                                obPosition,
                                cbPosition
                        )
                )
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
