package com.brookmanholmes.drilltracker.presentation.drilldetail;

import androidx.annotation.NonNull;

import com.brookmanholmes.drilltracker.presentation.base.Presenter;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;

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
    void initialize(String drillId);

    /**
     * Removes the last attempt that the user entered
     */
    void onUndoClicked();

    /**
     * Displays the dialog for adding an attempt to a drill
     *
     * @param drillId                The ID of the drill
     * @param type                   The type of drill
     * @param maxScore               the maximum score possible in the drill
     * @param targetScore            The target score for the drill
     * @param cbPositions            The number of possible cue ball positions
     * @param obPositions            The number of possible object ball positions
     * @param targetPositions        The number of possible target positions
     * @param selectedCbPosition     The selected position for the cue ball
     * @param selectedObPosition     The selected position of the object ball
     * @param selectedTargetPosition The selected position of the target
     * @param selectedPattern        The selected pattern of the attempt
     */
    void onAddAttemptClicked(String drillId, DrillModel.Type type, int maxScore, int targetScore,
                             int cbPositions, int obPositions, int targetPositions, int selectedCbPosition,
                             int selectedObPosition, int selectedTargetPosition, List<Integer> selectedPattern);
}
