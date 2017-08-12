package com.brookmanholmes.drilltracker.presentation.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.brookmanholmes.drilltracker.R;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Brookman Holmes on 8/9/2017.
 */

public abstract class BaseViewPagerActivity extends AppCompatActivity {
    private static final String TAG = BaseActivity.class.getName();

    protected FragmentPagerAdapter adapter;

    @BindView(R.id.pager)
    protected ViewPager pager;
    @BindView(R.id.tabs)
    protected TabLayout tabs;
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        } else {
            ButterKnife.bind(this);
            setSupportActionBar(toolbar);
            adapter = getAdapter();
            pager.setAdapter(adapter);
            tabs.setupWithViewPager(pager);
        }
    }


    protected abstract FragmentPagerAdapter getAdapter();
}
