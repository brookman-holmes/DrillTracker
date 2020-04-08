package com.brookmanholmes.drilltracker.presentation.drills;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.presentation.base.BaseFragment;
import com.brookmanholmes.drilltracker.presentation.deletedrill.DeleteDrillDialog;
import com.brookmanholmes.drilltracker.presentation.drilldetail.DrillDetailsActivity;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Brookman Holmes on 7/7/2017.
 */

public class DrillsListFragment extends BaseFragment<DrillsListContract> implements DrillsListView,
        DrillsListAdapter.OnItemClickListener<DrillModel>, ActivityCallback {
    private static final String TAG = DrillsListFragment.class.getName();

    @BindView(R.id.scrollView)
    RecyclerView recyclerView;

    private DrillsListAdapter adapter;

    /**
     * Fragment lifecycle methods
     */

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new DrillsListAdapter(getContext());
        presenter = new DrillsListPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drills_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        presenter.setView(this);
        adapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemViewCacheSize(8);
        recyclerView.setHasFixedSize(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setDrawingCacheEnabled(true);
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
        loadUserList();
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


    private void loadUserList() {
        this.presenter.initialize(getCallback().getTypeSelection());
    }

    /**
     * Implementation of DrillsListView
     */

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
    public void setFilterSelection(DrillModel.Type type) {
        presenter.initialize(type);
    }

    @Override
    public void viewDrill(DrillModel drillModel) {
        startActivity(DrillDetailsActivity.getIntent(
                getContext(),
                drillModel.id,
                drillModel.drillType,
                drillModel.imageUrl,
                drillModel.maxScore,
                drillModel.defaultTargetScore,
                drillModel.obPositions,
                drillModel.cbPositions,
                drillModel.targetPositions));
    }

    /**
     * Implementation of item click listeners in {@link DrillsListAdapter}
     */

    @Override
    public void onItemClicked(DrillModel drillModel, @IdRes int id) {
        presenter.onDrillClicked(drillModel);
    }

    @Override
    public void onItemLongClicked(DrillModel drillModel) {
        if (!drillModel.purchased) {
            DeleteDrillDialog dialog = DeleteDrillDialog.newInstance(drillModel.id);
            dialog.show(Objects.requireNonNull(getFragmentManager()), DeleteDrillDialog.class.getName());
        }
    }
}
