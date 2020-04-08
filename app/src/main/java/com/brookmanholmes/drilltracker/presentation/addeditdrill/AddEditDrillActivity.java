package com.brookmanholmes.drilltracker.presentation.addeditdrill;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.presentation.base.BaseActivity;

import java.util.Objects;

/**
 * Created by Brookman Holmes on 7/23/2017.
 */

public class AddEditDrillActivity extends BaseActivity {
    private static final String INTENT_EXTRA_PARAM_DRILL_ID = "param_drill_id";
    private static final String INSTANCE_STATE_PARAM_DRILL_ID = "com.brookmanholmes.STATE_PARAM_DRILL_ID";

    private String drillId;

    public static Intent newInstance(Context context, String drillId) {
        Intent intent = new Intent(context, AddEditDrillActivity.class);
        intent.putExtra(INTENT_EXTRA_PARAM_DRILL_ID, drillId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drill_list);
        if (savedInstanceState != null) {
            drillId = savedInstanceState.getString(INSTANCE_STATE_PARAM_DRILL_ID);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        if (outState != null) {
            outState.putString(INSTANCE_STATE_PARAM_DRILL_ID, drillId);
        }
        super.onSaveInstanceState(Objects.requireNonNull(outState));
    }

    @Override
    protected Fragment getFragment() {
        drillId = getIntent().getStringExtra(INTENT_EXTRA_PARAM_DRILL_ID);
        return AddEditDrillFragment.newInstance(drillId);
    }
}
