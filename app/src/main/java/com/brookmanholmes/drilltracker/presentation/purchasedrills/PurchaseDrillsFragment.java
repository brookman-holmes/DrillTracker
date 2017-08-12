package com.brookmanholmes.drilltracker.presentation.purchasedrills;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.data.repository.DrillPackDataRepository;
import com.brookmanholmes.drilltracker.data.repository.datasource.DataStoreFactory;
import com.brookmanholmes.drilltracker.domain.interactor.GetDrillPackList;
import com.brookmanholmes.drilltracker.presentation.base.BaseFragment;
import com.brookmanholmes.drilltracker.presentation.drills.ActivityCallback;
import com.brookmanholmes.drilltracker.presentation.drills.FragmentCallback;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;
import com.brookmanholmes.drilltracker.presentation.model.DrillPackModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Brookman Holmes on 8/9/2017.
 */

public class PurchaseDrillsFragment extends BaseFragment<PurchaseDrillsPresenter> implements
        PurchaseDrillsView, ActivityCallback, PurchaseDrillsAdapter.OnItemClickListener {
    @BindView(R.id.scrollView)
    RecyclerView recyclerView;

    private PurchaseDrillsAdapter adapter;
    private DrillPackDataRepository repository = new DrillPackDataRepository(DataStoreFactory.getDrillPackDataStore());

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new PurchaseDrillsAdapter(getContext());
        GetDrillPackList getDrillPackList = new GetDrillPackList(getThreadExecutor(), getPostExecutionThread(), repository);
        presenter = new PurchaseDrillsPresenter(getDrillPackList);
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

    private FragmentCallback getCallback() {
        if (getActivity() instanceof FragmentCallback) {
            return ((FragmentCallback) getActivity());
        } else {
            throw new IllegalStateException("Parent activity must implement FragmentCallback");
        }
    }

    /*
        PurchaseDrillsAdapter.OnItemClickListener
     */

    @Override
    public void onDrillPackSelected(DrillPackModel pack) {

    }
    /*
        PurchaseDrillsView methods
     */

    @Override
    public void renderDrillPacks(List<DrillPackModel> drillPacks) {
        adapter.setData(drillPacks);
    }

    @Override
    public void viewDrillPack(DrillPackModel drillModel) {

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
    public void showError(String message) {
        this.showToastMessage(message);
    }

    @Override
    public Context context() {
        return getContext();
    }

    /*
        ActivityCallback methods
     */

    @Override
    public void setFilterSelection(DrillModel.Type type) {

    }
}
