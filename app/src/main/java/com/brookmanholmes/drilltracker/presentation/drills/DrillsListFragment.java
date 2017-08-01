package com.brookmanholmes.drilltracker.presentation.drills;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.domain.interactor.DeleteDrill;
import com.brookmanholmes.drilltracker.domain.interactor.GetDrillList;
import com.brookmanholmes.drilltracker.presentation.base.BaseActivity;
import com.brookmanholmes.drilltracker.presentation.createdrill.CreateDrillActivity;
import com.brookmanholmes.drilltracker.presentation.drilldetail.DrillDetailsActivity;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;
import com.brookmanholmes.drilltracker.presentation.mapper.DrillModelDataMapper;
import com.brookmanholmes.drilltracker.presentation.base.BaseFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Brookman Holmes on 7/7/2017.
 */

public class DrillsListFragment extends BaseFragment implements DrillsListView {
    private static final String TAG = DrillsListFragment.class.getName();

    private DrillsListPresenter drillsListPresenter;
    private DrillsListAdapter drillsListAdapter;
    private Unbinder unbinder;
    ArrayAdapter<CharSequence> adapter;

    @BindView(R.id.scrollView)
    RecyclerView recyclerView;
    @BindView(R.id.rl_progress)
    View rl_progress;
    @BindView(R.id.rl_retry)
    View rl_retry;
    @BindView(R.id.bt_retry)
    Button bt_retry;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab_add_drill;
    Spinner spinner;

    public DrillsListFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drillsListAdapter = new DrillsListAdapter(getContext());

        GetDrillList getDrillList = new GetDrillList(getDrillRepository(), getThreadExecutor(), getPostExecutionThread());
        DrillModelDataMapper drillModelDataMapper = new DrillModelDataMapper();
        drillsListPresenter = new DrillsListPresenter(getDrillList, drillModelDataMapper);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_drills_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        this.drillsListAdapter.setOnItemClickListener(onItemClickListener);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.recyclerView.setAdapter(drillsListAdapter);

        toolbar.inflateMenu(R.menu.fragment_drills_list_menu);

        spinner = (Spinner) toolbar.getMenu().findItem(R.id.spinner).getActionView();

        adapter = ArrayAdapter.createFromResource(new ContextThemeWrapper(getContext(), android.R.style.ThemeOverlay_Material_Dark_ActionBar), R.array.drill_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                drillsListPresenter.loadDrillsList(transformSelectionToModel(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.drillsListPresenter.setView(this);
        if (savedInstanceState == null)
            this.loadUserList();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.drillsListPresenter.resume();
        this.drillsListPresenter.loadDrillsList(getFilterSelection());
    }

    @Override
    public void onPause() {
        super.onPause();
        this.drillsListPresenter.pause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recyclerView.setAdapter(null);
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.drillsListPresenter.destroy();
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
            this.drillsListAdapter.setDrillsCollection(drillModelCollection);
        }
    }

    @Override
    public void showError(String message) {
        this.showToastMessage(message);
    }

    @Override
    public Context context() {
        return this.getActivity().getApplicationContext();
    }

    private void loadUserList() {
        this.drillsListPresenter.initialize(getFilterSelection());
    }

    private DrillModel.Type transformSelectionToModel(int selection) {
        switch(selection) {
            case 0:
                return DrillModel.Type.ANY;
            case 1:
                return DrillModel.Type.POSITIONAL;
            case 2:
                return DrillModel.Type.AIMING;
            case 3:
                return DrillModel.Type.SAFETY;
            case 4:
                return DrillModel.Type.PATTERN;
            case 5:
                return DrillModel.Type.KICKING;
            case 6:
                return DrillModel.Type.BANKING;
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
    public void viewDrill(DrillModel drillModel) {
        startActivity(DrillDetailsActivity.getIntent(getContext(), drillModel.id, drillModel.maxScore, drillModel.defaultTargetScore));
    }

    private DrillsListAdapter.OnItemClickListener onItemClickListener = new DrillsListAdapter.OnItemClickListener() {
        @Override
        public void onDrillItemClicked(DrillModel drillModel) {
            drillsListPresenter.onDrillClicked(drillModel);
        }

        @Override
        public void onDrillItemLongClicked(DrillModel drillModel) {
            drillsListPresenter.showDeleteConfirmation(drillModel);
        }
    };

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
