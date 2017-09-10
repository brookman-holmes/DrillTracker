package com.brookmanholmes.drilltracker.presentation.drilldetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.presentation.base.BaseActivity;

/**
 * Created by Brookman Holmes on 7/11/2017.
 */

public class DrillDetailsActivity extends BaseActivity {
    private static final String INTENT_EXTRA_PARAM_DRILL_ID = "com.brookmanholmes.INTENT_PARAM_DRILL_ID";
    private static final String INTENT_EXTRA_PARAM_DRILL_MAX = "com.brookmanholmes.INTENT_PARAM_DRILL_MAX";
    private static final String INTENT_EXTRA_PARAM_DRILL_TARGET = "com.brookmanholmes.INTENT_PARAM_DRILL_TARGET";
    private static final String INTENT_EXTRA_PARAM_DRILL_URL = "com.brookmanholmes.INTENT_PARAM_DRILL_URL";
    private static final String INSTANCE_STATE_PARAM_DRILL_ID = "com.brookmanholmes.STATE_PARAM_DRILL_ID";
    private static final String INSTANCE_STATE_PARAM_DRILL_MAX = "com.brookmanholmes.STATE_DRILL_MAX";
    private static final String INSTANCE_STATE_PARAM_DRILL_TARGET = "com.brookmanholmes.TATE_DRILL_TARGET";
    private static final String INSTANCE_STATE_PARAM_DRILL_URL = "com.brookmanholmes.TATE_DRILL_URL";
    private String drillId, imageUrl;
    private int maxValue, targetValue;

    public static Intent getIntent(Context context, String drillId, String url, int maxValue, int targetValue) {
        Intent intent = new Intent(context, DrillDetailsActivity.class);
        intent.putExtra(INTENT_EXTRA_PARAM_DRILL_ID, drillId);
        intent.putExtra(INTENT_EXTRA_PARAM_DRILL_MAX, maxValue);
        intent.putExtra(INTENT_EXTRA_PARAM_DRILL_TARGET, targetValue);
        intent.putExtra(INTENT_EXTRA_PARAM_DRILL_URL, url);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drill_details);
        if (savedInstanceState != null) {
            drillId = savedInstanceState.getString(INSTANCE_STATE_PARAM_DRILL_ID);
            imageUrl = savedInstanceState.getString(INSTANCE_STATE_PARAM_DRILL_URL);
            maxValue = savedInstanceState.getInt(INSTANCE_STATE_PARAM_DRILL_MAX);
            targetValue = savedInstanceState.getInt(INSTANCE_STATE_PARAM_DRILL_TARGET);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (outState != null) {
            outState.putString(INSTANCE_STATE_PARAM_DRILL_ID, drillId);
            outState.putString(INSTANCE_STATE_PARAM_DRILL_URL, imageUrl);
            outState.putInt(INSTANCE_STATE_PARAM_DRILL_MAX, maxValue);
            outState.putInt(INSTANCE_STATE_PARAM_DRILL_TARGET, targetValue);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected Fragment getFragment() {
        drillId = getIntent().getStringExtra(INTENT_EXTRA_PARAM_DRILL_ID);
        imageUrl = getIntent().getStringExtra(INTENT_EXTRA_PARAM_DRILL_URL);
        maxValue = getIntent().getIntExtra(INTENT_EXTRA_PARAM_DRILL_MAX, -1);
        targetValue = getIntent().getIntExtra(INTENT_EXTRA_PARAM_DRILL_TARGET, -1);

        return DrillDetailsFragment.forDrill(drillId, imageUrl, maxValue, targetValue);
    }
}
