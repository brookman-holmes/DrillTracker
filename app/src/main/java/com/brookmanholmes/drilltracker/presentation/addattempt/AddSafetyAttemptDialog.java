package com.brookmanholmes.drilltracker.presentation.addattempt;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.brookmanholmes.drilltracker.R;
import com.goodiebag.horizontalpicker.HorizontalPicker;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by brookman on 12/31/17.
 */

public class AddSafetyAttemptDialog extends DialogFragment implements DialogInterface.OnClickListener {
    @BindView(R.id.spinPicker)
    HorizontalPicker spinPicker;
    @BindView(R.id.speedPicker)
    HorizontalPicker speedPicker;
    @BindView(R.id.thicknessPicker)
    HorizontalPicker thicknessPicker;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_safety_attempt_input, null);
        ButterKnife.bind(this, view);
        spinPicker.setItems(getSpinItems(), 1);
        speedPicker.setItems(getSpeedItems(), 1);
        thicknessPicker.setItems(getThicknessItems(), 1);
        builder.setView(view)
                .setTitle("Add safety attempt")
                .setPositiveButton(R.string.dialog_add_attempt_title, this)
                .setNegativeButton(android.R.string.cancel, null);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        Log.i("AddPatternAttempt", "onClick: ");
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
