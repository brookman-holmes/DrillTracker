package com.brookmanholmes.drilltracker.presentation.drilldetail;

import com.brookmanholmes.drilltracker.presentation.base.LoadDataView;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;

import java.util.List;

/**
 * Created by Brookman Holmes on 7/11/2017.
 * Interface representing a view in MVP, displays a {@link DrillModel} for the user to interact with
 */

interface DrillDetailsView extends LoadDataView {
    /**
     * Renders a drill for the user to see
     *
     * @param drill The drill to be displayed
     */
    void renderDrill(final DrillModel drill);

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
    void showAddAttemptDialog(String drillId, DrillModel.Type type, int maxScore, int targetScore,
                              int cbPositions, int obPositions, int targetPositions, int selectedCbPosition,
                              int selectedObPosition, int selectedTargetPosition, List<Integer> selectedPattern);
}
