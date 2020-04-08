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
import com.brookmanholmes.drilltracker.presentation.view.CustomNumberPicker;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Brookman Holmes on 7/28/2017.
 */
public abstract class AddAttemptDialog extends BaseDialogFragment<AddAttemptPresenter> {

    static final String PARAM_DRILL_ID = "param_drill_id";
    private static final String PARAM_MAX = "param_max";
    private static final String PARAM_TARGET = "param_target";
    static final String PARAM_VALUE = "param_value";
    static final String PARAM_CB_POS = "param_cb_pos";
    static final String PARAM_OB_POS = "param_ob_pos";
    static final String PARAM_SELECTED_CB_POS = "param_selected_cb_pos";
    static final String PARAM_SELECTED_OB_POS = "param_selected_ob_pos";
    static final String PARAM_OVER_CUTS = "param_over_cuts";
    static final String PARAM_UNDER_CUTS = "param_under_cuts";
    static final String PARAM_ENGLISH = "param_english";
    static final String PARAM_TARGET_SCORE = "param_target_score";
    static final String PARAM_PATTERN = "param_pattern";
    static final String PARAM_SPIN_VALUE = "param_spin_value";
    static final String PARAM_THICKNESS_VALUE = "param_thickness_value";
    static final String PARAM_SPEED_VALUE = "param_speed_value";
    static final String PARAM_TARGET_DISTANCE = "param_target_distance";
    static final String PARAM_TARGET_POS = "param_ob_pos";
    static final String PARAM_SELECTED_TARGET_POS = "param_selected_cb_pos";

    @BindView(R.id.number_picker)
    CustomNumberPicker picker;
    @BindView(R.id.obPositionsSpinner)
    Spinner obPositionsSpinner;
    @BindView(R.id.cbPositionsSpinner)
    Spinner cbPositionsSpinner;

    private int score, maxScore;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        maxScore = getMaxScore();

        if (savedInstanceState != null) {
            score = savedInstanceState.getInt(PARAM_VALUE);
        } else {
            score = getValue();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PARAM_VALUE, picker.getValue());
    }

    @Override
    protected boolean hasTitle() {
        return true;
    }

    @Override
    protected void setDialogBuilderView(AlertDialog.Builder dialogBuilder) {
        View view = LayoutInflater.from(dialogBuilder.getContext())
                .inflate(R.layout.dialog_add_attempt, null, false);
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
        picker.setMax(maxScore);
        picker.setValue(score);
        dialogBuilder.setView(view);
    }

    @Override
    protected String getTitle() {
        return getString(R.string.dialog_add_attempt_title);
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        presenter.addAttempt(getDrillId(), picker.getValue(), getTargetScore(), obPositionsSpinner.getSelectedItemPosition() + 1, cbPositionsSpinner.getSelectedItemPosition() + 1);
    }

    private int getMaxScore() {
        return Objects.requireNonNull(getArguments()).getInt(PARAM_MAX);
    }

    private int getTargetScore() {
        return Objects.requireNonNull(getArguments()).getInt(PARAM_TARGET);
    }

    private String getDrillId() {
        return Objects.requireNonNull(getArguments()).getString(PARAM_DRILL_ID);
    }

    private int getValue() {
        return Objects.requireNonNull(getArguments()).getInt(PARAM_VALUE, getTargetScore());
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
}
