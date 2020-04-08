package com.brookmanholmes.drilltracker.presentation.drilldetail;

import com.brookmanholmes.drilltracker.presentation.base.Presenter;

interface AddPatternContract extends Presenter {
    void onNewPatternClicked(String drillId);
}
