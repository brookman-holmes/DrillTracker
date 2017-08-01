package com.brookmanholmes.drilltracker.presentation.drilldetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.presentation.base.BaseActivity;

import butterknife.BindView;

/**
 * Created by Brookman Holmes on 7/11/2017.
 */

public class DrillDetailsActivity extends BaseActivity {
    private static final String INTENT_EXTRA_PARAM_DRILL_ID = "com.brookmanholmes.INTENT_PARAM_DRILL_ID";
    private static final String INTENT_EXTRA_PARAM_DRILL_MAX = "com.brookmanholmes.INTENT_PARAM_DRILL_MAX";
    private static final String INTENT_EXTRA_PARAM_DRILL_TARGET = "com.brookmanholmes.INTENT_PARAM_DRILL_TARGET";
    private static final String INSTANCE_STATE_PARAM_DRILL_ID = "com.brookmanholmes.STATE_PARAM_DRILL_ID";
    private static final String INSTANCE_STATE_PARAM_DRILL_MAX = "com.brookmanholmes.STATE_DRILL_MAX";
    private static final String INSTANCE_STATE_PARAM_DRILL_TARGET = "com.brookmanholmes.TATE_DRILL_TARGET";

    public static Intent getIntent(Context context, String drillId, int maxValue, int targetValue) {
        Intent intent = new Intent(context, DrillDetailsActivity.class);
        intent.putExtra(INTENT_EXTRA_PARAM_DRILL_ID, drillId);
        intent.putExtra(INTENT_EXTRA_PARAM_DRILL_MAX, maxValue);
        intent.putExtra(INTENT_EXTRA_PARAM_DRILL_TARGET, targetValue);
        return intent;
    }

    private String drillId;
    private int maxValue, targetValue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drill_details);
        if (savedInstanceState == null) {
            drillId = getIntent().getStringExtra(INTENT_EXTRA_PARAM_DRILL_ID);
            maxValue = getIntent().getIntExtra(INTENT_EXTRA_PARAM_DRILL_MAX, -1);
            targetValue = getIntent().getIntExtra(INTENT_EXTRA_PARAM_DRILL_TARGET, -1);
            addFragment(R.id.fragmentContainer, DrillDetailsFragment.forDrill(drillId, maxValue, targetValue));
        } else {
            drillId = savedInstanceState.getString(INSTANCE_STATE_PARAM_DRILL_ID);
            maxValue = savedInstanceState.getInt(INSTANCE_STATE_PARAM_DRILL_MAX);
            targetValue = savedInstanceState.getInt(INSTANCE_STATE_PARAM_DRILL_TARGET);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (outState != null) {
            outState.putString(INSTANCE_STATE_PARAM_DRILL_ID, drillId);
            outState.putInt(INSTANCE_STATE_PARAM_DRILL_MAX, maxValue);
            outState.putInt(INSTANCE_STATE_PARAM_DRILL_TARGET, targetValue);
        }
        super.onSaveInstanceState(outState);
    }
}
