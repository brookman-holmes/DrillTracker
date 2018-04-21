package com.brookmanholmes.drilltracker.presentation.addattempt;

import com.brookmanholmes.drilltracker.data.repository.datasource.DataStoreFactory;
import com.brookmanholmes.drilltracker.domain.Drill;
import com.brookmanholmes.drilltracker.domain.interactor.AddAttempt;
import com.brookmanholmes.drilltracker.domain.interactor.DefaultObserver;
import com.brookmanholmes.drilltracker.presentation.base.Presenter;
import com.brookmanholmes.drilltracker.presentation.model.AimDrillModel;

/**
 * Created by Brookman Holmes on 7/28/2017.
 */
class AddAimAttemptDialogPresenter implements Presenter {
    private final AddAttempt addAttempt;

    AddAimAttemptDialogPresenter() {
        addAttempt = new AddAttempt(DataStoreFactory.getDrillRepo());
    }

    AddAimAttemptDialogPresenter(AddAttempt addAttempt) {
        this.addAttempt = addAttempt;
    }

    void addAttempt(String drillId, int makes, int overcuts, int undercuts, int english, int obPosition, int cbPosition) {
        addAttempt.execute(new DefaultObserver<Drill>(),
                AddAttempt.Params.create(
                        drillId,
                        AimDrillModel.createAttempt(
                                makes,
                                overcuts,
                                undercuts,
                                english,
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
