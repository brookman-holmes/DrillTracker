package com.brookmanholmes.drilltracker.presentation.drilldetail;

import com.brookmanholmes.drilltracker.presentation.base.LoadDataView;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;

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
}
