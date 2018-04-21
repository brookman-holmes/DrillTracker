package com.brookmanholmes.drilltracker.presentation.addattempt;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Spinner;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.presentation.adapters.SpinnerAdapterHelper;
import com.brookmanholmes.drilltracker.presentation.base.BaseDialogFragment;
import com.brookmanholmes.drilltracker.presentation.model.PositionalDrillModel;
import com.goodiebag.horizontalpicker.HorizontalPicker;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddPositionalAttemptDialog extends BaseDialogFragment<AddPositionalAttemptDialogPresenter> {

    private static final String PARAM_DRILL_ID = "param_drill_id";
    private static final String PARAM_SPIN_VALUE = "param_spin_value";
    private static final String PARAM_THICKNESS_VALUE = "param_thickness_value";
    private static final String PARAM_SPEED_VALUE = "param_speed_value";
    private static final String PARAM_TARGET_DISTANCE = "param_target_distance";
    private static final String PARAM_CB_POS = "param_cb_pos";
    private static final String PARAM_TARGET_POS = "param_ob_pos";
    private static final String PARAM_SELECTED_TARGET_POS = "param_selected_cb_pos";
    private static final String PARAM_SELECTED_CB_POS = "param_selected_ob_pos";

    @BindView(R.id.hSpinPicker)
    HorizontalPicker hSpinPicker;
    @BindView(R.id.speedPicker)
    HorizontalPicker speedPicker;
    @BindView(R.id.vSpinPicker)
    HorizontalPicker vSpinPicker;
    @BindView(R.id.obPositionsSpinner)
    Spinner targetPositionsSpinner;
    @BindView(R.id.cbPositionsSpinner)
    Spinner cbPositionsSpinner;
    @BindView(R.id.distanceFromTargetPicker)
    HorizontalPicker targetDistancePicker;

    private PositionalDrillModel.PositionalTypes speedSelection = PositionalDrillModel.PositionalTypes.SPEED_CORRECT;
    private PositionalDrillModel.PositionalTypes vSpinSelection = PositionalDrillModel.PositionalTypes.V_SPIN_CORRECT;
    private PositionalDrillModel.PositionalTypes hSpinSelection = PositionalDrillModel.PositionalTypes.H_SPIN_CORRECT;
    private PositionalDrillModel.PositionalTypes targetDistanceSelection = PositionalDrillModel.PositionalTypes.ZERO_INCHES;

    static AddPositionalAttemptDialog newInstance(String drillId, int cbPositions, int targetPositions, int selectedTargetPosition, int selectedCbPosition) {
        AddPositionalAttemptDialog dialog = new AddPositionalAttemptDialog();
        Bundle args = new Bundle();
        args.putString(PARAM_DRILL_ID, drillId);
        args.putInt(PARAM_CB_POS, cbPositions);
        args.putInt(PARAM_TARGET_POS, targetPositions);
        args.putInt(PARAM_SELECTED_TARGET_POS, selectedTargetPosition);
        args.putInt(PARAM_SELECTED_CB_POS, selectedCbPosition);
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            vSpinSelection = (PositionalDrillModel.PositionalTypes) savedInstanceState.getSerializable(PARAM_SPIN_VALUE);
            speedSelection = (PositionalDrillModel.PositionalTypes) savedInstanceState.getSerializable(PARAM_SPEED_VALUE);
            hSpinSelection = (PositionalDrillModel.PositionalTypes) savedInstanceState.getSerializable(PARAM_THICKNESS_VALUE);
            targetDistanceSelection = (PositionalDrillModel.PositionalTypes) savedInstanceState.getSerializable(PARAM_TARGET_DISTANCE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(PARAM_SPEED_VALUE, getSpeedValue());
        outState.putSerializable(PARAM_SPIN_VALUE, getVSpinValue());
        outState.putSerializable(PARAM_THICKNESS_VALUE, getHSpinValue());
        outState.putSerializable(PARAM_TARGET_DISTANCE, getTargetDistanceValue());
    }

    @Override
    protected boolean hasTitle() {
        return true;
    }

    @Override
    protected void setDialogBuilderView(AlertDialog.Builder dialogBuilder) {
        View view = LayoutInflater.from(dialogBuilder.getContext())
                .inflate(R.layout.dialog_add_positional_attempt, null, false);
        ButterKnife.bind(this, view);

        vSpinPicker.setItems(createPickerItems("Too little", "Correct", "Too much"), getPositionalTypeSelection(vSpinSelection));
        hSpinPicker.setItems(createPickerItems("Too little", "Correct", "Too much"), getPositionalTypeSelection(hSpinSelection));
        speedPicker.setItems(createPickerItems("Too soft", "Correct", "Too hard"), getPositionalTypeSelection(speedSelection));
        targetDistancePicker.setItems(createPickerItems("0\"", "6\"", "12\"", "18-24\"", "24+\""), getPositionalTypeSelection(targetDistanceSelection));

        targetPositionsSpinner.setAdapter(SpinnerAdapterHelper.createNumberedListAdapter(getContext(), 1, getTargetPositions() + 1, "Target "));
        cbPositionsSpinner.setAdapter(SpinnerAdapterHelper.createNumberedListAdapter(getContext(), 1, getCueBallPositions() + 1, "CB Position "));
        targetPositionsSpinner.setSelection(getDefaultSelectedTargetPosition());
        cbPositionsSpinner.setSelection(getDefaultSelectedCbBallPosition());
        if (!showCueBallSpinner()) {
            cbPositionsSpinner.setVisibility(View.GONE);
        }
        if (!showTargetPositionsSpinner()) {
            targetPositionsSpinner.setVisibility(View.GONE);
        }

        dialogBuilder.setView(view);
    }

    @Override
    protected String getTitle() {
        return getString(R.string.dialog_add_attempt_title);
    }

    @Override
    protected AddPositionalAttemptDialogPresenter getPresenter() {
        return new AddPositionalAttemptDialogPresenter();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        presenter.addAttempt(
                getDrillId(),
                cbPositionsSpinner.getSelectedItemPosition() + 1,
                targetPositionsSpinner.getSelectedItemPosition() + 1,
                EnumSet.of(getHSpinValue(), getSpeedValue(), getVSpinValue(), getTargetDistanceValue()));
    }

    private String getDrillId() {
        return getArguments().getString(PARAM_DRILL_ID);
    }

    private int getCueBallPositions() {
        return getArguments().getInt(PARAM_CB_POS, 1);
    }

    private int getTargetPositions() {
        return getArguments().getInt(PARAM_TARGET_POS, 1);
    }

    private int getDefaultSelectedCbBallPosition() {
        int selection = getArguments().getInt(PARAM_SELECTED_CB_POS, 0);
        if (selection > 0)
            return selection - 1;
        else return selection;
    }

    private int getDefaultSelectedTargetPosition() {
        int selection = getArguments().getInt(PARAM_SELECTED_TARGET_POS, 0);
        if (selection > 0)
            return selection - 1;
        else return selection;
    }

    private boolean showCueBallSpinner() {
        return getCueBallPositions() > 1;
    }

    private boolean showTargetPositionsSpinner() {
        return getTargetPositions() > 1;
    }

    private PositionalDrillModel.PositionalTypes getVSpinValue() {
        int value = vSpinPicker.getSelectedIndex();

        if (value == 0) {
            return PositionalDrillModel.PositionalTypes.V_SPIN_LESS;
        } else if (value == 1) {
            return PositionalDrillModel.PositionalTypes.V_SPIN_CORRECT;
        } else {
            return PositionalDrillModel.PositionalTypes.V_SPIN_MORE;
        }
    }

    private PositionalDrillModel.PositionalTypes getTargetDistanceValue() {
        int value = targetDistancePicker.getSelectedIndex();

        if (value == 0) {
            return PositionalDrillModel.PositionalTypes.ZERO_INCHES;
        } else if (value == 1) {
            return PositionalDrillModel.PositionalTypes.SIX_INCHES;
        } else if (value == 2) {
            return PositionalDrillModel.PositionalTypes.TWELVE_INCHES;
        } else if (value == 3) {
            return PositionalDrillModel.PositionalTypes.EIGHTEEN_INCHES;
        } else {
            return PositionalDrillModel.PositionalTypes.TWENTY_FOUR_INCHES;
        }
    }

    private PositionalDrillModel.PositionalTypes getHSpinValue() {
        int value = hSpinPicker.getSelectedIndex();

        if (value == 0) {
            return PositionalDrillModel.PositionalTypes.H_SPIN_LESS;
        } else if (value == 1) {
            return PositionalDrillModel.PositionalTypes.H_SPIN_CORRECT;
        } else {
            return PositionalDrillModel.PositionalTypes.H_SPIN_MORE;
        }
    }

    private PositionalDrillModel.PositionalTypes getSpeedValue() {
        int value = speedPicker.getSelectedIndex();

        if (value == 0) {
            return PositionalDrillModel.PositionalTypes.SPEED_SOFT;
        } else if (value == 1) {
            return PositionalDrillModel.PositionalTypes.SPEED_CORRECT;
        } else {
            return PositionalDrillModel.PositionalTypes.SPEED_HARD;
        }
    }

    private int getPositionalTypeSelection(PositionalDrillModel.PositionalTypes type) {
        switch (type) {
            case SPEED_HARD:
                return 2;
            case SPEED_SOFT:
                return 0;
            case SPEED_CORRECT:
                return 1;
            case V_SPIN_LESS:
                return 0;
            case V_SPIN_MORE:
                return 2;
            case V_SPIN_CORRECT:
                return 1;
            case H_SPIN_LESS:
                return 0;
            case H_SPIN_MORE:
                return 2;
            case H_SPIN_CORRECT:
                return 1;
            case ZERO_INCHES:
                return 0;
            case SIX_INCHES:
                return 1;
            case TWELVE_INCHES:
                return 2;
            case EIGHTEEN_INCHES:
                return 3;
            case TWENTY_FOUR_INCHES:
                return 4;
            default:
                return 1;
        }
    }

    private List<HorizontalPicker.PickerItem> createPickerItems(String... items) {
        List<HorizontalPicker.PickerItem> result = new ArrayList<>();
        for (String item : items) {
            result.add(new HorizontalPicker.TextItem(item));
        }

        return result;
    }
}
