package com.brookmanholmes.drilltracker.presentation.drills;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.domain.interactor.GetDrillList;
import com.brookmanholmes.drilltracker.presentation.base.BaseFragment;
import com.brookmanholmes.drilltracker.presentation.createdrill.CreateDrillActivity;
import com.brookmanholmes.drilltracker.presentation.deletedrill.DeleteDrillDialog;
import com.brookmanholmes.drilltracker.presentation.drilldetail.DrillDetailsActivity;
import com.brookmanholmes.drilltracker.presentation.mapper.DrillModelDataMapper;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Brookman Holmes on 7/7/2017.
 */

public class DrillsListFragment extends BaseFragment<DrillsListPresenter> implements DrillsListView, DrillsListAdapter.OnItemClickListener, AdapterView.OnItemSelectedListener {
    private static final String TAG = DrillsListFragment.class.getName();
    @BindView(R.id.scrollView)
    RecyclerView recyclerView;
    @BindView(R.id.rl_progress)
    View rl_progress;
    @BindView(R.id.rl_retry)
    View rl_retry;
    @BindView(R.id.bt_retry)
    Button bt_retry;
    @BindView(R.id.fab)
    FloatingActionButton fab_add_drill;
    Spinner spinner;
    private DrillsListAdapter drillsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");

        drillsAdapter = new DrillsListAdapter(getContext());

        GetDrillList getDrillList = new GetDrillList(getDrillRepository(), getThreadExecutor(), getPostExecutionThread());
        DrillModelDataMapper drillModelDataMapper = new DrillModelDataMapper();
        presenter = new DrillsListPresenter(getDrillList, drillModelDataMapper);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drills_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        presenter.setView(this);
        drillsAdapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setItemViewCacheSize(10);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setAdapter(drillsAdapter);

        toolbar.inflateMenu(R.menu.fragment_drills_list_menu);

        spinner = (Spinner) toolbar.getMenu().findItem(R.id.spinner).getActionView();
        ArrayAdapter<CharSequence> spinnerAdapter;
        spinnerAdapter = ArrayAdapter.createFromResource(new ContextThemeWrapper(getContext(), android.R.style.ThemeOverlay_Material_Dark_ActionBar), R.array.drill_types, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null)
            this.loadUserList();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.presenter.loadDrillsList(getFilterSelection());
    }

    @Override
    public void onDestroyView() {
        recyclerView.setAdapter(null);
        super.onDestroyView();
    }

    private void loadUserList() {
        this.presenter.initialize(getFilterSelection());
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

    private DrillModel.Type getFilterSelection() {
        return transformSelectionToModel(spinner.getSelectedItemPosition());
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

    @Override
    public void renderUserList(List<DrillModel> drillModelCollection) {
        if (drillModelCollection != null) {
            this.drillsAdapter.setDrillsCollection(drillModelCollection);
        }
    }

    @Override
    public void showError(String message) {
        this.showToastMessage(message);
    }

    @Override
    public Context context() {
        return getContext();
    }

    @Override
    public void onDrillItemClicked(DrillModel drillModel) {
        presenter.onDrillClicked(drillModel);
    }

    @Override
    public void onDrillItemLongClicked(DrillModel drillModel) {
        presenter.showDeleteConfirmation(drillModel);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        presenter.loadDrillsList(transformSelectionToModel(i));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void viewDrill(DrillModel drillModel) {
        startActivity(DrillDetailsActivity.getIntent(getContext(), drillModel.id, drillModel.maxScore, drillModel.defaultTargetScore));
    }

    @OnClick(R.id.fab)
    @Override
    public void showCreateDrillActivity() {
        startActivity(new Intent(getContext(), CreateDrillActivity.class));
    }

    @Override
    public void showDeleteConfirmation(final DrillModel drillModel) {
        DeleteDrillDialog dialog = DeleteDrillDialog.newInstance(drillModel.id);
        dialog.show(getFragmentManager(), DeleteDrillDialog.class.getName());
    }
}
