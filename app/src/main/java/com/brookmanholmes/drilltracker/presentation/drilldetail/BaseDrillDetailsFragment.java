package com.brookmanholmes.drilltracker.presentation.drilldetail;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.presentation.adapters.SpinnerAdapterHelper;
import com.brookmanholmes.drilltracker.presentation.addeditdrill.AddEditDrillActivity;
import com.brookmanholmes.drilltracker.presentation.base.BaseFragment;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;
import com.github.chrisbanes.photoview.PhotoView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

/**
 * Created by brookman on 1/8/18.
 */

public abstract class BaseDrillDetailsFragment extends BaseFragment<DrillDetailsContract> implements DrillDetailsView, Toolbar.OnMenuItemClickListener, View.OnClickListener {
    protected static final String TAG = DrillDetailsFragment.class.getName();
    protected static final String PARAM_DRILL_ID = "param_drill_id";
    protected static final String PARAM_MAX = "param_max";
    protected static final String PARAM_TARGET = "param_target";
    protected static final String PARAM_URL = "param_url";
    protected static final String PARAM_OB_POSITIONS = "param_ob_positions";
    protected static final String PARAM_CB_POSITIONS = "param_cb_positions";

    @BindView(R.id.cbPositionsSpinner)
    Spinner cbPositionsSpinner;
    @BindView(R.id.obPositionsSpinner)
    Spinner obPositionsSpinner;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.rl_progress)
    View rl_progress;
    @BindView(R.id.rl_retry)
    View rl_retry;
    @BindView(R.id.bt_retry)
    Button bt_retry;

    @BindView(R.id.image)
    PhotoView image;
    @BindView(R.id.description)
    TextView description;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new DrillDetailsPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutRes(), container, false);
        unbinder = ButterKnife.bind(this, view);
        presenter.setView(this);

        setupToolbar();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDrillDetails();
    }

    protected void loadDrillDetails() {
        if (presenter != null)
            presenter.initialize(getDrillId());
    }

    protected void setupSpinners() {
        obPositionsSpinner.setAdapter(SpinnerAdapterHelper.createNumberedListAdapter(toolbar.getContext(), 0, getObPositions() + 1, "OB Position "));
        cbPositionsSpinner.setAdapter(SpinnerAdapterHelper.createNumberedListAdapter(toolbar.getContext(), 0, getCbPositions() + 1, "CB Position "));
        if (getCbPositions() > 1) {
            cbPositionsSpinner.setVisibility(View.VISIBLE);
        } else {
            cbPositionsSpinner.setVisibility(View.GONE);
        }

        if (getObPositions() > 1) {
            obPositionsSpinner.setVisibility(View.VISIBLE);
        } else {
            obPositionsSpinner.setVisibility(View.GONE);
        }
    }

    protected void setupToolbar() {
        toolbar.inflateMenu(R.menu.fragment_drill_details_menu);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(this);
        toolbar.setOnMenuItemClickListener(this);
        setupSpinners();
    }

    protected void setArguments(DrillModel model) {
        getArguments().putString(PARAM_DRILL_ID, model.id);
        getArguments().putInt(PARAM_MAX, model.maxScore);
        getArguments().putInt(PARAM_TARGET, model.defaultTargetScore);
        getArguments().putString(PARAM_URL, model.imageUrl);
        getArguments().putInt(PARAM_CB_POSITIONS, model.cbPositions);
        getArguments().putInt(PARAM_OB_POSITIONS, model.obPositions);
    }

    protected String getDrillId() {
        return getArguments().getString(PARAM_DRILL_ID);
    }

    protected String getDrillUrl() {
        return getArguments().getString(PARAM_URL, "invalid url");
    }

    protected int getMaxScore() {
        return getArguments().getInt(PARAM_MAX);
    }

    protected int getTargetScore() {
        return getArguments().getInt(PARAM_TARGET);
    }

    protected int getObPositions() {
        return getArguments().getInt(PARAM_OB_POSITIONS);
    }

    protected int getCbPositions() {
        return getArguments().getInt(PARAM_CB_POSITIONS);
    }

    protected int getSelectedCbPosition() {
        return cbPositionsSpinner.getSelectedItemPosition();
    }

    protected int getSelectedObPosition() {
        return obPositionsSpinner.getSelectedItemPosition();
    }

    protected void setMenuIconEnabled(@IdRes int res, boolean enabled) {
        toolbar.getMenu().findItem(res).setEnabled(enabled);
        toolbar.getMenu().findItem(res).getIcon().setAlpha(enabled ? 255 : 127);
    }

    @Override
    public void onClick(View view) {
        getActivity().finish();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.ic_edit) {
            startActivity(AddEditDrillActivity.newInstance(getContext(), getDrillId()));
        } else if (item.getItemId() == R.id.ic_undo_attempt) {
            presenter.onUndoClicked();
        }
        return true;
    }

    /**
     * On click methods
     */

    @OnClick(R.id.bt_retry)
    void onButtonRetryClick() {
        this.loadDrillDetails();
    }

    @OnClick(R.id.image)
    public void onImageClicked() {
        DialogFragment dialogFragment = FullScreenImageDialog.newInstance(getDrillUrl());
        dialogFragment.show(getFragmentManager(), "tag");
    }

    @OnClick(R.id.fab)
    public void onAddAttemptClicked() {
        getAddAttemptDialog().show(getFragmentManager(), "AddAttemptDialog");
    }

    @OnItemSelected({R.id.obPositionsSpinner, R.id.cbPositionsSpinner})
    void onBallPositionSelected() {
        presenter.initialize(getDrillId());
    }

    protected abstract @LayoutRes
    int getLayoutRes();

    protected abstract DialogFragment getAddAttemptDialog();

    /**
     * Implementation of {@link DrillDetailsView}
     */

    @Override
    public void hideLoading() {
        rl_progress.setVisibility(View.GONE);
    }

    @Override
    public void showRetry() {
        rl_retry.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoading() {
        rl_progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideRetry() {
        rl_retry.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {
        this.showToastMessage(message);
    }

    @Override
    public Context context() {
        return getContext();
    }
}
