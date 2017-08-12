package com.brookmanholmes.drilltracker.presentation.drills;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.domain.interactor.GetDrillList;
import com.brookmanholmes.drilltracker.presentation.base.BaseFragment;
import com.brookmanholmes.drilltracker.presentation.deletedrill.DeleteDrillDialog;
import com.brookmanholmes.drilltracker.presentation.drilldetail.DrillDetailsActivity;
import com.brookmanholmes.drilltracker.presentation.mapper.DrillModelDataMapper;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Brookman Holmes on 7/7/2017.
 */

public class DrillsListFragment extends BaseFragment<DrillsListPresenter> implements DrillsListView,
        DrillsListAdapter.OnItemClickListener, ActivityCallback {
    private static final String TAG = DrillsListFragment.class.getName();

    @BindView(R.id.scrollView)
    RecyclerView recyclerView;

    private DrillsListAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new DrillsListAdapter(getContext());

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
        adapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setItemViewCacheSize(10);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setAdapter(adapter);

        getCallback().addListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null)
            this.loadUserList();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.presenter.loadDrillsList(getCallback().getTypeSelection());
    }

    @Override
    public void onDestroyView() {
        recyclerView.setAdapter(null);
        getCallback().removeListener(this);
        super.onDestroyView();
    }

    private void loadUserList() {
        this.presenter.initialize(getCallback().getTypeSelection());
    }

    @Override
    public void showLoading() {
        getCallback().showLoading();
    }

    @Override
    public void hideLoading() {
        getCallback().hideLoading();
    }

    @Override
    public void showRetry() {
        getCallback().showRetry();
    }

    @Override
    public void hideRetry() {
        getCallback().hideRetry();
    }

    @Override
    public void renderDrillList(List<DrillModel> drillModelCollection) {
        if (drillModelCollection != null) {
            this.adapter.setData(drillModelCollection);
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

    private FragmentCallback getCallback() {
        if (getActivity() instanceof FragmentCallback) {
            return ((FragmentCallback) getActivity());
        } else {
            throw new IllegalStateException("Parent activity must implement FragmentCallback");
        }
    }

    @Override
    public void setFilterSelection(DrillModel.Type type) {
        presenter.loadDrillsList(type);
    }

    @Override
    public void viewDrill(DrillModel drillModel) {
        startActivity(DrillDetailsActivity.getIntent(getContext(), drillModel.id, drillModel.maxScore, drillModel.defaultTargetScore));
    }

    @Override
    public void showDeleteConfirmation(final DrillModel drillModel) {
        DeleteDrillDialog dialog = DeleteDrillDialog.newInstance(drillModel.id);
        dialog.show(getFragmentManager(), DeleteDrillDialog.class.getName());
    }
}
