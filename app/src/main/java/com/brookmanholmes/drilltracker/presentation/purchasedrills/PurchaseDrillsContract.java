package com.brookmanholmes.drilltracker.presentation.purchasedrills;

import android.support.annotation.NonNull;

import com.brookmanholmes.drilltracker.presentation.base.Presenter;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;

/**
 * Created by Brookman Holmes on 8/11/2017.
 */

interface PurchaseDrillsContract extends Presenter {
    /**
     * Sets the view within the presenter
     *
     * @param view The view that this presenter will control
     */
    void setView(@NonNull PurchaseDrillsView view);

    /**
     * Load the list of purchasable drills, filtering out items not matching the typeSelection
     *
     * @param typeSelection The type of drills to display
     */
    void loadDrillsList(DrillModel.Type typeSelection);
}
