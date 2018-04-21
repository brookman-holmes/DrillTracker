package com.brookmanholmes.drilltracker.presentation.addattempt;


import android.support.v4.app.DialogFragment;

import com.brookmanholmes.drilltracker.presentation.model.DrillModel;

public class AddAttemptDialogFactory {
    private AddAttemptDialogFactory() {
    }

    public static DialogFragment createDialog(String drillId, DrillModel.Type type, int maxScore, int targetScore,
                                              int cbPositions, int obPositions, int targetPositions, int selectedCbPosition,
                                              int selectedObPosition, int selectedTargetPosition) {
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
            default:
                return new UnimplementedAttemptDialog();
        }
    }
}
