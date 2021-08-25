package com.brookmanholmes.drilltracker.presentation.drills;

import androidx.annotation.NonNull;

import com.brookmanholmes.drilltracker.presentation.base.Presenter;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;
import com.brookmanholmes.drilltracker.presentation.model.Type;

/**
 * Created by Brookman Holmes on 8/11/2017.
 * Interface representing the contract between the {@link DrillsListPresenter} and the
 * {@link DrillsListView}
 */
interface DrillsListContract extends Presenter {
    /**
     * Sets the view within the presenter
     *
     * @param view The view that this presenter will control
     */
    void setView(@NonNull DrillsListView view);

    /**
     * Retrieves all the drills from the drill repository
     * @param filter The type of drill to filter for
     */
    void initialize(Type filter);

    /**
     * Tells the presenter that a drill has been clicked on
     * @param drillModel The drill that was clicked
     */
    void onDrillClicked(DrillModel drillModel);
}
