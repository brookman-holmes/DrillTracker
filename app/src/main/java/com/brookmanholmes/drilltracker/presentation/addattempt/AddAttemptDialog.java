package com.brookmanholmes.drilltracker.presentation.addattempt;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.Preconditions;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.presentation.base.BaseDialogFragment;
import com.brookmanholmes.drilltracker.presentation.view.CustomNumberPicker;

/**
 * Created by Brookman Holmes on 7/28/2017.
 */
public class AddAttemptDialog extends BaseDialogFragment<AddAttemptDialogPresenter> {
    private static final String TAG = AddAttemptDialog.class.getName();

    private static final String PARAM_DRILL_ID = "param_drill_id";
    private static final String PARAM_MAX = "param_max";
    private static final String PARAM_TARGET = "param_target";
    private static final String PARAM_VALUE = "param_value";

    CustomNumberPicker picker;
    private int score, maxScore;

    public static AddAttemptDialog newInstance(String drillId, int maxScore, int targetScore) {
        AddAttemptDialog dialog = new AddAttemptDialog();
        Bundle args = new Bundle();
        args.putInt(PARAM_MAX, maxScore);
        args.putInt(PARAM_TARGET, targetScore);
        args.putString(PARAM_DRILL_ID, drillId);

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
        picker = (CustomNumberPicker) LayoutInflater.from(dialogBuilder.getContext())
                .inflate(R.layout.view_add_attempt, null);
        picker.setMax(maxScore);
        picker.setValue(score);
        dialogBuilder.setView(picker);
    }

    @Override
    protected String getTitle() {
        return getString(R.string.dialog_add_attempt_title);
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        presenter.addAttempt(getDrillId(), picker.getValue(), getTargetScore());
    }

    @Override
    protected AddAttemptDialogPresenter getPresenter() {
        return new AddAttemptDialogPresenter();
    }

    private int getMaxScore() {
        final Bundle arguments = getArguments();
        return arguments.getInt(PARAM_MAX);
    }

    private int getTargetScore() {
        final Bundle arguments = getArguments();
        return arguments.getInt(PARAM_TARGET);
    }

    private String getDrillId() {
        final Bundle arguments = getArguments();
        Preconditions.checkNotNull(arguments, "Fragment arguments cannot be null");
        return arguments.getString(PARAM_DRILL_ID);
    }

    private int getValue() {
        final Bundle arguments = getArguments();
        return arguments.getInt(PARAM_VALUE, getTargetScore());
    }
}
