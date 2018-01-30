package com.brookmanholmes.drilltracker.presentation.addeditdrill;

import android.support.annotation.NonNull;

import com.brookmanholmes.drilltracker.presentation.base.Presenter;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;

/**
 * Created by Brookman Holmes on 8/11/2017.
 */

interface AddEditDrillContract extends Presenter {
    /**
     * Sets the view that the presenter will interact with
     *
     * @param view The view the presenter will use
     */
    void setView(@NonNull AddEditDrillView view);

    /**
     * Sets the name of the drill to be added/edited
     *
     * @param drillName The name of the drill
     */
    void setDrillName(String drillName);

    /**
     * Sets the description of the drill to be added/edited
     *
     * @param drillDescription The description of the drill
     */
    void setDrillDescription(String drillDescription);

    /**
     * Sets the maximum score of the drill to be added/edited
     *
     * @param maximumScore The maximum score of the drill
     */
    void setMaximumScore(int maximumScore);

    /**
     * Sets the target score of the drill to be added/edited
     *
     * @param targetScore The target score of the drill
     */
    void setTargetScore(int targetScore);

    /**
     * sets the image bytes of the drill to be added/edited
     *
     * @param image The image of the drill
     */
    void setImage(byte[] image);

    /**
     * Sets the number of possible cue ball positions
     *
     * @param positions The number of possible positions
     */
    void setCbPositions(int positions);

    /**
     * Sets the number of possible object ball positions
     *
     * @param positions The number of possible positions
     */
    void setObPositions(int positions);

    /**
     * Saves the drill to the database/server/etc.
     */
    void saveDrill();

    /**
     * Sets the type of the drill to be added/edited
     *
     * @param type The type of the drill
     */
    void setDrillType(DrillModel.Type type);
}
