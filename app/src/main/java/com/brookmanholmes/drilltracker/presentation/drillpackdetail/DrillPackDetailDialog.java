package com.brookmanholmes.drilltracker.presentation.drillpackdetail;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.presentation.base.BaseDialogFragment;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Brookman Holmes on 8/16/2017.
 */

public class DrillPackDetailDialog extends BaseDialogFragment<DrillPackDetailPresenter>
        implements DrillPackDetailView {
    private static final String TAG = DrillPackDetailDialog.class.getName();

    private static final String PARAM_TITLE = "param_title";
    private static final String PARAM_DRILL_PACK_ID = "param_drill_pack_id";

    @BindView(R.id.scrollView2)
    RecyclerView recyclerView;
    @BindView(R.id.rl_progress)
    View rl_progress;
    @BindView(R.id.rl_retry)
    View rl_retry;

    private DrillsListAdapter adapter;
    private Unbinder unbinder;

    public static DrillPackDetailDialog newInstance(String title, String drillPackId) {
        DrillPackDetailDialog dialog = new DrillPackDetailDialog();
        Bundle args = new Bundle();
        args.putString(PARAM_TITLE, title);
        args.putString(PARAM_DRILL_PACK_ID, drillPackId);

        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        adapter = new DrillsListAdapter(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getDrillPackDrillList(Objects.requireNonNull(getArguments()).getString(PARAM_DRILL_PACK_ID));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    protected boolean hasTitle() {
        return true;
    }

    @Override
    protected void setDialogBuilderView(AlertDialog.Builder dialogBuilder) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_recycler_view, null, false);
        unbinder = ButterKnife.bind(this, view);
        dialogBuilder.setView(view);
        presenter.setView(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(Objects.requireNonNull(getContext()), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(getContext(), R.drawable.divider)));
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    protected String getTitle() {
        return Objects.requireNonNull(getArguments()).getString(PARAM_TITLE);
    }

    @Override
    protected boolean hasNegativeButton() {
        return false;
    }

    @Override
    protected DrillPackDetailPresenter getPresenter() {
        return new DrillPackDetailPresenter();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        dismiss();
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
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context context() {
        return getContext();
    }

    @Override
    public void renderDrillList(List<DrillModel> drillModelList) {
        adapter.setData(drillModelList);
    }
}
