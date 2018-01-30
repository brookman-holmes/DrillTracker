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
import com.brookmanholmes.drilltracker.presentation.view.CustomNumberPicker;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Brookman Holmes on 7/28/2017.
 */
public class AddAttemptDialog extends BaseDialogFragment<AddAttemptDialogPresenter> {
    private static final String TAG = AddAttemptDialog.class.getName();

    private static final String PARAM_DRILL_ID = "param_drill_id";
    private static final String PARAM_MAX = "param_max";
    private static final String PARAM_TARGET = "param_target";
    private static final String PARAM_VALUE = "param_value";
    private static final String PARAM_CB_POS = "param_cb_pos";
    private static final String PARAM_OB_POS = "param_ob_pos";
    private static final String PARAM_SELECTED_CB_POS = "param_selected_cb_pos";
    private static final String PARAM_SELECTED_OB_POS = "param_selected_ob_pos";

    @BindView(R.id.number_picker)
    CustomNumberPicker picker;
    @BindView(R.id.obPositionsSpinner)
    Spinner obPositionsSpinner;
    @BindView(R.id.cbPositionsSpinner)
    Spinner cbPositionsSpinner;

    private int score, maxScore;

    public static AddAttemptDialog newInstance(String drillId, int maxScore, int targetScore, int cbPositions, int obPositions, int selectedCbPosition, int selectedObPosition) {
        AddAttemptDialog dialog = new AddAttemptDialog();
        Bundle args = new Bundle();
        args.putInt(PARAM_MAX, maxScore);
        args.putInt(PARAM_TARGET, targetScore);
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
        maxScore = getMaxScore();

        if (savedInstanceState != null) {
            score = savedInstanceState.getInt(PARAM_VALUE);
        } else {
            score = getValue();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
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

    @Override
    protected AddAttemptDialogPresenter getPresenter() {
        return new AddAttemptDialogPresenter();
    }

    private int getMaxScore() {
        return getArguments().getInt(PARAM_MAX);
    }

    private int getTargetScore() {
        return getArguments().getInt(PARAM_TARGET);
    }

    private String getDrillId() {
        return getArguments().getString(PARAM_DRILL_ID);
    }

    private int getValue() {
        return getArguments().getInt(PARAM_VALUE, getTargetScore());
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
