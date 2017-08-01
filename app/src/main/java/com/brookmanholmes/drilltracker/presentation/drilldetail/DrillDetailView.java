package com.brookmanholmes.drilltracker.presentation.drilldetail;

import com.brookmanholmes.drilltracker.presentation.base.LoadDataView;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;

/**
 * Created by Brookman Holmes on 7/11/2017.
 */

interface DrillDetailView extends LoadDataView {
    void renderDrill(final DrillModel drill);
    void onFabClicked();
    void showAddAttemptView();
    void showDrillImageFullScreen(DrillModel drillModel);

    void showEditDrillView(String drillId);
}
