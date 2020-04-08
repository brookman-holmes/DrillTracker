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
import com.brookmanholmes.drilltracker.presentation.model.English;
import com.brookmanholmes.drilltracker.presentation.view.CustomNumberPickerV2;
import com.goodiebag.horizontalpicker.HorizontalPicker;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.brookmanholmes.drilltracker.presentation.addattempt.AddAttemptDialog.PARAM_CB_POS;
import static com.brookmanholmes.drilltracker.presentation.addattempt.AddAttemptDialog.PARAM_DRILL_ID;
import static com.brookmanholmes.drilltracker.presentation.addattempt.AddAttemptDialog.PARAM_ENGLISH;
import static com.brookmanholmes.drilltracker.presentation.addattempt.AddAttemptDialog.PARAM_OB_POS;
import static com.brookmanholmes.drilltracker.presentation.addattempt.AddAttemptDialog.PARAM_OVER_CUTS;
import static com.brookmanholmes.drilltracker.presentation.addattempt.AddAttemptDialog.PARAM_SELECTED_CB_POS;
import static com.brookmanholmes.drilltracker.presentation.addattempt.AddAttemptDialog.PARAM_SELECTED_OB_POS;
import static com.brookmanholmes.drilltracker.presentation.addattempt.AddAttemptDialog.PARAM_UNDER_CUTS;
import static com.brookmanholmes.drilltracker.presentation.addattempt.AddAttemptDialog.PARAM_VALUE;

/**
 * Created by Brookman Holmes on 7/28/2017.
 */
public class AddAimAttemptDialog extends BaseDialogFragment<AddAimAttemptPresenter> {
    private static final String TAG = AddAimAttemptDialog.class.getName();

    @BindView(R.id.number_picker)
    CustomNumberPickerV2 makesPicker;
    @BindView(R.id.overCutsPicker)
    CustomNumberPickerV2 overCutsPicker;
    @BindView(R.id.underCutsPicker)
    CustomNumberPickerV2 underCutsPicker;
    @BindView(R.id.englishPicker)
    HorizontalPicker englishPicker;
    @BindView(R.id.obPositionsSpinner)
    Spinner obPositionsSpinner;
    @BindView(R.id.cbPositionsSpinner)
    Spinner cbPositionsSpinner;

    private int overCuts = 0;
    private int underCuts = 0;
    private int ballsMade = 0;
    private int english = 2;

    static AddAimAttemptDialog newInstance(String drillId, int cbPositions, int obPositions, int selectedCbPosition, int selectedObPosition) {
        AddAimAttemptDialog dialog = new AddAimAttemptDialog();
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
            ballsMade = savedInstanceState.getInt(PARAM_VALUE, 0);
            overCuts = savedInstanceState.getInt(PARAM_OVER_CUTS, 0);
            underCuts = savedInstanceState.getInt(PARAM_UNDER_CUTS, 0);
            english = savedInstanceState.getInt(PARAM_ENGLISH, 1);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PARAM_VALUE, makesPicker.getValue());
        outState.putInt(PARAM_OVER_CUTS, overCutsPicker.getValue());
        outState.putInt(PARAM_UNDER_CUTS, underCutsPicker.getValue());
        outState.putInt(PARAM_ENGLISH, englishPicker.getSelectedIndex());
    }

    @Override
    protected boolean hasTitle() {
        return true;
    }

    @Override
    protected void setDialogBuilderView(AlertDialog.Builder dialogBuilder) {
        View view = LayoutInflater.from(dialogBuilder.getContext())
                .inflate(R.layout.dialog_add_aim_attempt, null, false);
        ButterKnife.bind(this, view);
        englishPicker.setItems(getEnglishItems());
        englishPicker.setSelectedIndex(english);
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
        makesPicker.setValue(ballsMade);
        overCutsPicker.setValue(overCuts);
        underCutsPicker.setValue(underCuts);
        dialogBuilder.setView(view);
    }

    @Override
    protected String getTitle() {
        return getString(R.string.dialog_add_attempt_title);
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        presenter.addAttempt(
                getDrillId(),
                makesPicker.getValue(),
                overCutsPicker.getValue(),
                underCutsPicker.getValue(),
                getEnglish().ordinal(),
                obPositionsSpinner.getSelectedItemPosition() + 1,
                cbPositionsSpinner.getSelectedItemPosition() + 1
        );
    }

    @Override
    protected AddAimAttemptPresenter getPresenter() {
        return new AddAimAttemptPresenter();
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

    private English getEnglish() {
        switch (englishPicker.getSelectedIndex()) {
            case 0:
                return English.FULL_INSIDE;
            case 1:
                return English.INSIDE;
            case 2:
                return English.NONE;
            case 3:
                return English.OUTSIDE;
            case 4:
                return English.FULL_OUTSIDE;
            default:
                return English.ANY;
        }
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

    private List<HorizontalPicker.PickerItem> getEnglishItems() {
        List<HorizontalPicker.PickerItem> result = new ArrayList<>();
        result.add(new HorizontalPicker.TextItem("Full Inside"));
        result.add(new HorizontalPicker.TextItem("Inside"));
        result.add(new HorizontalPicker.TextItem("None"));
        result.add(new HorizontalPicker.TextItem("Outside"));
        result.add(new HorizontalPicker.TextItem("Full Outside"));

        return result;
    }
}
