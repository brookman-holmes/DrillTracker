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
import com.brookmanholmes.drilltracker.presentation.model.SafetyDrillModel;
import com.goodiebag.horizontalpicker.HorizontalPicker;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Brookman Holmes on 7/28/2017.
 */
public class AddSafetyAttemptDialog extends BaseDialogFragment<AddSafetyAttemptDialogPresenter> {
    private static final String TAG = AddSafetyAttemptDialog.class.getName();

    private static final String PARAM_DRILL_ID = "param_drill_id";
    private static final String PARAM_VALUE = "param_value";
    private static final String PARAM_CB_POS = "param_cb_pos";
    private static final String PARAM_OB_POS = "param_ob_pos";
    private static final String PARAM_SELECTED_CB_POS = "param_selected_cb_pos";
    private static final String PARAM_SELECTED_OB_POS = "param_selected_ob_pos";

    @BindView(R.id.spinPicker)
    HorizontalPicker spinPicker;
    @BindView(R.id.speedPicker)
    HorizontalPicker speedPicker;
    @BindView(R.id.thicknessPicker)
    HorizontalPicker thicknessPicker;
    @BindView(R.id.obPositionsSpinner)
    Spinner obPositionsSpinner;
    @BindView(R.id.cbPositionsSpinner)
    Spinner cbPositionsSpinner;

    EnumSet<SafetyDrillModel.SafetyTypes> safetyTypes = EnumSet.noneOf(SafetyDrillModel.SafetyTypes.class);

    public static AddSafetyAttemptDialog newInstance(String drillId, int cbPositions, int obPositions, int selectedCbPosition, int selectedObPosition) {
        AddSafetyAttemptDialog dialog = new AddSafetyAttemptDialog();
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            safetyTypes = (EnumSet<SafetyDrillModel.SafetyTypes>) savedInstanceState.getSerializable(PARAM_VALUE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(PARAM_VALUE, getSafetyTypes());
    }

    @Override
    protected boolean hasTitle() {
        return true;
    }

    @Override
    protected void setDialogBuilderView(AlertDialog.Builder dialogBuilder) {
        View view = LayoutInflater.from(dialogBuilder.getContext())
                .inflate(R.layout.dialog_add_safety_attempt, null, false);
        ButterKnife.bind(this, view);
        spinPicker.setItems(getSpinItems(), 1);
        speedPicker.setItems(getSpeedItems(), 1);
        thicknessPicker.setItems(getThicknessItems(), 1);
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
        return getString(R.string.dialog_add_safety_attempt_title);
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        presenter.addAttempt(getDrillId(), getSafetyTypes(), obPositionsSpinner.getSelectedItemPosition() + 1, cbPositionsSpinner.getSelectedItemPosition() + 1);
    }

    @Override
    protected AddSafetyAttemptDialogPresenter getPresenter() {
        return new AddSafetyAttemptDialogPresenter();
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

    private SafetyDrillModel.SafetyTypes getSpinValue() {
        int value = spinPicker.getSelectedIndex();

        if (value == 0) {
            return SafetyDrillModel.SafetyTypes.SPIN_LESS;
        } else if (value == 1) {
            return SafetyDrillModel.SafetyTypes.SPIN_CORRECT;
        } else {
            return SafetyDrillModel.SafetyTypes.SPIN_MORE;
        }
    }

    private SafetyDrillModel.SafetyTypes getSpeedValue() {
        int value = speedPicker.getSelectedIndex();

        if (value == 0) {
            return SafetyDrillModel.SafetyTypes.SPEED_SOFT;
        } else if (value == 1) {
            return SafetyDrillModel.SafetyTypes.SPEED_CORRECT;
        } else {
            return SafetyDrillModel.SafetyTypes.SPEED_HARD;
        }
    }

    private SafetyDrillModel.SafetyTypes getThicknessValue() {
        int value = thicknessPicker.getSelectedIndex();

        if (value == 0) {
            return SafetyDrillModel.SafetyTypes.THICKNESS_THIN;
        } else if (value == 1) {
            return SafetyDrillModel.SafetyTypes.THICKNESS_CORRECT;
        } else {
            return SafetyDrillModel.SafetyTypes.THICKNESS_THICK;
        }
    }

    private EnumSet<SafetyDrillModel.SafetyTypes> getSafetyTypes() {
        return EnumSet.of(getSpeedValue(), getSpinValue(), getThicknessValue());
    }

    private List<HorizontalPicker.PickerItem> getSpinItems() {
        List<HorizontalPicker.PickerItem> result = new ArrayList<>();
        result.add(new HorizontalPicker.TextItem("Too little"));
        result.add(new HorizontalPicker.TextItem("Correct"));
        result.add(new HorizontalPicker.TextItem("Too much"));

        return result;
    }

    private List<HorizontalPicker.PickerItem> getSpeedItems() {
        List<HorizontalPicker.PickerItem> result = new ArrayList<>();
        result.add(new HorizontalPicker.TextItem("Too soft"));
        result.add(new HorizontalPicker.TextItem("Correct"));
        result.add(new HorizontalPicker.TextItem("Too hard"));

        return result;
    }

    private List<HorizontalPicker.PickerItem> getThicknessItems() {
        List<HorizontalPicker.PickerItem> result = new ArrayList<>();
        result.add(new HorizontalPicker.TextItem("Too thin"));
        result.add(new HorizontalPicker.TextItem("Correct"));
        result.add(new HorizontalPicker.TextItem("Too Thick"));

        return result;
    }
}
