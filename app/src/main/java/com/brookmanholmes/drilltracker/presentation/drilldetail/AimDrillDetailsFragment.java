package com.brookmanholmes.drilltracker.presentation.drilldetail;

import android.os.Bundle;
import android.widget.Spinner;
import android.widget.TextView;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.presentation.model.AimDrillModel;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;
import com.brookmanholmes.drilltracker.presentation.model.English;
import com.brookmanholmes.drilltracker.presentation.view.util.ChartUtil;

import java.text.DecimalFormat;
import java.util.EnumSet;

import butterknife.BindView;
import butterknife.OnItemSelected;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by Brookman Holmes on 7/11/2017.
 */

public class AimDrillDetailsFragment extends BaseDrillDetailsFragment {
    private static final String TAG = AimDrillDetailsFragment.class.getName();

    @BindView(R.id.lifetimeChart)
    LineChartView lifetimeChart;

    @BindView(R.id.spinner)
    Spinner englishSpinner;

    @BindView(R.id.sessionMakes)
    TextView sessionMakes;
    @BindView(R.id.sessionAttempts)
    TextView sessionAttempts;
    @BindView(R.id.sessionAvg)
    TextView sessionAvg;
    @BindView(R.id.sessionUnderCuts)
    TextView sessionUnderCuts;
    @BindView(R.id.sessionOverCuts)
    TextView sessionOverCuts;

    @BindView(R.id.lifetimeMakes)
    TextView lifetimeMakes;
    @BindView(R.id.lifetimeAttempts)
    TextView lifetimeAttempts;
    @BindView(R.id.lifetimeAvg)
    TextView lifetimeAvg;
    @BindView(R.id.lifetimeOverCuts)
    TextView lifetimeOverCuts;
    @BindView(R.id.lifetimeUnderCuts)
    TextView lifetimeUnderCuts;

    @BindView(R.id.targetScore)
    TextView targetScore;

    private DecimalFormat numberFormatter = new DecimalFormat("#.00");

    static AimDrillDetailsFragment forDrill(String drillId, String url, int maxValue, int targetValue, int obPositions, int cbPositions) {
        final AimDrillDetailsFragment fragment = new AimDrillDetailsFragment();
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

    /**
     * Implementation of {@link DrillDetailsView}
     */

    @Override
    public void renderDrill(DrillModel drill) {
        super.renderDrill(drill);
        if (drill != null) {
            DrillModel filteredDrillModel = new DrillModel(drill, getSelectedCbPosition(), getSelectedObPosition());
            AimDrillModel aimDrillModel = new AimDrillModel(filteredDrillModel, EnumSet.of(English.values()[englishSpinner.getSelectedItemPosition()]));

            ChartUtil.setupAimChart(lifetimeChart, aimDrillModel);

            sessionMakes.setText(getString(R.string.number, aimDrillModel.sessionMakes));
            sessionAttempts.setText(getString(R.string.number, aimDrillModel.sessionAttempts));
            sessionAvg.setText(getString(R.string.number_float, aimDrillModel.sessionAverage));
            sessionOverCuts.setText(getString(R.string.number, aimDrillModel.sessionOverCuts));
            sessionUnderCuts.setText(getString(R.string.number, aimDrillModel.sessionUnderCuts));

            lifetimeMakes.setText(getString(R.string.number, aimDrillModel.lifetimeMakes));
            lifetimeAttempts.setText(getString(R.string.number, aimDrillModel.lifetimeAttempts));
            lifetimeAvg.setText(getString(R.string.number_float, aimDrillModel.allTimeAverage));
            lifetimeOverCuts.setText(getString(R.string.number, aimDrillModel.lifetimeOverCuts));
            lifetimeUnderCuts.setText(getString(R.string.number, aimDrillModel.lifetimeUnderCuts));

            targetScore.setText(getString(R.string.target_score_with_number_float, numberFormatter.format(aimDrillModel.targetScore)));
        }
    }

    @OnItemSelected(R.id.spinner)
    void onItemSelected() {
        presenter.initialize(getDrillId());
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_aim_drill_details;
    }
}
