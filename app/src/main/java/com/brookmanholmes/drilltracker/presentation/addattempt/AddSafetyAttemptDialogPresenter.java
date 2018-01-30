package com.brookmanholmes.drilltracker.presentation.addattempt;

import android.util.Pair;

import com.brookmanholmes.drilltracker.data.repository.datasource.DataStoreFactory;
import com.brookmanholmes.drilltracker.domain.Drill;
import com.brookmanholmes.drilltracker.domain.interactor.AddAttempt;
import com.brookmanholmes.drilltracker.domain.interactor.DefaultObserver;
import com.brookmanholmes.drilltracker.presentation.base.Presenter;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;
import com.brookmanholmes.drilltracker.presentation.model.SafetyDrillModel;

import java.util.Date;
import java.util.EnumSet;

/**
 * Created by Brookman Holmes on 7/28/2017.
 */
class AddSafetyAttemptDialogPresenter implements Presenter {
    private final AddAttempt addAttempt;

    AddSafetyAttemptDialogPresenter() {
        addAttempt = new AddAttempt(DataStoreFactory.getDrillRepo());
    }

    AddSafetyAttemptDialogPresenter(AddAttempt addAttempt) {
        this.addAttempt = addAttempt;
    }

    void addAttempt(String drillId, EnumSet<SafetyDrillModel.SafetyTypes> safetyTypes, int obPosition, int cbPosition) {
        addAttempt.execute(new DefaultObserver<Drill>(),
                AddAttempt.Params.create(
                        drillId,
                        new DrillModel.AttemptModel(
                                0,
                                0,
                                new Date(),
                                obPosition,
                                cbPosition,
                                new Pair<>(SafetyDrillModel.SAFETY_TYPES, SafetyDrillModel.encode(safetyTypes)))
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
