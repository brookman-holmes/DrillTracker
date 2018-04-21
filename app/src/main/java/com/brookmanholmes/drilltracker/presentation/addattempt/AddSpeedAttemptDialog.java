package com.brookmanholmes.drilltracker.presentation.addattempt;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Spinner;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.presentation.adapters.SpinnerAdapterHelper;
import com.brookmanholmes.drilltracker.presentation.base.BaseDialogFragment;
import com.brookmanholmes.drilltracker.presentation.view.CustomNumberPickerV2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by brookman on 1/25/18.
 */

public class AddSpeedAttemptDialog extends BaseDialogFragment<AddSpeedAttemptDialogPresenter> {
    private static final String PARAM_DRILL_ID = "param_drill_id";
    private static final String PARAM_CB_POS = "param_cb_pos";
    private static final String PARAM_OB_POS = "param_ob_pos";
    private static final String PARAM_SELECTED_CB_POS = "param_selected_cb_pos";
    private static final String PARAM_SELECTED_OB_POS = "param_selected_ob_pos";

    @BindView(R.id.twoDiamondsSlowPicker)
    CustomNumberPickerV2 twoDiamondsSlowPicker;
    @BindView(R.id.oneDiamondsSlowPicker)
    CustomNumberPickerV2 oneDiamondsSlowPicker;
    @BindView(R.id.halfDiamondsSlowPicker)
    CustomNumberPickerV2 halfDiamondsSlowPicker;
    @BindView(R.id.quarterDiamondsSlowPicker)
    CustomNumberPickerV2 quarterDiamondsSlowPicker;
    @BindView(R.id.correctSpeedPicker)
    CustomNumberPickerV2 correctSpeedPicker;
    @BindView(R.id.quarterDiamondsFastPicker)
    CustomNumberPickerV2 quarterDiamondsFastPicker;
    @BindView(R.id.halfDiamondsFastPicker)
    CustomNumberPickerV2 halfDiamondsFastPicker;
    @BindView(R.id.oneDiamondsFastPicker)
    CustomNumberPickerV2 oneDiamondsFastPicker;
    @BindView(R.id.twoDiamondsFastPicker)
    CustomNumberPickerV2 twoDiamondsFastPicker;

    @BindView(R.id.obPositionsSpinner)
    Spinner obPositionsSpinner;
    @BindView(R.id.cbPositionsSpinner)
    Spinner cbPositionsSpinner;

    static AddSpeedAttemptDialog newInstance(String drillId, int cbPositions, int obPositions, int selectedCbPosition, int selectedObPosition) {
        AddSpeedAttemptDialog dialog = new AddSpeedAttemptDialog();
        Bundle args = new Bundle();
        args.putString(PARAM_DRILL_ID, drillId);
        args.putInt(PARAM_CB_POS, cbPositions);
        args.putInt(PARAM_OB_POS, obPositions);
        args.putInt(PARAM_SELECTED_CB_POS, selectedCbPosition);
        args.putInt(PARAM_SELECTED_OB_POS, selectedObPosition);
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    protected boolean hasTitle() {
        return true;
    }

    @Override
    protected void setDialogBuilderView(AlertDialog.Builder dialogBuilder) {
        View view = LayoutInflater.from(dialogBuilder.getContext())
                .inflate(R.layout.dialog_add_speed_attempt, null, false);
        ButterKnife.bind(this, view);
        obPositionsSpinner.setAdapter(SpinnerAdapterHelper.createNumberedListAdapter(getContext(), 1, getObBallPositions() + 1, "OB Position "));
        cbPositionsSpinner.setAdapter(SpinnerAdapterHelper.createNumberedListAdapter(getContext(), 1, getCueBallPositions() + 1, "CB Position "));
        obPositionsSpinner.setSelection(getDefaultSelectedObBallPosition());
        cbPositionsSpinner.setSelection(getDefaultSelectedCbBallPosition());
        if (!showCueBallSpinner()) {
            cbPositionsSpinner.setVisibility(View.GONE);
        }
        if (!showObjectBallSpinner()) {
            obPositionsSpinner.setVisibility(View.GONE);
        }
        dialogBuilder.setView(view);
    }

    @Override
    protected String getTitle() {
        return "Add Speed Attempt";
    }

    @Override
    protected AddSpeedAttemptDialogPresenter getPresenter() {
        return new AddSpeedAttemptDialogPresenter();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        presenter.addAttempt(
                getDrillId(),
                obPositionsSpinner.getSelectedItemPosition() + 1,
                cbPositionsSpinner.getSelectedItemPosition() + 1,
                twoDiamondsSlowPicker.getValue(),
                oneDiamondsSlowPicker.getValue(),
                halfDiamondsSlowPicker.getValue(),
                quarterDiamondsSlowPicker.getValue(),
                correctSpeedPicker.getValue(),
                quarterDiamondsFastPicker.getValue(),
                halfDiamondsFastPicker.getValue(),
                oneDiamondsFastPicker.getValue(),
                twoDiamondsFastPicker.getValue()
        );
    }

    private String getDrillId() {
        return getArguments().getString(PARAM_DRILL_ID);
    }

    private int getCueBallPositions() {
        return getArguments().getInt(PARAM_CB_POS, 1);
    }

    private int getObBallPositions() {
        return getArguments().getInt(PARAM_OB_POS, 1);
    }

    private int getDefaultSelectedObBallPosition() {
        int selection = getArguments().getInt(PARAM_SELECTED_OB_POS, 0);
        if (selection > 0)
            return selection - 1;
        else return selection;
    }

    private int getDefaultSelectedCbBallPosition() {
        int selection = getArguments().getInt(PARAM_SELECTED_CB_POS, 0);
        if (selection > 0)
            return selection - 1;
        else return selection;
    }

    private boolean showCueBallSpinner() {
        return getCueBallPositions() > 1;
    }

    private boolean showObjectBallSpinner() {
        return getObBallPositions() > 1;
    }
}
