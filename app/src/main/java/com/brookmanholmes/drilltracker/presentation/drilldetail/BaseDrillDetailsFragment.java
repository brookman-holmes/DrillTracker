package com.brookmanholmes.drilltracker.presentation.drilldetail;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.CallSuper;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.presentation.adapters.SpinnerAdapterHelper;
import com.brookmanholmes.drilltracker.presentation.addattempt.AddAttemptDialogFactory;
import com.brookmanholmes.drilltracker.presentation.addeditdrill.AddEditDrillActivity;
import com.brookmanholmes.drilltracker.presentation.base.BaseFragment;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;
import com.brookmanholmes.drilltracker.presentation.view.util.ImageHandler;
import com.github.chrisbanes.photoview.PhotoView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

/**
 * Created by brookman on 1/8/18.
 */

public abstract class BaseDrillDetailsFragment extends BaseFragment<DrillDetailsContract> implements DrillDetailsView, Toolbar.OnMenuItemClickListener, View.OnClickListener {
    protected static final String TAG = DrillDetailsFragment.class.getName();
    static final String PARAM_DRILL_ID = "param_drill_id";
    static final String PARAM_MAX = "param_max";
    static final String PARAM_TARGET = "param_target";
    static final String PARAM_URL = "param_url";
    static final String PARAM_OB_POSITIONS = "param_ob_positions";
    static final String PARAM_CB_POSITIONS = "param_cb_positions";
    static final String PARAM_TARGET_POSITIONS = "param_target_positions";
    private static final String PARAM_DRILL_TYPE = "param_drill_type";

    @BindView(R.id.cbPositionsSpinner)
    Spinner cbPositionsSpinner;
    @BindView(R.id.obPositionsSpinner)
    Spinner obPositionsSpinner;
    @BindView(R.id.targetPositionsSpinner)
    Spinner targetPositionsSpinner;

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
    final DecimalFormat pctf = new DecimalFormat("#.00");

    public static Fragment createDrillDetailsFragment(String drillId, String imageUrl, DrillModel.Type type,
                                                      int maxValue, int targetValue, int obPositions,
                                                      int cbPositions, int targetPositions) {
        switch (type) {
            case SAFETY:
                return SafetyDrillDetailsFragment.forDrill(drillId, imageUrl, obPositions, cbPositions);
            case AIMING:
            case KICKING:
            case BANKING:
                return AimDrillDetailsFragment.forDrill(drillId, imageUrl, maxValue, targetValue, obPositions, cbPositions);
            case SPEED:
                return SpeedDrillDetailsFragment.forDrill(drillId, imageUrl, maxValue, targetValue, obPositions, cbPositions);
            case POSITIONAL:
                return PositionalDrillDetailsFragment.forDrill(drillId, imageUrl, cbPositions, targetPositions);
            case PATTERN:
                return PatternDrillDetailsFragment.forDrill(drillId, imageUrl, maxValue);
            default:
                return DrillDetailsFragment.forDrill(drillId, imageUrl, maxValue, targetValue, obPositions, cbPositions, targetPositions);
        }
    }

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
        setupSpinners();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDrillDetails();
    }

    private void loadDrillDetails() {
        if (presenter != null)
            presenter.initialize(getDrillId());
    }

    private void setupSpinners() {
        obPositionsSpinner.setAdapter(SpinnerAdapterHelper.createNumberedListAdapter(toolbar.getContext(), 0, getObPositions() + 1, "OB Position "));
        cbPositionsSpinner.setAdapter(SpinnerAdapterHelper.createNumberedListAdapter(toolbar.getContext(), 0, getCbPositions() + 1, "CB Position "));
        targetPositionsSpinner.setAdapter(SpinnerAdapterHelper.createNumberedListAdapter(toolbar.getContext(), 0, getTargetPositions() + 1, "Target "));
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

        if (getTargetPositions() > 1) {
            targetPositionsSpinner.setVisibility(View.VISIBLE);
        } else {
            targetPositionsSpinner.setVisibility(View.GONE);
        }
    }

    private void setupToolbar() {
        toolbar.inflateMenu(R.menu.fragment_drill_details_menu);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(this);
        toolbar.setOnMenuItemClickListener(this);
        setupSpinners();
    }

    void setArguments(DrillModel model) {
        Objects.requireNonNull(getArguments()).putString(PARAM_DRILL_ID, model.id);
        getArguments().putSerializable(PARAM_DRILL_TYPE, model.drillType);
        getArguments().putInt(PARAM_MAX, model.maxScore);
        getArguments().putInt(PARAM_TARGET, model.defaultTargetScore);
        getArguments().putString(PARAM_URL, model.imageUrl);

        if (getTargetPositions() != model.targetPositions ||
                getCbPositions() != model.cbPositions ||
                getObPositions() != model.obPositions) {
            int targetPositionSelection = Math.min(model.targetPositions, getSelectedTargetPosition());
            int cbPositionSelection = Math.min(model.cbPositions, getSelectedCbPosition());
            int obPositionSelection = Math.min(model.obPositions, getSelectedObPosition());

            setupSpinners();
            targetPositionsSpinner.setSelection(targetPositionSelection);
            cbPositionsSpinner.setSelection(cbPositionSelection);
            obPositionsSpinner.setSelection(obPositionSelection);
        }
        getArguments().putInt(PARAM_CB_POSITIONS, model.cbPositions);
        getArguments().putInt(PARAM_OB_POSITIONS, model.obPositions);
        getArguments().putInt(PARAM_TARGET_POSITIONS, model.targetPositions);
    }

    private DrillModel.Type getDrillType() {
        return (DrillModel.Type) Objects.requireNonNull(getArguments()).getSerializable(PARAM_DRILL_TYPE);
    }

    String getDrillId() {
        return Objects.requireNonNull(getArguments()).getString(PARAM_DRILL_ID, "invalid drill id");
    }

    private String getDrillUrl() {
        return Objects.requireNonNull(getArguments()).getString(PARAM_URL, "invalid url");
    }

    private int getMaxScore() {
        return Objects.requireNonNull(getArguments()).getInt(PARAM_MAX, 0);
    }

    private int getTargetScore() {
        return Objects.requireNonNull(getArguments()).getInt(PARAM_TARGET, 0);
    }

    private int getObPositions() {
        return Objects.requireNonNull(getArguments()).getInt(PARAM_OB_POSITIONS, 0);
    }

    private int getCbPositions() {
        return Objects.requireNonNull(getArguments()).getInt(PARAM_CB_POSITIONS, 0);
    }

    private int getTargetPositions() {
        return Objects.requireNonNull(getArguments()).getInt(PARAM_TARGET_POSITIONS, 0);
    }

    private int getSelectedTargetPosition() {
        return targetPositionsSpinner.getSelectedItemPosition();
    }

    int getSelectedCbPosition() {
        return cbPositionsSpinner.getSelectedItemPosition();
    }

    int getSelectedObPosition() {
        return obPositionsSpinner.getSelectedItemPosition();
    }

    List<Integer> getSelectedPattern() {
        return new ArrayList<>();
    }

    private void setMenuIconEnabled(@IdRes int res, boolean isEnabled) {
        toolbar.getMenu().findItem(res).setEnabled(isEnabled);
        toolbar.getMenu().findItem(res).getIcon().setAlpha(isEnabled ? 255 : 127);
    }

    @Override
    public void onClick(View view) {
        Objects.requireNonNull(getActivity()).finish();
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
        dialogFragment.show(Objects.requireNonNull(getFragmentManager()), "tag");
    }

    @OnClick(R.id.fab)
    public void onAddAttemptClicked() {
        presenter.onAddAttemptClicked(getDrillId(), getDrillType(), getMaxScore(), getTargetScore(),
                getCbPositions(), getObPositions(), getTargetPositions(), getSelectedCbPosition(),
                getSelectedObPosition(), getSelectedTargetPosition(), getSelectedPattern());
    }


    @OnItemSelected({R.id.obPositionsSpinner, R.id.cbPositionsSpinner, R.id.targetPositionsSpinner})
    void onBallPositionSelected() {
        presenter.initialize(getDrillId());
    }

    protected abstract @LayoutRes
    int getLayoutRes();

    @Override
    public void showAddAttemptDialog(String drillId, DrillModel.Type type, int maxScore, int targetScore,
                                     int cbPositions, int obPositions, int targetPositions, int selectedCbPosition,
                                     int selectedObPosition, int selectedTargetPosition, List<Integer> selectedPattern) {
        AddAttemptDialogFactory.createDialog(drillId, type, targetScore, cbPositions,
                obPositions, targetPositions, selectedCbPosition, selectedObPosition,
                selectedTargetPosition, selectedPattern).show(Objects.requireNonNull(getFragmentManager()), "AddAttemptDialog");
    }

    /**
     * Implementation of {@link DrillDetailsView}
     */


    @Override
    @CallSuper
    public void renderDrill(DrillModel drill) {
        if (drill != null) {
            setArguments(drill);
            ImageHandler.loadImage(image, drill.imageUrl);
            toolbar.setTitle(drill.name);
            description.setText(drill.description);

            setMenuIconEnabled(R.id.ic_edit, !drill.purchased);
            setMenuIconEnabled(R.id.ic_undo_attempt, drill.attemptModels.size() > 0);
        }
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
