package com.brookmanholmes.drilltracker.presentation.purchasedrills;

import com.brookmanholmes.drilltracker.presentation.base.LoadDataView;
import com.brookmanholmes.drilltracker.presentation.model.DrillPackModel;

import java.util.List;

/**
 * Created by Brookman Holmes on 8/9/2017.
 * Interface representing a view in MVP, displays a list {@link DrillPackModel} for the user to interact with
 */

interface PurchaseDrillsView extends LoadDataView {
    /**
     * Updates the display with a list of drill packs for the user to see
     *
     * @param drillModelCollection The list of drill packs to display
     */
    void renderDrillPacks(List<DrillPackModel> drillModelCollection);

    /**
     * Show a more detailed view of a drill pack when the user clicks on drill pack
     * @param drillModel The drill pack to show
     */
    void viewDrillPack(DrillPackModel drillModel);
}
