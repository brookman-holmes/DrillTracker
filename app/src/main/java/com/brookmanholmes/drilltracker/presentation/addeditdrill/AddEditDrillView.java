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
     * @param drillId     The id of the drill
     * @param maxScore    The max score of the drill
     * @param targetScore The target score of the drill
     */
    void showDrillDetailsView(String drillId, int maxScore, int targetScore);

    /**
     * Removes the view from the stack (prevents the user from navigating back)
     */
    void finish();
}
