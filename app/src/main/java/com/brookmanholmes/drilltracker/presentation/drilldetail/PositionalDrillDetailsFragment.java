package com.brookmanholmes.drilltracker.presentation.drilldetail;

import android.os.Bundle;
import android.widget.Spinner;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;
import com.brookmanholmes.drilltracker.presentation.model.PositionalDrillModel;
import com.brookmanholmes.drilltracker.presentation.view.util.ChartUtil;

import java.util.Date;

import butterknife.BindView;
import butterknife.OnItemSelected;
import lecho.lib.hellocharts.view.PieChartView;

public class PositionalDrillDetailsFragment extends BaseDrillDetailsFragment {
    @BindView(R.id.distanceFromTargetChart)
    PieChartView targetDistanceSessionChart;
    @BindView(R.id.hSpinSessionChart)
    PieChartView hSpinSessionChart;
    @BindView(R.id.vSpinSessionChart)
    PieChartView vSpinSessionChart;
    @BindView(R.id.speedSessionChart)
    PieChartView speedSessionChart;

    @BindView(R.id.distanceFromTargetHistoryChart)
    PieChartView targetDistanceHistoryChart;
    @BindView(R.id.hSpinHistoryChart)
    PieChartView hSpinHistoryChart;
    @BindView(R.id.vSpinHistoryChart)
    PieChartView vSpinHistoryChart;
    @BindView(R.id.speedHistoryChart)
    PieChartView speedHistoryChart;

    @BindView(R.id.spinner)
    Spinner historySelectorSpinner;

    static PositionalDrillDetailsFragment forDrill(String drillId, String url, int cbPositions, int targetPositions) {
        final PositionalDrillDetailsFragment fragment = new PositionalDrillDetailsFragment();
        final Bundle args = new Bundle();
        args.putString(PARAM_DRILL_ID, drillId);
        args.putString(PARAM_URL, url);
        args.putInt(PARAM_CB_POSITIONS, cbPositions);
        args.putInt(PARAM_TARGET_POSITIONS, targetPositions);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_positional_drill_details;
    }

    @Override
    public void renderDrill(DrillModel drill) {
        super.renderDrill(drill);

        // TODO: 4/21/18 add in some stuff like average distance to target

        if (drill != null) {
            drill = new DrillModel(drill, getSelectedCbPosition(), getSelectedObPosition());

            ChartUtil.setupChart(
                    vSpinSessionChart,
                    hSpinSessionChart,
                    speedSessionChart,
                    targetDistanceSessionChart,
                    PositionalDrillModel.filterByTargetPosition(DrillModel.getSessionAttempts(drill.attemptModels), targetPositionsSpinner.getSelectedItemPosition())
            );
            ChartUtil.setupChart(
                    vSpinHistoryChart,
                    hSpinHistoryChart,
                    speedHistoryChart,
                    targetDistanceHistoryChart,
                    PositionalDrillModel.filterByTargetPosition(DrillModel.getAttemptsBetween(drill.attemptModels, getDateSelected(), new Date()), targetPositionsSpinner.getSelectedItemPosition())
            );
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
}
