package com.brookmanholmes.drilltracker.presentation.drills;

import com.brookmanholmes.drilltracker.presentation.base.LoadDataView;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;

import java.util.List;

/**
 * Created by Brookman Holmes on 7/7/2017.
 */

interface DrillsListView extends LoadDataView {
    void renderDrillList(List<DrillModel> drillModelCollection);
    void viewDrill(DrillModel drillModel);
    void showDeleteConfirmation(DrillModel drillModel);
}
