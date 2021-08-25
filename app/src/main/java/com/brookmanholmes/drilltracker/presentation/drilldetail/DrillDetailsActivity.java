package com.brookmanholmes.drilltracker.presentation.drilldetail;

import static com.brookmanholmes.drilltracker.presentation.addattempt.AddAttemptDialog.PARAM_DRILL_ID;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.presentation.base.BaseActivity;

import java.util.Objects;

import timber.log.Timber;

/**
 * Created by Brookman Holmes on 7/11/2017.
 */

public class DrillDetailsActivity extends BaseActivity {
    private static final String INTENT_EXTRA_PARAM_DRILL_ID = "com.brookmanholmes.INTENT_PARAM_DRILL_ID";
    private static final String INSTANCE_STATE_PARAM_DRILL_ID = "com.brookmanholmes.STATE_PARAM_DRILL_ID";

    private String drillId;

    public static Intent getIntent(Context context, String drillId) {
        Timber.i(drillId);
        Intent intent = new Intent(context, DrillDetailsActivity.class);
        intent.putExtra(INTENT_EXTRA_PARAM_DRILL_ID, drillId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drill_details);
        if (savedInstanceState != null) {
            drillId = savedInstanceState.getString(INSTANCE_STATE_PARAM_DRILL_ID);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(INSTANCE_STATE_PARAM_DRILL_ID, drillId);
        super.onSaveInstanceState(Objects.requireNonNull(outState));
    }

    @Override
    protected Fragment getFragment() {
        drillId = getIntent().getStringExtra(INTENT_EXTRA_PARAM_DRILL_ID);
        Fragment fragment = new DrillDetailsFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_DRILL_ID, drillId);
        fragment.setArguments(args);

        Timber.i(fragment.getArguments().getString(PARAM_DRILL_ID, "drill id not set in getFragment"));
        Timber.i(drillId);
        return fragment;
    }
}
