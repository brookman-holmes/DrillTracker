package com.brookmanholmes.drilltracker.presentation.drilldetail;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.util.Preconditions;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.domain.interactor.DeleteAttempt;
import com.brookmanholmes.drilltracker.domain.interactor.GetDrillDetails;
import com.brookmanholmes.drilltracker.presentation.addattempt.AddAttemptDialog;
import com.brookmanholmes.drilltracker.presentation.base.BaseFragment;
import com.brookmanholmes.drilltracker.presentation.createdrill.CreateDrillActivity;
import com.brookmanholmes.drilltracker.presentation.mapper.DrillModelDataMapper;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;
import com.brookmanholmes.drilltracker.presentation.model.DrillModelMathUtil;
import com.brookmanholmes.drilltracker.presentation.view.util.ChartUtil;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by Brookman Holmes on 7/11/2017.
 */

public class DrillDetailsFragment extends BaseFragment<DrillDetailsPresenter> implements DrillDetailView, Toolbar.OnMenuItemClickListener, View.OnClickListener {
    private static final String TAG = DrillDetailsFragment.class.getName();
    private static final String PARAM_DRILL_ID = "param_drill_id";
    private static final String PARAM_MAX = "param_max";
    private static final String PARAM_TARGET = "param_target";

    @BindView(R.id.rl_progress)
    View rl_progress;
    @BindView(R.id.rl_retry)
    View rl_retry;
    @BindView(R.id.bt_retry)
    Button bt_retry;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.lifetimeChart)
    LineChartView lifetimeChart;
    @BindView(R.id.sessionChart)
    LineChartView sessionChart;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.sessionHigh)
    TextView sessionHigh;
    @BindView(R.id.sessionAttempts)
    TextView sessionAttempts;
    @BindView(R.id.sessionAvg)
    TextView sessionAvg;
    @BindView(R.id.sessionMedian)
    TextView sessionMedian;

    @BindView(R.id.lifetimeHigh)
    TextView lifetimeHigh;
    @BindView(R.id.lifetimeAttempts)
    TextView lifetimeAttempts;
    @BindView(R.id.lifetimeAvg)
    TextView lifetimeAvg;
    @BindView(R.id.lifetimeMedian)
    TextView lifetimeMedian;

    @BindView(R.id.maxScoreAttainable)
    TextView maxScoreAttainable;
    @BindView(R.id.targetScore)
    TextView targetScore;

    @BindView(R.id.image)
    PhotoView image;

    public static DrillDetailsFragment forDrill(String drillId, int maxValue, int targetValue) {
        final DrillDetailsFragment fragment = new DrillDetailsFragment();
        final Bundle args = new Bundle();
        args.putString(PARAM_DRILL_ID, drillId);
        args.putInt(PARAM_MAX, maxValue);
        args.putInt(PARAM_TARGET, targetValue);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GetDrillDetails getDrillDetailsUseCase = new GetDrillDetails(getDrillRepository(), getThreadExecutor(), getPostExecutionThread());
        DeleteAttempt deleteAttemptUseCase = new DeleteAttempt(getDrillRepository(), getThreadExecutor(), getPostExecutionThread());
        presenter = new DrillDetailsPresenter(getDrillDetailsUseCase, deleteAttemptUseCase, new DrillModelDataMapper());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drill_details, container, false);
        unbinder = ButterKnife.bind(this, view);
        presenter.setView(this);

        toolbar.inflateMenu(R.menu.fragment_drill_details_menu);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(this);
        toolbar.setOnMenuItemClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDrillDetails();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.ic_edit) {
            presenter.onEditClicked();
        } else if (item.getItemId() == R.id.ic_undo_attempt) {
            presenter.onUndoClicked();
        }
        return true;
    }

    @Override
    public void showLoading() {
        rl_progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        rl_progress.setVisibility(View.GONE);
    }

    @Override
    public void showRetry() {
        rl_retry.setVisibility(View.VISIBLE);
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
    public void renderDrill(DrillModel drill) {
        if (drill != null) {
            //ImageHandler.loadImage(image, drill.imageUrl);
            Picasso.with(context()).load(drill.imageUrl).transform(new RoundedCornersTransformation(50, 10)).into(image);
            DrillModelMathUtil lifetimeStats = new DrillModelMathUtil(drill.attemptModels);
            DrillModelMathUtil sessionStats = new DrillModelMathUtil(drill.getSessionAttempts());
            description.setText(drill.description);
            ChartUtil.setupLifetimeChart(lifetimeChart, drill, true);
            ChartUtil.setupChart(sessionChart, DrillModel.getSessionModel(drill));
            toolbar.setTitle(drill.name);

            sessionHigh.setText(getString(R.string.number, sessionStats.getMax()));
            sessionAttempts.setText(getString(R.string.number, sessionStats.getAttempts()));
            sessionAvg.setText(getString(R.string.number_float, sessionStats.getAverage()));
            sessionMedian.setText(getString(R.string.number_float, sessionStats.getMedian()));

            lifetimeHigh.setText(getString(R.string.number, lifetimeStats.getMax()));
            lifetimeAttempts.setText(getString(R.string.number, lifetimeStats.getAttempts()));
            lifetimeAvg.setText(getString(R.string.number_float, lifetimeStats.getAverage()));
            lifetimeMedian.setText(getString(R.string.number_float, lifetimeStats.getMedian()));

            maxScoreAttainable.setText(getString(R.string.max_score_attainable, drill.maxScore));
            targetScore.setText(getString(R.string.target_score_with_number, drill.defaultTargetScore));

            toolbar.getMenu().findItem(R.id.ic_undo_attempt).setEnabled(drill.attemptModels.size() > 0);
            toolbar.getMenu().findItem(R.id.ic_undo_attempt).getIcon().setAlpha(drill.attemptModels.size() > 0 ? 255 : 127);
        }
    }

    @Override
    public Context context() {
        return getContext();
    }

    private void loadDrillDetails() {
        if (presenter != null)
            presenter.initialize(currentDrillId());
    }

    private String currentDrillId() {
        final Bundle arguments = getArguments();
        Preconditions.checkNotNull(arguments, "Fragment arguments cannot be null");
        return arguments.getString(PARAM_DRILL_ID);
    }

    private int currentMaxScore() {
        final Bundle arguments = getArguments();
        return arguments.getInt(PARAM_MAX);
    }

    private int currentTargetScore() {
        final Bundle arguments = getArguments();
        return arguments.getInt(PARAM_TARGET);
    }

    public void showDrillImageFullScreen(DrillModel drillModel) {
        DialogFragment dialogFragment = FullScreenImageDialog.newInstance(drillModel.imageUrl);
        dialogFragment.show(getFragmentManager(), "tag");
    }


    @Override
    public void showAddAttemptView() {
        AddAttemptDialog addAttemptDialog = AddAttemptDialog.newInstance(currentDrillId(), currentMaxScore(), currentTargetScore());
        addAttemptDialog.show(getFragmentManager(), AddAttemptDialog.class.getName());
    }

    @Override
    public void showEditDrillView(String drillId) {
        startActivity(CreateDrillActivity.newInstance(getContext(), drillId));
    }

    @OnClick(R.id.bt_retry)
    void onButtonRetryClick() {
        DrillDetailsFragment.this.loadDrillDetails();
    }

    @OnClick(R.id.fab)
    @Override
    public void onFabClicked() {
        presenter.showAddAttemptView();
    }

    @OnClick(R.id.image)
    public void onImageClicked() {
        presenter.onDrillImageClicked();
    }

    @Override
    public void onClick(View view) {
        getActivity().finish();
    }
}
