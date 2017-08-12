package com.brookmanholmes.drilltracker.presentation.drills;

import com.brookmanholmes.drilltracker.presentation.model.DrillModel;

/**
 * Created by Brookman Holmes on 8/9/2017.
 */
public interface ActivityCallback {
    void setFilterSelection(DrillModel.Type type);
}
