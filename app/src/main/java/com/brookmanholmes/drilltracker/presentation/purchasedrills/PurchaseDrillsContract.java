package com.brookmanholmes.drilltracker.presentation.purchasedrills;

import androidx.annotation.NonNull;

import com.brookmanholmes.drilltracker.presentation.base.Presenter;

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
     */
    void loadDrillsList();

    /**
     * Purchase a drill pack for the user (add the drills within the drill pack into the user's list
     * of available drills
     *
     * @param sku The sku of the drill pack to purchase
     */
    void purchaseDrillPack(String sku);
}
