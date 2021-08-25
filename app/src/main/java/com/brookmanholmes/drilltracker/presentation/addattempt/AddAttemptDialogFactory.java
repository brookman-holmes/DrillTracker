package com.brookmanholmes.drilltracker.presentation.addattempt;


import static com.brookmanholmes.drilltracker.presentation.addattempt.AddAttemptDialog.PARAM_DRILL;
import static com.brookmanholmes.drilltracker.presentation.addattempt.AddAttemptDialog.PARAM_DRILL_ID;
import static com.brookmanholmes.drilltracker.presentation.addattempt.AddAttemptDialog.PARAM_SELECTED_CB_POS;
import static com.brookmanholmes.drilltracker.presentation.addattempt.AddAttemptDialog.PARAM_SELECTED_ENGLISH;
import static com.brookmanholmes.drilltracker.presentation.addattempt.AddAttemptDialog.PARAM_SELECTED_OB_POS;
import static com.brookmanholmes.drilltracker.presentation.addattempt.AddAttemptDialog.PARAM_SELECTED_SPEED;
import static com.brookmanholmes.drilltracker.presentation.addattempt.AddAttemptDialog.PARAM_SELECTED_TARGET_POS;
import static com.brookmanholmes.drilltracker.presentation.addattempt.AddAttemptDialog.PARAM_SELECTED_V_SPIN;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import com.brookmanholmes.drilltracker.presentation.model.DrillModel;
import com.brookmanholmes.drilltracker.presentation.model.English;
import com.brookmanholmes.drilltracker.presentation.model.Speed;
import com.brookmanholmes.drilltracker.presentation.model.VSpin;



public class AddAttemptDialogFactory {
    private AddAttemptDialogFactory() {
    }

    public static DialogFragment createDialog(DrillModel drill, int selectedCbPosition,
                                              int selectedObPosition, int selectedTargetPosition,
                                              English selectedEnglish, VSpin selectedVSpin,
                                              Speed selectedSpeed) {

        DialogFragment dialogFragment = new AddAttemptDialog();
        Bundle args = new Bundle();
        args.putString(PARAM_DRILL_ID, drill.getId());
        args.putInt(PARAM_SELECTED_TARGET_POS, selectedTargetPosition);
        args.putInt(PARAM_SELECTED_CB_POS, selectedCbPosition);
        args.putInt(PARAM_SELECTED_OB_POS, selectedObPosition);
        args.putSerializable(PARAM_SELECTED_ENGLISH, selectedEnglish);
        args.putSerializable(PARAM_SELECTED_V_SPIN, selectedVSpin);
        args.putSerializable(PARAM_SELECTED_SPEED, selectedSpeed);
        args.putSerializable(PARAM_DRILL, drill);

        /*

        args.putIntegerArrayList(PARAM_PATTERN, new ArrayList<>(selectedPattern));
        args.putInt(PARAM_TARGET_SCORE, targetScore);
        args.putInt(PARAM_CB_POS, cbPositions);
        args.putInt(PARAM_OB_POS, obPositions);
        args.putInt(PARAM_TARGET_POS, targetPositions);
        switch (type) {
            case AIMING:
            case BANKING:
            case KICKING:
                dialogFragment = new AddAimAttemptDialog();
                break;
            case SPEED:
                dialogFragment = new AddSpeedAttemptDialog();
                break;
            case SAFETY:
                dialogFragment = new AddSafetyAttemptDialog();
                break;
            case POSITIONAL:
                dialogFragment = new AddPositionalAttemptDialog();
                break;
            case PATTERN:
                dialogFragment = new AddPatternAttemptDialog();
                break;
            default:
                throw new IllegalArgumentException("This drill has not yet been implemented");
        }

         */

        dialogFragment.setArguments(args);
        return dialogFragment;
    }
}
