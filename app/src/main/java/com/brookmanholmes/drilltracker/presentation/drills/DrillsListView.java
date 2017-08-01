package com.brookmanholmes.drilltracker.presentation.drills;

import com.brookmanholmes.drilltracker.presentation.model.DrillModel;
import com.brookmanholmes.drilltracker.presentation.base.LoadDataView;

import java.util.Collection;
import java.util.List;

/**
 * Created by Brookman Holmes on 7/7/2017.
 */

interface DrillsListView extends LoadDataView {
    void renderUserList(List<DrillModel> drillModelCollection);
    void viewDrill(DrillModel drillModel);
    void showCreateDrillActivity();
    void showDeleteConfirmation(DrillModel drillModel);
}
