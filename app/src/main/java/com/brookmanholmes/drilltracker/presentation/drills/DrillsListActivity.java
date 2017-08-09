package com.brookmanholmes.drilltracker.presentation.drills;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.presentation.base.BaseActivity;

public class DrillsListActivity extends BaseActivity {
    private static final String TAG = DrillsListActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drill_list);
    }

    @Override
    protected Fragment getFragment() {
        return new DrillsListFragment();
    }
}
