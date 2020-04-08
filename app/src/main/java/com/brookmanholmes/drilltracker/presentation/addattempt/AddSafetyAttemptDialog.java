package com.brookmanholmes.drilltracker.presentation.addattempt;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.presentation.adapters.SpinnerAdapterHelper;
import com.brookmanholmes.drilltracker.presentation.base.BaseDialogFragment;
import com.brookmanholmes.drilltracker.presentation.model.SafetyDrillModel;
import com.goodiebag.horizontalpicker.HorizontalPicker;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.brookmanholmes.drilltracker.presentation.addattempt.AddAttemptDialog.PARAM_CB_POS;
import static com.brookmanholmes.drilltracker.presentation.addattempt.AddAttemptDialog.PARAM_DRILL_ID;
import static com.brookmanholmes.drilltracker.presentation.addattempt.AddAttemptDialog.PARAM_OB_POS;
import static com.brookmanholmes.drilltracker.presentation.addattempt.AddAttemptDialog.PARAM_SELECTED_CB_POS;
import static com.brookmanholmes.drilltracker.presentation.addattempt.AddAttemptDialog.PARAM_SELECTED_OB_POS;
import static com.brookmanholmes.drilltracker.presentation.addattempt.AddAttemptDialog.PARAM_SPEED_VALUE;
import static com.brookmanholmes.drilltracker.presentation.addattempt.AddAttemptDialog.PARAM_SPIN_VALUE;
import static com.brookmanholmes.drilltracker.presentation.addattempt.AddAttemptDialog.PARAM_THICKNESS_VALUE;

/**
 * Created by Brookman Holmes on 7/28/2017.
 */
public class AddSafetyAttemptDialog extends BaseDialogFragment<AddSafetyAttemptPresenter> {
    private static final String TAG = AddSafetyAttemptDialog.class.getName();
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

    private SafetyDrillModel.SafetyTypes speedSelection = SafetyDrillModel.SafetyTypes.SPEED_CORRECT;
    private SafetyDrillModel.SafetyTypes spinSelection = SafetyDrillModel.SafetyTypes.SPIN_CORRECT;
    private SafetyDrillModel.SafetyTypes thicknessSelection = SafetyDrillModel.SafetyTypes.THICKNESS_CORRECT;

    static AddSafetyAttemptDialog newInstance(String drillId, int cbPositions, int obPositions, int selectedCbPosition, int selectedObPosition) {
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
            spinSelection = (SafetyDrillModel.SafetyTypes) savedInstanceState.getSerializable(PARAM_SPIN_VALUE);
            speedSelection = (SafetyDrillModel.SafetyTypes) savedInstanceState.getSerializable(PARAM_SPEED_VALUE);
            thicknessSelection = (SafetyDrillModel.SafetyTypes) savedInstanceState.getSerializable(PARAM_THICKNESS_VALUE);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(PARAM_SPEED_VALUE, getSpeedValue());
        outState.putSerializable(PARAM_SPIN_VALUE, getSpinValue());
        outState.putSerializable(PARAM_THICKNESS_VALUE, getThicknessValue());
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
        spinPicker.setItems(createPickerItems("Too little", "Correct", "Too much"), getSafetyTypesSelectionValue(spinSelection));
        speedPicker.setItems(createPickerItems("Too soft", "Correct", "Too hard"), getSafetyTypesSelectionValue(speedSelection));
        thicknessPicker.setItems(createPickerItems("Too thin", "Correct", "Too thick"), getSafetyTypesSelectionValue(thicknessSelection));

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
    protected AddSafetyAttemptPresenter getPresenter() {
        return new AddSafetyAttemptPresenter();
    }

    private String getDrillId() {
        return Objects.requireNonNull(getArguments()).getString(PARAM_DRILL_ID);
    }

    private int getCueBallPositions() {
        return Objects.requireNonNull(getArguments()).getInt(PARAM_CB_POS, 1);
    }

    private int getObBallPositions() {
        return Objects.requireNonNull(getArguments()).getInt(PARAM_OB_POS, 1);
    }

    private int getDefaultSelectedObBallPosition() {
        int selection = Objects.requireNonNull(getArguments()).getInt(PARAM_SELECTED_OB_POS, 0);
        if (selection > 0)
            return selection - 1;
        else return selection;
    }

    private int getDefaultSelectedCbBallPosition() {
        int selection = Objects.requireNonNull(getArguments()).getInt(PARAM_SELECTED_CB_POS, 0);
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

    private int getSafetyTypesSelectionValue(SafetyDrillModel.SafetyTypes type) {
        switch (type) {
            case SPEED_HARD:
                return 2;
            case SPEED_SOFT:
                return 0;
            case SPEED_CORRECT:
                return 1;
            case SPIN_LESS:
                return 0;
            case SPIN_MORE:
                return 2;
            case SPIN_CORRECT:
                return 1;
            case THICKNESS_THIN:
                return 0;
            case THICKNESS_THICK:
                return 2;
            case THICKNESS_CORRECT:
                return 1;
            default:
                return 1;
        }
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

    private List<HorizontalPicker.PickerItem> createPickerItems(String... items) {
        List<HorizontalPicker.PickerItem> result = new ArrayList<>();
        for (String item : items) {
            result.add(new HorizontalPicker.TextItem(item));
        }

        return result;
    }
}
