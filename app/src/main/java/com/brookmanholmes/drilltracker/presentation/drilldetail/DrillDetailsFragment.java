package com.brookmanholmes.drilltracker.presentation.drilldetail;

import android.os.Bundle;
import android.widget.TextView;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;
import com.brookmanholmes.drilltracker.presentation.model.DrillModelMathUtil;
import com.brookmanholmes.drilltracker.presentation.view.util.ChartUtil;

import butterknife.BindView;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by Brookman Holmes on 7/11/2017.
 */

public class DrillDetailsFragment extends BaseDrillDetailsFragment {
    @BindView(R.id.lifetimeChart)
    LineChartView lifetimeChart;
    @BindView(R.id.sessionChart)
    LineChartView sessionChart;

    @BindView(R.id.sessionHigh)
    TextView sessionHigh;
    @BindView(R.id.sessionAttempts)
    TextView sessionAttempts;
    @BindView(R.id.sessionAvg)
    TextView sessionAvg;
    @BindView(R.id.sessionMedian)
    TextView sessionMedian;

    @BindView(R.id.lifetimeHigh)
    TextView lifetimeHigh;
    @BindView(R.id.lifetimeAttempts)
    TextView lifetimeAttempts;
    @BindView(R.id.lifetimeAvg)
    TextView lifetimeAvg;
    @BindView(R.id.lifetimeMedian)
    TextView lifetimeMedian;

    @BindView(R.id.maxScoreAttainable)
    TextView maxScoreAttainable;
    @BindView(R.id.targetScore)
    TextView targetScore;

    static DrillDetailsFragment forDrill(String drillId, String url, int maxValue, int targetValue, int obPositions, int cbPositions, int targetPositions) {
        final DrillDetailsFragment fragment = new DrillDetailsFragment();
        final Bundle args = new Bundle();
        args.putString(PARAM_DRILL_ID, drillId);
        args.putInt(PARAM_MAX, maxValue);
        args.putInt(PARAM_TARGET, targetValue);
        args.putString(PARAM_URL, url);
        args.putInt(PARAM_CB_POSITIONS, cbPositions);
        args.putInt(PARAM_OB_POSITIONS, obPositions);
        args.putInt(PARAM_TARGET_POSITIONS, targetPositions);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void renderDrill(DrillModel drill) {
        super.renderDrill(drill);

        if (drill != null) {
            drill = new DrillModel(drill, getSelectedCbPosition(), getSelectedObPosition());

            DrillModelMathUtil lifetimeStats = new DrillModelMathUtil(drill.attemptModels);
            DrillModelMathUtil sessionStats = new DrillModelMathUtil(DrillModel.getSessionAttempts(drill.attemptModels));

            ChartUtil.setupLifetimeChart(lifetimeChart, drill);
            ChartUtil.setupChart(sessionChart, drill);

            sessionHigh.setText(getString(R.string.number, sessionStats.getMax()));
            sessionAttempts.setText(getString(R.string.number, sessionStats.getAttempts()));
            sessionAvg.setText(getString(R.string.number_float, sessionStats.getAverage()));
            sessionMedian.setText(getString(R.string.number_float, sessionStats.getMedian()));

            lifetimeHigh.setText(getString(R.string.number, lifetimeStats.getMax()));
            lifetimeAttempts.setText(getString(R.string.number, lifetimeStats.getAttempts()));
            lifetimeAvg.setText(getString(R.string.number_float, lifetimeStats.getAverage()));
            lifetimeMedian.setText(getString(R.string.number_float, lifetimeStats.getMedian()));

            maxScoreAttainable.setText(getString(R.string.max_score_attainable, drill.maxScore));
            targetScore.setText(getString(R.string.target_score_with_number, drill.defaultTargetScore));
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_drill_details;
    }
}
