package com.brookmanholmes.drilltracker.presentation.base;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.presentation.signin.SignInActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Brookman Holmes on 8/9/2017.
 */

public abstract class BaseViewPagerActivity extends AppCompatActivity {
    private static final String TAG = BaseActivity.class.getName();

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
            FragmentPagerAdapter adapter = getAdapter();
            pager.setAdapter(adapter);
            tabs.setupWithViewPager(pager);
        }
    }


    protected abstract FragmentPagerAdapter getAdapter();
}
