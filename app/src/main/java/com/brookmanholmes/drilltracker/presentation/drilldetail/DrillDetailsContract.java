package com.brookmanholmes.drilltracker.presentation.drilldetail;

import androidx.annotation.NonNull;

import com.brookmanholmes.drilltracker.presentation.base.Presenter;
import com.brookmanholmes.drilltracker.presentation.model.English;
import com.brookmanholmes.drilltracker.presentation.model.ShotResult;
import com.brookmanholmes.drilltracker.presentation.model.Speed;
import com.brookmanholmes.drilltracker.presentation.model.VSpin;

import java.util.List;

/**
 * Created by Brookman Holmes on 8/11/2017.
 * Interface representing the contract between {@link DrillDetailsPresenter} and the
 * {@link DrillDetailsView}
 */

interface DrillDetailsContract extends Presenter {
    /**
     * Sets the view for this presenter
     *
     * @param view The view that this presenter will interact with
     */
    void setView(@NonNull DrillDetailsView view);

    /**
     * Initializes the presenter
     *
     * @param drillId The ID of the drill that this presenter will use
     */
    void initialize(@NonNull String drillId);

    /**
     * Removes the last attempt that the user entered
     */
    void onUndoClicked();

    /**
     * Displays the dialog for adding an attempt to a drill
     */
    void onAddAttemptClicked();

    void setSelectedCbPosition(int position);
    void setSelectedObPosition(int position);
    void setSelectedTargetPosition(int position);
    void setSelectedEnglish(English english);
    void setSelectedPattern(List<Integer> pattern);
    void setSelectedSpeed(Speed speed);
    void setVSpinUsed(VSpin spin);
    void setSelectedShotResult(ShotResult shotResult);

}
