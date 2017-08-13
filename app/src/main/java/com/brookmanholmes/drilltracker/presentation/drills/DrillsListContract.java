package com.brookmanholmes.drilltracker.presentation.drills;

import android.support.annotation.NonNull;

import com.brookmanholmes.drilltracker.presentation.base.Presenter;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;

/**
 * Created by Brookman Holmes on 8/11/2017.
 * Interface representing the contract between the {@link DrillsListPresenter} and the
 * {@link DrillsListView}
 */

interface DrillsListContract extends Presenter {
    /**
     * @param view
     */
    void setView(@NonNull DrillsListView view);

    /**
     * @param filter
     */
    void initialize(DrillModel.Type filter);

    /**
     * @param filter
     */
    void loadDrillsList(DrillModel.Type filter);

    /**
     * @param drillModel
     */
    void onDrillClicked(DrillModel drillModel);
}
