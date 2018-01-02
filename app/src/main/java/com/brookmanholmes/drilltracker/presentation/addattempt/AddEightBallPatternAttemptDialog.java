package com.brookmanholmes.drilltracker.presentation.addattempt;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.presentation.view.PatternPlayInputView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by brookman on 12/9/17.
 */

public class AddEightBallPatternAttemptDialog extends DialogFragment implements DialogInterface.OnClickListener {
    @BindView(R.id.patternPlayView)
    PatternPlayInputView patternPlayInputView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_pattern_input, container);
        ButterKnife.bind(this, view);
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_pattern_input, null);
        ButterKnife.bind(this, view);

        builder.setView(view)
                .setPositiveButton(R.string.dialog_add_attempt_title, this)
                .setNegativeButton(android.R.string.cancel, null);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        Log.i("AddPatternAttempt", "onClick: " + patternPlayInputView.getPattern());
    }
}
