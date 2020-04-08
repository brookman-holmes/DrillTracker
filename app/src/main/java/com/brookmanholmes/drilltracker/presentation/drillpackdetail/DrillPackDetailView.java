package com.brookmanholmes.drilltracker.presentation.drillpackdetail;

import com.brookmanholmes.drilltracker.presentation.base.LoadDataView;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;

import java.util.List;

/**
 * Created by Brookman Holmes on 7/7/2017.
 * Interface representing a view in MVP, displays a list {@link DrillModel} for the user to interact with
 */

interface DrillPackDetailView extends LoadDataView {
    /**
     * Displays the list of drills in the view
     *
     * @param drillModelList The collection of drills to display
     */
    void renderDrillList(List<DrillModel> drillModelList);
}
