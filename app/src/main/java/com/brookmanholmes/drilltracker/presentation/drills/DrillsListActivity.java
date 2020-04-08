package com.brookmanholmes.drilltracker.presentation.drills;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.presentation.addeditdrill.AddEditDrillActivity;
import com.brookmanholmes.drilltracker.presentation.base.BaseViewPagerActivity;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;
import com.brookmanholmes.drilltracker.presentation.purchasedrills.PurchaseDrillsFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Brookman Holmes on 8/9/2017.
 */

public class DrillsListActivity extends BaseViewPagerActivity implements AdapterView.OnItemSelectedListener,
        DrillFilter, ViewPager.OnPageChangeListener, FragmentCallback {

    @BindView(R.id.rl_progress)
    View rl_progress;
    @BindView(R.id.rl_retry)
    View rl_retry;
    @BindView(R.id.bt_retry)
    Button bt_retry;

    private final List<ActivityCallback> callbacks = new ArrayList<>();
    private Spinner spinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_drills_list_menu, menu);
        MenuItem item = menu.findItem(R.id.spinner);

        spinner = (Spinner) item.getActionView();

        spinner.setAdapter(createAdapter());
        spinner.setOnItemSelectedListener(this);

        pager.addOnPageChangeListener(this);
        pager.setOffscreenPageLimit(0);
        return true;
    }

    @Override
    protected FragmentPagerAdapter getAdapter() {
        return new PagerAdapter(getSupportFragmentManager());
    }

    private ArrayAdapter<CharSequence> createAdapter() {
        ArrayAdapter<CharSequence> spinnerAdapter
                = ArrayAdapter.createFromResource(
                Objects.requireNonNull(getSupportActionBar()).getThemedContext(),
                R.array.drill_types,
                android.R.layout.simple_spinner_item);

        spinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);

        return spinnerAdapter;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        for (ActivityCallback callback : callbacks) {
            callback.setFilterSelection(getFilterSelection());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private DrillModel.Type transformSelectionToModel(int selection) {
        switch (selection) {
            case 0:
                return DrillModel.Type.ANY;
            case 1:
                return DrillModel.Type.AIMING;
            case 2:
                return DrillModel.Type.BANKING;
            case 3:
                return DrillModel.Type.KICKING;
            case 4:
                return DrillModel.Type.PATTERN;
            case 5:
                return DrillModel.Type.POSITIONAL;
            case 6:
                return DrillModel.Type.SAFETY;
            case 7:
                return DrillModel.Type.SPEED;
            default:
                throw new IllegalArgumentException("No such selection possible: " + selection);
        }
    }

    @Override
    public DrillModel.Type onSelected() {
        return null;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void addListener(ActivityCallback callback) {
        callbacks.add(callback);
    }

    @Override
    public void removeListener(ActivityCallback callback) {
        callbacks.remove(callback);
    }

    @Override
    public DrillModel.Type getTypeSelection() {
        return getFilterSelection();
    }

    @Override
    public DrillModel.Type getFilterSelection() {
        if (spinner != null)
            return transformSelectionToModel(spinner.getSelectedItemPosition());
        else
            return DrillModel.Type.ANY;
    }

    @Override
    public void showLoading() {
        this.rl_progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        this.rl_progress.setVisibility(View.GONE);
    }

    @Override
    public void showRetry() {
        this.rl_retry.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideRetry() {
        this.rl_retry.setVisibility(View.GONE);
    }

    @OnClick(R.id.fab)
    @Override
    public void showCreateDrillActivity() {
        startActivity(new Intent(this, AddEditDrillActivity.class));
    }

    private static class PagerAdapter extends FragmentPagerAdapter {
        private PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new DrillsListFragment();
                case 1:
                    return new PurchaseDrillsFragment();
                default:
                    throw new IllegalArgumentException("Position must be between 0 and 1, was: " + position);
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "My drills";
                case 1:
                    return "Get drills";
                default:
                    throw new IllegalArgumentException("Position must be between 0 and 1, was: " + position);
            }
        }
    }
}
