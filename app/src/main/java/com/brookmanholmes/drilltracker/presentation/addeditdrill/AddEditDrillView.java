package com.brookmanholmes.drilltracker.presentation.addeditdrill;

import com.brookmanholmes.drilltracker.presentation.model.DrillModel;

/**
 * Created by Brookman Holmes on 7/23/2017.
 */

interface AddEditDrillView {
    /**
     * Populate the view with the drill data
     *
     * @param model The drill to display
     */
    void loadDrillData(DrillModel model);

    /**
     * Sets the whether or not all the requirements for creating/editing a drill have been fulfilled
     *
     * @param isComplete true if the drill is complete, false otherwise
     */
    void isDrillComplete(boolean isComplete);

    /**
     * Shows a drill to the user
     *
     * @param drill The drill to show
     */
    void showDrillDetailsView(DrillModel drill);

    /**
     * Removes the view from the stack (prevents the user from navigating back)
     */
    void finish();
}
