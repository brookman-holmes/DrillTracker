package com.brookmanholmes.drilltracker.presentation.drilldetail;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.presentation.base.BaseDialogFragment;

import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * TODO make this work
 */
public class AddPatternDialog extends BaseDialogFragment<AddPatternDialogPresenter> {

    private static final String PARAM_DRILL_ID = "param_drill_id";

    private Unbinder unbinder;

    public static AddPatternDialog newInstance(String drillId) {
        AddPatternDialog dialog = new AddPatternDialog();
        Bundle args = new Bundle();
        args.putString(PARAM_DRILL_ID, drillId);
        dialog.setArguments(args);

        return dialog;
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
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_pattern, null, false);
        unbinder = ButterKnife.bind(this, view);
        dialogBuilder.setView(view);
    }

    @Override
    protected String getTitle() {
        return "Create New Pattern";
    }

    @Override
    protected AddPatternDialogPresenter getPresenter() {
        return new AddPatternDialogPresenter();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        presenter.addNewPattern();
    }


    private String getDrillId() {
        return Objects.requireNonNull(getArguments()).getString(PARAM_DRILL_ID);
    }
}
