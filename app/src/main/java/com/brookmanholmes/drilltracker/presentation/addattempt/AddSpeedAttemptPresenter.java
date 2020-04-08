package com.brookmanholmes.drilltracker.presentation.addattempt;

import com.brookmanholmes.drilltracker.data.repository.datasource.DataStoreFactory;
import com.brookmanholmes.drilltracker.domain.interactor.AddAttempt;
import com.brookmanholmes.drilltracker.domain.interactor.DefaultObserver;
import com.brookmanholmes.drilltracker.presentation.base.Presenter;
import com.brookmanholmes.drilltracker.presentation.model.SpeedDrillModel;

/**
 * Created by brookman on 1/25/18.
 */

public class AddSpeedAttemptPresenter implements Presenter {
    private final AddAttempt addAttempt;

    public AddSpeedAttemptPresenter() {
        this.addAttempt = new AddAttempt(DataStoreFactory.getDrillRepo());
    }

    void addAttempt(String drillId, int obPosition, int cbPosition, int twoDiamondsSoftAttempts,
                    int diamondSoftAttempts, int halfDiamondSoftAttempts, int quarterDiamondSoftAttempts,
                    int correctSpeedAttempts, int quarterDiamondHardAttempts, int halfDiamondHardAttempts,
                    int diamondHardAttempts, int twoDiamondsHardAttempts) {
        this.addAttempt.execute(new DefaultObserver<>(), AddAttempt.Params.create(
                drillId,
                SpeedDrillModel.createAttempt(
                        obPosition,
                        cbPosition,
                        twoDiamondsSoftAttempts,
                        diamondSoftAttempts,
                        halfDiamondSoftAttempts,
                        quarterDiamondSoftAttempts,
                        correctSpeedAttempts,
                        quarterDiamondHardAttempts,
                        halfDiamondHardAttempts,
                        diamondHardAttempts,
                        twoDiamondsHardAttempts)
        ));
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
