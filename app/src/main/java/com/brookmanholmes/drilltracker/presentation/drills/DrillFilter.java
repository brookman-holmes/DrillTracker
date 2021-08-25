package com.brookmanholmes.drilltracker.presentation.drills;

import com.brookmanholmes.drilltracker.presentation.model.Type;

/**
 * Created by Brookman Holmes on 8/9/2017.
 */

interface DrillFilter {
    Type getFilterSelection();

    Type onSelected();
}
