package com.brookmanholmes.drilltracker.presentation.addattempt;

import com.brookmanholmes.drilltracker.data.repository.datasource.DataStoreFactory;
import com.brookmanholmes.drilltracker.domain.Drill;
import com.brookmanholmes.drilltracker.domain.interactor.AddAttempt;
import com.brookmanholmes.drilltracker.domain.interactor.DefaultObserver;
import com.brookmanholmes.drilltracker.presentation.base.Presenter;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;

import java.util.Date;

/**
 * Created by Brookman Holmes on 7/28/2017.
 */
class AddAttemptDialogPresenter implements Presenter {
    private final AddAttempt addAttempt;

    AddAttemptDialogPresenter() {
        addAttempt = new AddAttempt(DataStoreFactory.getDrillRepo());
    }

    AddAttemptDialogPresenter(AddAttempt addAttempt) {
        this.addAttempt = addAttempt;
    }

    void addAttempt(String drillId, int score, int target, int obPosition, int cbPosition) {
        addAttempt.execute(new DefaultObserver<Drill>(),
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
