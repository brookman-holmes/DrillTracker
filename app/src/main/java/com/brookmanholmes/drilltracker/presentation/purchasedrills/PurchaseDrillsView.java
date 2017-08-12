package com.brookmanholmes.drilltracker.presentation.purchasedrills;

import com.brookmanholmes.drilltracker.presentation.base.LoadDataView;
import com.brookmanholmes.drilltracker.presentation.model.DrillPackModel;

import java.util.List;

/**
 * Created by Brookman Holmes on 8/9/2017.
 */

interface PurchaseDrillsView extends LoadDataView {
    void renderDrillPacks(List<DrillPackModel> drillModelCollection);

    void viewDrillPack(DrillPackModel drillModel);

}
