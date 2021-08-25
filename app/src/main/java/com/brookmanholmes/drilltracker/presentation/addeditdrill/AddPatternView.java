package com.brookmanholmes.drilltracker.presentation.addeditdrill;

import java.util.List;

interface AddPatternView {
    void updatePattern(List<Integer> pattern);
    void onBallPressed(int ballPressed);
}
