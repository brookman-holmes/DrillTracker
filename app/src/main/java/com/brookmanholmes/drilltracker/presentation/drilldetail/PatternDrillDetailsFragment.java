package com.brookmanholmes.drilltracker.presentation.drilldetail;

import android.os.Bundle;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;
import com.brookmanholmes.drilltracker.presentation.model.PatternDrillModel;
import com.brookmanholmes.drilltracker.presentation.view.PatternSpinnerAdapter;
import com.brookmanholmes.drilltracker.presentation.view.util.ChartUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;

public class PatternDrillDetailsFragment extends BaseDrillDetailsFragment implements AddPatternView {
    private final List<List<Integer>> patterns = new ArrayList<>();
    private final DecimalFormat avgf = new DecimalFormat("##.##");
    private final AddPatternContract patternPresenter = new AddPatternPresenter(this);
    @BindView(R.id.transitionRatingsChart)
    ColumnChartView transitionRatingsChart;
    @BindView(R.id.averageTransitionRatingsChart)
    ColumnChartView averageTransitionRatingsChart;
    @BindView(R.id.chart)
    LineChartView shootingHistoryChart;
    @BindView(R.id.patternSpinner)
    Spinner patternSpinner;
    @BindView(R.id.thisPatternAttempts)
    TextView thisPatternAttempts;
    @BindView(R.id.thisPatternCompPct)
    TextView thisPatternCompPct;
    @BindView(R.id.thisPatternRunLength)
    TextView thisPatternRunLength;
    @BindView(R.id.allPatternAttempts)
    TextView allPatternAttempts;
    @BindView(R.id.allPatternCompPct)
    TextView allPatternCompPct;
    @BindView(R.id.allPatternRunLength)
    TextView allPatternRunLength;
    @BindView(R.id.missFrequencyChart)
    ColumnChartView missFrequencyChart;
    private PatternSpinnerAdapter patternSpinnerAdapter;

    static PatternDrillDetailsFragment forDrill(String drillId, String url, int maxScore) {
        final PatternDrillDetailsFragment fragment = new PatternDrillDetailsFragment();
        final Bundle args = new Bundle();
        args.putString(PARAM_DRILL_ID, drillId);
        args.putString(PARAM_URL, url);
        args.putInt(PARAM_MAX, maxScore);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        patternSpinnerAdapter = new PatternSpinnerAdapter(Objects.requireNonNull(getContext()), R.layout.view_dropdown_pattern_layout, new ArrayList<>());
    }

    @Override
    public void onResume() {
        super.onResume();
        patternPresenter.resume();
        patternSpinner.setAdapter(patternSpinnerAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        patternPresenter.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        patternPresenter.destroy();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_rotation_pattern_drill_details;
    }

    @OnItemSelected(R.id.patternSpinner)
    void onItemSelected() {
        presenter.initialize(getDrillId());
    }

    @OnClick(R.id.createNewPattern)
    void createNewPattern() {
        patternPresenter.onNewPatternClicked(getDrillId());
    }

    @Override
    public void renderDrill(DrillModel drill) {
        super.renderDrill(drill);

        PatternDrillModel patternDrillModel = new PatternDrillModel(drill, getSelectedPattern());
        ChartUtil.setupChart(transitionRatingsChart, patternDrillModel, getSelectedPattern());
        ChartUtil.setupAveragesChart(averageTransitionRatingsChart, patternDrillModel, getSelectedPattern());
        ChartUtil.setupLifetimePatternChart(shootingHistoryChart, drill);
        ChartUtil.setupMissFrequencyChart(missFrequencyChart, patternDrillModel, getSelectedPattern());

        thisPatternAttempts.setText(getString(R.string.number, patternDrillModel.thisPatternAttempts));
        thisPatternCompPct.setText(pctf.format(patternDrillModel.thisPatternCompPct));
        thisPatternRunLength.setText(avgf.format(patternDrillModel.thisPatternRunLength));

        allPatternAttempts.setText(getString(R.string.number, patternDrillModel.allPatternAttempts));
        allPatternCompPct.setText(pctf.format(patternDrillModel.allPatternCompPct));
        allPatternRunLength.setText(avgf.format(patternDrillModel.allPatternRunLength));
    }

    @Override
    List<Integer> getSelectedPattern() {
        if (patternSpinner.getSelectedItem() != null)
            return (List<Integer>) patternSpinner.getSelectedItem();
        else
            return new ArrayList<>();
    }

    @Override
    protected void setArguments(DrillModel model) {
        super.setArguments(model);
        if (!patterns.equals(model.patterns)) {
            this.patterns.clear();
            patterns.addAll(model.patterns);
            patternSpinnerAdapter.clear();
            patternSpinnerAdapter.addAll(model.patterns);
            patternSpinnerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showNewPatternDialog(String drillId) {
        AddPatternDialog.newInstance(drillId).show(Objects.requireNonNull(getFragmentManager()), "AddPatternDialog");
    }
}
