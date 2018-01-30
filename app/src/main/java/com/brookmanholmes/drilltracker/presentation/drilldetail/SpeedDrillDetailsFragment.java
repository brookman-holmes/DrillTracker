package com.brookmanholmes.drilltracker.presentation.drilldetail;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Spinner;

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
            ImageHandler.loadImage(image, drill.imageUrl);

            toolbar.setTitle(drill.name);
            description.setText(drill.description);

            ChartUtil.setupChart(sessionChart, new SpeedDrillModel(DrillModel.getSessionAttempts(filteredDrill.attemptModels)));
            ChartUtil.setupChart(lifetimeChart, new SpeedDrillModel(DrillModel.getAttemptsBetween(filteredDrill.attemptModels, getDateSelected(), new Date())));

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
