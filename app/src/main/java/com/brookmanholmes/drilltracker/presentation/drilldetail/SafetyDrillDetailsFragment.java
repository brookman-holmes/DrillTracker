package com.brookmanholmes.drilltracker.presentation.drilldetail;

import android.os.Bundle;
import android.widget.Spinner;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;
import com.brookmanholmes.drilltracker.presentation.model.SafetyDrillModel;
import com.brookmanholmes.drilltracker.presentation.view.util.ChartUtil;

import java.util.Date;

import butterknife.BindView;
import butterknife.OnItemSelected;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * Created by Brookman Holmes on 7/11/2017.
 */

public class SafetyDrillDetailsFragment extends BaseDrillDetailsFragment {
    private static final String TAG = SafetyDrillDetailsFragment.class.getName();

    @BindView(R.id.speedSessionChart)
    PieChartView speedChart;
    @BindView(R.id.spinSessionChart)
    PieChartView spinChart;
    @BindView(R.id.thicknessSessionChart)
    PieChartView thicknessChart;

    @BindView(R.id.spinner)
    Spinner historySelectorSpinner;

    @BindView(R.id.speedHistoryChart)
    PieChartView speedHistoryChart;
    @BindView(R.id.spinHistoryChart)
    PieChartView spinHistoryChart;
    @BindView(R.id.thicknessHistoryChart)
    PieChartView thicknessHistoryChart;

    static SafetyDrillDetailsFragment forDrill(String drillId, String url, int cbPositions, int obPositions) {
        final SafetyDrillDetailsFragment fragment = new SafetyDrillDetailsFragment();
        final Bundle args = new Bundle();
        args.putString(PARAM_DRILL_ID, drillId);
        args.putString(PARAM_URL, url);
        args.putInt(PARAM_CB_POSITIONS, cbPositions);
        args.putInt(PARAM_OB_POSITIONS, obPositions);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void renderDrill(DrillModel drill) {
        super.renderDrill(drill);

        if (drill != null) {
            drill = new DrillModel(drill, getSelectedCbPosition(), getSelectedObPosition());

            ChartUtil.setupChart(speedChart, thicknessChart, spinChart, new SafetyDrillModel(DrillModel.getSessionAttempts(drill.attemptModels)));
            ChartUtil.setupChart(speedHistoryChart, thicknessHistoryChart, spinHistoryChart, new SafetyDrillModel(DrillModel.getAttemptsBetween(drill.attemptModels, getDateSelected(), new Date())));
        }
    }

    @OnItemSelected(R.id.spinner)
    void onItemSelected() {
        presenter.initialize(getDrillId());
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
        return R.layout.fragment_safety_drill_details;
    }
}
