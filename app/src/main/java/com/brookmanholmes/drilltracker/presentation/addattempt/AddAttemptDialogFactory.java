package com.brookmanholmes.drilltracker.presentation.addattempt;


import androidx.fragment.app.DialogFragment;

import com.brookmanholmes.drilltracker.presentation.model.DrillModel;

import java.util.List;

public class AddAttemptDialogFactory {
    private AddAttemptDialogFactory() {
    }

    public static DialogFragment createDialog(String drillId, DrillModel.Type type, int targetScore,
                                              int cbPositions, int obPositions, int targetPositions, int selectedCbPosition,
                                              int selectedObPosition, int selectedTargetPosition, List<Integer> selectedPattern) {
        switch (type) {
            case AIMING:
            case BANKING:
            case KICKING:
                return AddAimAttemptDialog.newInstance(drillId, cbPositions, obPositions, selectedCbPosition, selectedObPosition);
            case SPEED:
                return AddSpeedAttemptDialog.newInstance(drillId, cbPositions, obPositions, selectedCbPosition, selectedObPosition);
            case SAFETY:
                return AddSafetyAttemptDialog.newInstance(drillId, cbPositions, obPositions, selectedCbPosition, selectedObPosition);
            case POSITIONAL:
                return AddPositionalAttemptDialog.newInstance(drillId, cbPositions, targetPositions, selectedTargetPosition, selectedCbPosition);
            case PATTERN:
                return AddPatternAttemptDialog.newInstance(drillId, targetScore, selectedPattern);
            default:
                return new UnimplementedAttemptDialog();
        }
    }
}
