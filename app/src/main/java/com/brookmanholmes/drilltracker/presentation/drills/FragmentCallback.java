package com.brookmanholmes.drilltracker.presentation.drills;

import com.brookmanholmes.drilltracker.presentation.model.DrillModel;

/**
 * Created by Brookman Holmes on 8/9/2017.
 */
public interface FragmentCallback {
    void addListener(ActivityCallback callback);

    void removeListener(ActivityCallback callback);

    DrillModel.Type getTypeSelection();

    void showLoading();

    void hideLoading();

    void showRetry();

    void hideRetry();

    void showCreateDrillActivity();
}
