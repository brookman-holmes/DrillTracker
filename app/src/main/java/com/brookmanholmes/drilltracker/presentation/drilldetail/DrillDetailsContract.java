package com.brookmanholmes.drilltracker.presentation.drilldetail;

import android.support.annotation.NonNull;

import com.brookmanholmes.drilltracker.presentation.base.Presenter;

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
    void initialize(String drillId);

    /**
     * Removes the last attempt that the user entered
     */
    void onUndoClicked();
}
