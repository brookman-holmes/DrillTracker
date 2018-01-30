package com.brookmanholmes.drilltracker.presentation.drilldetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.presentation.base.BaseActivity;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;

/**
 * Created by Brookman Holmes on 7/11/2017.
 */

public class DrillDetailsActivity extends BaseActivity {
    private static final String INTENT_EXTRA_PARAM_DRILL_ID = "com.brookmanholmes.INTENT_PARAM_DRILL_ID";
    private static final String INTENT_EXTRA_PARAM_DRILL_MAX = "com.brookmanholmes.INTENT_PARAM_DRILL_MAX";
    private static final String INTENT_EXTRA_PARAM_DRILL_TARGET = "com.brookmanholmes.INTENT_PARAM_DRILL_TARGET";
    private static final String INTENT_EXTRA_PARAM_DRILL_URL = "com.brookmanholmes.INTENT_PARAM_DRILL_URL";
    private static final String INTENT_EXTRA_PARAM_DRILL_TYPE = "com.brookmanholmes.INTENT_PARAM_DRILL_TYPE";
    private static final String INTENT_EXTRA_PARAM_DRILL_OB_POS = "com.brookmanholmes.INTENT_PARAM_DRILL_OB_POS";
    private static final String INTENT_EXTRA_PARAM_DRILL_CB_POS = "com.brookmanholmes.INTENT_PARAM_DRILL_CB_POS";

    private static final String INSTANCE_STATE_PARAM_DRILL_ID = "com.brookmanholmes.STATE_PARAM_DRILL_ID";
    private static final String INSTANCE_STATE_PARAM_DRILL_MAX = "com.brookmanholmes.STATE_DRILL_MAX";
    private static final String INSTANCE_STATE_PARAM_DRILL_TARGET = "com.brookmanholmes.STATE_DRILL_TARGET";
    private static final String INSTANCE_STATE_PARAM_DRILL_URL = "com.brookmanholmes.STATE_DRILL_URL";
    private static final String INSTANCE_STATE_PARAM_DRILL_TYPE = "com.brookmanholmes.STATE_PARAM_DRILL_TYPE";
    private static final String INSTANCE_STATE_PARAM_DRILL_CB_POS = "com.brookmanholmes.STATE_DRILL_CB_POS";
    private static final String INSTANCE_STATE_PARAM_DRILL_OB_POS = "com.brookmanholmes.STATE_PARAM_DRILL_OB_POS";
    private String drillId, imageUrl;
    private int maxValue, targetValue, obPositions, cbPositions;
    private DrillModel.Type type;

    public static Intent getIntent(Context context, String drillId, DrillModel.Type type, String url, int maxValue, int targetValue, int obPositions, int cbPositions) {
        Intent intent = new Intent(context, DrillDetailsActivity.class);
        updateExtras(intent, drillId, type, url, maxValue, targetValue, obPositions, cbPositions);
        return intent;
    }

    static void updateExtras(Intent intent, String drillId, DrillModel.Type type, String url, int maxValue, int targetValue, int obPositions, int cbPositions) {
        intent.putExtra(INTENT_EXTRA_PARAM_DRILL_ID, drillId);
        intent.putExtra(INTENT_EXTRA_PARAM_DRILL_MAX, maxValue);
        intent.putExtra(INTENT_EXTRA_PARAM_DRILL_TARGET, targetValue);
        intent.putExtra(INTENT_EXTRA_PARAM_DRILL_URL, url);
        intent.putExtra(INTENT_EXTRA_PARAM_DRILL_TYPE, type);
        intent.putExtra(INTENT_EXTRA_PARAM_DRILL_CB_POS, cbPositions);
        intent.putExtra(INTENT_EXTRA_PARAM_DRILL_OB_POS, obPositions);
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
            obPositions = savedInstanceState.getInt(INSTANCE_STATE_PARAM_DRILL_OB_POS);
            cbPositions = savedInstanceState.getInt(INSTANCE_STATE_PARAM_DRILL_CB_POS);
            type = (DrillModel.Type) savedInstanceState.getSerializable(INSTANCE_STATE_PARAM_DRILL_TYPE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (outState != null) {
            outState.putString(INSTANCE_STATE_PARAM_DRILL_ID, drillId);
            outState.putString(INSTANCE_STATE_PARAM_DRILL_URL, imageUrl);
            outState.putInt(INSTANCE_STATE_PARAM_DRILL_MAX, maxValue);
            outState.putInt(INSTANCE_STATE_PARAM_DRILL_TARGET, targetValue);
            outState.putSerializable(INSTANCE_STATE_PARAM_DRILL_TYPE, type);
            outState.putInt(INSTANCE_STATE_PARAM_DRILL_CB_POS, obPositions);
            outState.putInt(INSTANCE_STATE_PARAM_DRILL_CB_POS, cbPositions);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected Fragment getFragment() {
        drillId = getIntent().getStringExtra(INTENT_EXTRA_PARAM_DRILL_ID);
        imageUrl = getIntent().getStringExtra(INTENT_EXTRA_PARAM_DRILL_URL);
        maxValue = getIntent().getIntExtra(INTENT_EXTRA_PARAM_DRILL_MAX, -1);
        targetValue = getIntent().getIntExtra(INTENT_EXTRA_PARAM_DRILL_TARGET, -1);
        cbPositions = getIntent().getIntExtra(INTENT_EXTRA_PARAM_DRILL_CB_POS, -1);
        obPositions = getIntent().getIntExtra(INTENT_EXTRA_PARAM_DRILL_OB_POS, -1);
        type = (DrillModel.Type) getIntent().getSerializableExtra(INTENT_EXTRA_PARAM_DRILL_TYPE);

        if (type == DrillModel.Type.SAFETY) {
            return SafetyDrillDetailsFragment.forDrill(drillId, imageUrl, obPositions, cbPositions);
        } else if (type == DrillModel.Type.AIMING || type == DrillModel.Type.KICKING || type == DrillModel.Type.BANKING) {
            return AimDrillDetailsFragment.forDrill(drillId, imageUrl, maxValue, targetValue, obPositions, cbPositions);
        } else if (type == DrillModel.Type.SPEED) {
            return SpeedDrillDetailsFragment.forDrill(drillId, imageUrl, maxValue, targetValue, obPositions, cbPositions);
        } else {
            return DrillDetailsFragment.forDrill(drillId, imageUrl, maxValue, targetValue, obPositions, cbPositions);
        }
    }
}
