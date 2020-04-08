package com.brookmanholmes.drilltracker.presentation.drills;

import com.brookmanholmes.drilltracker.presentation.model.DrillModel;

/**
 * Created by Brookman Holmes on 8/9/2017.
 */

interface DrillFilter {
    DrillModel.Type getFilterSelection();

    DrillModel.Type onSelected();
}
