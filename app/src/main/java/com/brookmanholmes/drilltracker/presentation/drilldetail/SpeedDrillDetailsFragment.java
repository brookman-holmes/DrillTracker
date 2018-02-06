package com.brookmanholmes.drilltracker.presentation.drilldetail;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Spinner;
import android.widget.TextView;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.presentation.addattempt.AddSpeedAttemptDialog;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;
import com.brookmanholmes.drilltracker.presentation.model.SpeedDrillModel;
import com.brookmanholmes.drilltracker.presentation.view.util.ChartUtil;
import com.brookmanholmes.drilltracker.presentation.view.util.ImageHandler;

import java.util.Date;

import butterknife.BindView;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * Created by brookman on 1/25/18.
 */

public class SpeedDrillDetailsFragment extends BaseDrillDetailsFragment {
    @BindView(R.id.lifetimeChart)
    ColumnChartView lifetimeChart;
    @BindView(R.id.sessionChart)
    ColumnChartView sessionChart;

    @BindView(R.id.sessionMakes)
    TextView sessionMakes;
    @BindView(R.id.sessionAttempts)
    TextView sessionAttempts;
    @BindView(R.id.sessionAvg)
    TextView sessionAvg;
    @BindView(R.id.sessionError)
    TextView sessionError;
    @BindView(R.id.sessionSoft)
    TextView sessionSoft;
    @BindView(R.id.sessionHard)
    TextView sessionHard;

    @BindView(R.id.lifetimeMakes)
    TextView lifetimeMakes;
    @BindView(R.id.lifetimeAttempts)
    TextView lifetimeAttempts;
    @BindView(R.id.lifetimeAvg)
    TextView lifetimeAvg;
    @BindView(R.id.lifetimeError)
    TextView lifetimeError;
    @BindView(R.id.lifetimeSoft)
    TextView lifetimeSoft;
    @BindView(R.id.lifetimeHard)
    TextView lifetimeHard;

    @BindView(R.id.spinner)
    Spinner historySelectorSpinner;

    public static SpeedDrillDetailsFragment forDrill(String drillId, String url, int maxValue, int targetValue, int obPositions, int cbPositions) {
        final SpeedDrillDetailsFragment fragment = new SpeedDrillDetailsFragment();
        final Bundle args = new Bundle();
        args.putString(PARAM_DRILL_ID, drillId);
        args.putInt(PARAM_MAX, maxValue);
        args.putInt(PARAM_TARGET, targetValue);
        args.putString(PARAM_URL, url);
        args.putInt(PARAM_CB_POSITIONS, cbPositions);
        args.putInt(PARAM_OB_POSITIONS, obPositions);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void renderDrill(DrillModel drill) {
        if (drill != null) {
            setArguments(drill);
            DrillModel filteredDrill = new DrillModel(drill, getSelectedCbPosition(), getSelectedObPosition());
            SpeedDrillModel speedDrillModel = new SpeedDrillModel(filteredDrill.attemptModels);
            ImageHandler.loadImage(image, drill.imageUrl);

            toolbar.setTitle(drill.name);
            description.setText(drill.description);

            ChartUtil.setupChart(sessionChart, new SpeedDrillModel(DrillModel.getSessionAttempts(filteredDrill.attemptModels)));
            ChartUtil.setupChart(lifetimeChart, new SpeedDrillModel(DrillModel.getAttemptsBetween(filteredDrill.attemptModels, getDateSelected(), new Date())));

            sessionAttempts.setText(getString(R.string.number, speedDrillModel.sessionAttempts));
            sessionMakes.setText(getString(R.string.number, speedDrillModel.sessionCorrect));
            sessionSoft.setText(getString(R.string.number, speedDrillModel.sessionSoft));
            sessionHard.setText(getString(R.string.number, speedDrillModel.sessionHard));
            sessionAvg.setText(getString(R.string.number_float, speedDrillModel.sessionSuccessRate));
            sessionError.setText(getString(R.string.number_float, speedDrillModel.sessionAvgError));

            lifetimeAttempts.setText(getString(R.string.number, speedDrillModel.lifetimeAttempts));
            lifetimeMakes.setText(getString(R.string.number, speedDrillModel.lifetimeCorrect));
            lifetimeSoft.setText(getString(R.string.number, speedDrillModel.lifetimeSoft));
            lifetimeHard.setText(getString(R.string.number, speedDrillModel.lifetimeHard));
            lifetimeAvg.setText(getString(R.string.number_float, speedDrillModel.lifetimeSuccessRate));
            lifetimeError.setText(getString(R.string.number_float, speedDrillModel.lifetimeAvgError));


            setMenuIconEnabled(R.id.ic_edit, !drill.purchased);
            setMenuIconEnabled(R.id.ic_undo_attempt, drill.attemptModels.size() > 0);
        }
    }

    private Date getDateSelected() {
        int selectedItem = historySelectorSpinner.getSelectedItemPosition();

        if (selectedItem == 0) {
            return DrillModel.Dates.ALL_TIME;
        } else if (selectedItem == 1) {
            return DrillModel.Dates.LAST_SIX_MONTHS;
        } else if (selectedItem == 2) {
            return DrillModel.Dates.LAST_THREE_MONTHS;
        } else if (selectedItem == 3) {
            return DrillModel.Dates.LAST_MONTH;
        } else {
            return DrillModel.Dates.LAST_WEEK;
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_speed_drill_details;
    }

    @Override
    protected DialogFragment getAddAttemptDialog() {
        return AddSpeedAttemptDialog.newInstance(
                getDrillId(),
                getCbPositions(),
                getObPositions(),
                getSelectedCbPosition(),
                getSelectedObPosition()
        );
    }
}
