package com.brookmanholmes.drilltracker.presentation.addattempt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Spinner;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.presentation.adapters.SpinnerAdapterHelper;
import com.brookmanholmes.drilltracker.presentation.base.BaseDialogFragment;
import com.brookmanholmes.drilltracker.presentation.base.Presenter;
import com.brookmanholmes.drilltracker.presentation.model.DistanceResult;
import com.brookmanholmes.drilltracker.presentation.model.English;
import com.brookmanholmes.drilltracker.presentation.model.ShotResult;
import com.brookmanholmes.drilltracker.presentation.model.Speed;
import com.brookmanholmes.drilltracker.presentation.model.SpeedResult;
import com.brookmanholmes.drilltracker.presentation.model.SpinResult;
import com.goodiebag.horizontalpicker.HorizontalPicker;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseAddAttemptDialog<T extends Presenter> extends BaseDialogFragment<T> {
    static final String PARAM_DRILL_ID = "param_drill_id";
    static final String PARAM_MAX = "param_max";
    static final String PARAM_TARGET = "param_target";
    static final String PARAM_VALUE = "param_value";
    static final String PARAM_CB_POS = "param_cb_pos";
    static final String PARAM_OB_POS = "param_ob_pos";
    static final String PARAM_SELECTED_CB_POS = "param_select_cb_pos";
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
    static final String PARAM_SELECTED_TARGET_POS = "param_selected_target_pos";
    static final String PARAM_SHOT_RESULT_VALUE = "param_shot_result_value";
    static final String PARAM_ENGLISH_VALUE = "param_english_value";
    static final String PARAM_SELECTED_ENGLISH = "param_selected_english";
    static final String PARAM_SPEED_RESULT_VALUE = "param_speed_result_value";

    @BindView(R.id.obPositionsSpinner)
    Spinner obPositionsSpinner;
    @BindView(R.id.cbPositionsSpinner)
    Spinner cbPositionsSpinner;
    @BindView(R.id.targetPositionsSpinner)
    Spinner targetPositionsSpinner;

    // default values
    ShotResult shotResult = ShotResult.MAKE_CENTER;
    SpinResult speedSelection = SpinResult.CORRECT;
    SpinResult vSpinSelection = SpinResult.CORRECT;
    SpinResult hSpinSelection = SpinResult.CORRECT;
    DistanceResult targetDistanceSelection = DistanceResult.ZERO;
    Speed speedUsedSelection = Speed.THREE;
    SpeedResult speedResultSelection = SpeedResult.CORRECT;


    // values set via fragment arguments
    English englishSelection;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            // set saved values
            shotResult = (ShotResult) savedInstanceState.getSerializable(PARAM_SHOT_RESULT_VALUE);
            vSpinSelection = (SpinResult) savedInstanceState.getSerializable(PARAM_SPIN_VALUE);
            speedSelection = (SpinResult) savedInstanceState.getSerializable(PARAM_SPEED_VALUE);
            hSpinSelection = (SpinResult) savedInstanceState.getSerializable(PARAM_THICKNESS_VALUE);
            targetDistanceSelection = (DistanceResult) savedInstanceState.getSerializable(PARAM_TARGET_DISTANCE);
            englishSelection = (English) savedInstanceState.getSerializable(PARAM_ENGLISH_VALUE);
            speedResultSelection = (SpeedResult) savedInstanceState.getSerializable(PARAM_SPEED_RESULT_VALUE);
        } else {
            // set values that should be populated via fragment arguments
            englishSelection = getDefaultSelectedEnglish();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // save values
        outState.putSerializable(PARAM_SHOT_RESULT_VALUE, getShotResult());
        outState.putSerializable(PARAM_SPEED_VALUE, getSpeedSpinValue());
        outState.putSerializable(PARAM_SPIN_VALUE, getVSpinValue());
        outState.putSerializable(PARAM_THICKNESS_VALUE, getHSpinValue());
        outState.putSerializable(PARAM_TARGET_DISTANCE, getTargetDistanceValue());
        outState.putSerializable(PARAM_ENGLISH_VALUE, getEnglishValue());
        outState.putSerializable(PARAM_SPEED_RESULT_VALUE, getSpeedResultValue());
    }

    @Override
    @CallSuper
    protected void setDialogBuilderView(AlertDialog.Builder dialogBuilder) {
        View view = LayoutInflater.from(dialogBuilder.getContext())
                .inflate(getDialogId(), null, false);
        ButterKnife.bind(this, view);
        dialogBuilder.setView(view);

        targetPositionsSpinner.setAdapter(SpinnerAdapterHelper.createNumberedListAdapter(getContext(), 1, getTargetPositions() + 1, "Target "));
        cbPositionsSpinner.setAdapter(SpinnerAdapterHelper.createNumberedListAdapter(getContext(), 1, getCueBallPositions() + 1, "CB Position "));
        obPositionsSpinner.setAdapter(SpinnerAdapterHelper.createNumberedListAdapter(getContext(), 1, getObjectBallPositions() + 1, "OB Position "));

        targetPositionsSpinner.setSelection(getDefaultSelectedTargetPosition());
        cbPositionsSpinner.setSelection(getDefaultSelectedCbBallPosition());
        obPositionsSpinner.setSelection(getDefaultSelectedObBallPosition());

        if (!showCueBallSpinner()) {
            cbPositionsSpinner.setVisibility(View.GONE);
        }
        if (!showTargetPositionsSpinner()) {
            targetPositionsSpinner.setVisibility(View.GONE);
        }

        if (!showObjectBallSpinner()) {
            obPositionsSpinner.setVisibility(View.GONE);
        }
    }

    @Override
    protected boolean hasTitle() {
        return true;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.dialog_add_attempt_title);
    }

    @LayoutRes
    abstract int getDialogId();

    abstract ShotResult getShotResult();

    abstract SpinResult getSpeedSpinValue();

    abstract SpinResult getHSpinValue();

    abstract English getEnglishValue();

    abstract DistanceResult getTargetDistanceValue();

    abstract SpinResult getVSpinValue();

    abstract Speed getSpeedUsed();

    abstract SpeedResult getSpeedResultValue();

    String getDrillId() {
        return requireArguments().getString(PARAM_DRILL_ID);
    }

    private int getCueBallPositions() {
        return requireArguments().getInt(PARAM_CB_POS, 1);
    }

    private int getObjectBallPositions() {
        return requireArguments().getInt(PARAM_OB_POS, 1);
    }

    private int getTargetPositions() {
        return requireArguments().getInt(PARAM_TARGET_POS, 1);
    }

    private int getDefaultSelectedCbBallPosition() {
        int selection = requireArguments().getInt(PARAM_SELECTED_CB_POS, 0);
        if (selection > 0)
            return selection - 1;
        else return selection;
    }

    private int getDefaultSelectedObBallPosition() {
        int selection = requireArguments().getInt(PARAM_SELECTED_OB_POS, 0);
        if (selection > 0)
            return selection - 1;
        else return selection;
    }

    private int getDefaultSelectedTargetPosition() {
        int selection = requireArguments().getInt(PARAM_SELECTED_TARGET_POS, 0);
        if (selection > 0)
            return selection - 1;
        else return selection;
    }

    private English getDefaultSelectedEnglish() {
        English result = (English) requireArguments().getSerializable(PARAM_SELECTED_ENGLISH);
        if (result.equals(English.ANY))
            return English.NONE;
        else return result;
    }

    int getCueBallPositionSelection() {
        return cbPositionsSpinner.getSelectedItemPosition() + 1;
    }

    int getObjectBallPositionSelection() {
        return obPositionsSpinner.getSelectedItemPosition() + 1;
    }

    int getTargetPositionSelection() {
        return targetPositionsSpinner.getSelectedItemPosition() + 1;
    }

    List<HorizontalPicker.PickerItem> createPickerItems(String... items) {
        List<HorizontalPicker.PickerItem> result = new ArrayList<>();
        for (String item : items) {
            result.add(new HorizontalPicker.TextItem(item));
        }

        return result;
    }

    List<HorizontalPicker.PickerItem> getShotResultItems() {
        return createPickerItems("Miss over cut", "Make over cut", "Make center pocket", "Make under cut", "Miss under cut");
    }

    List<HorizontalPicker.PickerItem> getSpinItems() {
        return createPickerItems("Too little", "Correct", "Too much");
    }

    List<HorizontalPicker.PickerItem> getSpeedItems() {
        return createPickerItems("Too soft", "Correct", "Too hard");
    }

    List<HorizontalPicker.PickerItem> getEnglishItems() {
        return createPickerItems("Full inside", "Inside", "None", "Outside", "Full outside");
    }

    List<HorizontalPicker.PickerItem> getSpeedUsedItems() {
        return createPickerItems("1", "2", "3", "4", "5");
    }

    List<HorizontalPicker.PickerItem> getTargetDistanceItems() {
        return createPickerItems("In target", "< 6\"", "< 12\"", "< 18\"", "18+\"");
    }

    List<HorizontalPicker.PickerItem> getThicknessItems() {
        return createPickerItems("Too thin", "Correct", "Too thick");
    }

    private boolean showCueBallSpinner() {
        return getCueBallPositions() > 1;
    }

    private boolean showObjectBallSpinner() {
        return getObjectBallPositions() > 1;
    }

    private boolean showTargetPositionsSpinner() {
        return getTargetPositions() > 1;
    }

    int getSpeedValueSelection(SpinResult spinResult) {
        return getSpinResultSelection(spinResult);
    }

    int getTargetDistanceSelection(DistanceResult distanceResult) {
        switch (distanceResult) {
            case ONE_HALF:
                return 1;
            case ONE:
                return 2;
            case ONE_AND_HALF:
                return 3;
            case OVER_ONE_AND_HALF:
                return 4;
            default:
                return 0;
        }
    }

    int getEnglishSelection(English english) {
        switch (english) {
            case FULL_INSIDE:
                return 0;
            case INSIDE:
                return 1;
            case OUTSIDE:
                return 3;
            case FULL_OUTSIDE:
                return 4;
            default:
                return 2;
        }
    }

    int getShotResultSelection(ShotResult shotResult) {
        switch (shotResult) {
            case MISS_OVER_CUT:
                return 0;
            case MAKE_OVER_CUT:
                return 1;
            case MAKE_UNDER_CUT:
                return 3;
            case MISS_UNDER_CUT:
                return 4;
            default:
                return 2;
        }
    }

    int getSpinResultSelection(SpinResult spinResult) {
        switch (spinResult) {
            case TOO_LITTLE:
                return 0;
            case TOO_MUCH:
                return 2;
            default:
                return 1;
        }
    }

    int getSpeedUsedSelection(Speed speedUsed) {
        switch (speedUsed) {
            case ONE:
                return 0;
            case TWO:
                return 1;
            case THREE:
                return 2;
            case FOUR:
                return 3;
            case FIVE:
                return 4;
            default:
                return 2;
        }
    }

    int getSpeedResultSelection(SpeedResult speedResult) {
        switch (speedResult) {
            case TWO_DIAMOND_SOFT:
                return 0;
            case DIAMOND_SOFT:
                return 1;
            case HALF_DIAMOND_SOFT:
                return 2;
            case QUARTER_DIAMOND_SOFT:
                return 3;
            case CORRECT:
                return 4;
            case QUARTER_DIAMOND_HARD:
                return 5;
            case HALF_DIAMOND_HARD:
                return 6;
            case DIAMOND_HARD:
                return 7;
            case TWO_DIAMOND_HARD:
                return 8;
            default:
                return 4;
        }
    }
}
