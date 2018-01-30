package com.brookmanholmes.drilltracker.presentation.addattempt;

import android.util.Pair;

import com.brookmanholmes.drilltracker.data.repository.datasource.DataStoreFactory;
import com.brookmanholmes.drilltracker.domain.Drill;
import com.brookmanholmes.drilltracker.domain.interactor.AddAttempt;
import com.brookmanholmes.drilltracker.domain.interactor.DefaultObserver;
import com.brookmanholmes.drilltracker.presentation.base.Presenter;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;

import java.util.Date;

/**
 * Created by brookman on 1/25/18.
 */

public class AddSpeedAttemptDialogPresenter implements Presenter {
    private final AddAttempt addAttempt;

    public AddSpeedAttemptDialogPresenter() {
        this.addAttempt = new AddAttempt(DataStoreFactory.getDrillRepo());
    }

    void addAttempt(String drillId, int obPosition, int cbPosition, int twoDiamondsSoftAttempts,
                    int diamondSoftAttempts, int halfDiamondSoftAttempts, int quarterDiamondSoftAttempts,
                    int correctSpeedAttempts, int quarterDiamondHardAttempts, int halfDiamondHardAttempts,
                    int diamondHardAttempts, int twoDiamondsHardAttempts) {
        this.addAttempt.execute(new DefaultObserver<Drill>(), AddAttempt.Params.create(
                drillId,
                new DrillModel.AttemptModel(
                        0,
                        0,
                        new Date(),
                        obPosition,
                        cbPosition,
                        new Pair<>("TWO_DIAMOND_SOFT", twoDiamondsSoftAttempts),
                        new Pair<>("TWO_DIAMOND_HARD", twoDiamondsHardAttempts),
                        new Pair<>("DIAMOND_SOFT", diamondSoftAttempts),
                        new Pair<>("HALF_DIAMOND_SOFT", halfDiamondSoftAttempts),
                        new Pair<>("QUARTER_DIAMOND_SOFT", quarterDiamondSoftAttempts),
                        new Pair<>("CORRECT", correctSpeedAttempts),
                        new Pair<>("QUARTER_DIAMOND_HARD", quarterDiamondHardAttempts),
                        new Pair<>("HALF_DIAMOND_HARD", halfDiamondHardAttempts),
                        new Pair<>("DIAMOND_HARD", diamondHardAttempts)
                )
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
