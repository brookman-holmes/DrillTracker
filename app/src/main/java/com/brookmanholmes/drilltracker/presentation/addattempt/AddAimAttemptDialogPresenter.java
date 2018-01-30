package com.brookmanholmes.drilltracker.presentation.addattempt;

import android.util.Pair;

import com.brookmanholmes.drilltracker.data.repository.datasource.DataStoreFactory;
import com.brookmanholmes.drilltracker.domain.Drill;
import com.brookmanholmes.drilltracker.domain.interactor.AddAttempt;
import com.brookmanholmes.drilltracker.domain.interactor.DefaultObserver;
import com.brookmanholmes.drilltracker.presentation.base.Presenter;
import com.brookmanholmes.drilltracker.presentation.model.AimDrillModel;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;

import java.util.Date;

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
                        new DrillModel.AttemptModel(
                                0,
                                0,
                                new Date(),
                                obPosition,
                                cbPosition,
                                new Pair<>(AimDrillModel.ATTEMPTS, makes + overcuts + undercuts),
                                new Pair<>(AimDrillModel.ENGLISH, english),
                                new Pair<>(AimDrillModel.MAKES, makes),
                                new Pair<>(AimDrillModel.OVER_CUTS, overcuts),
                                new Pair<>(AimDrillModel.UNDER_CUTS, undercuts)
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
