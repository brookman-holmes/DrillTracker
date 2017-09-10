package com.brookmanholmes.drilltracker.presentation.deletedrill;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.presentation.base.BaseDialogFragment;

/**
 * Created by Brookman Holmes on 7/28/2017.
 */

public class DeleteDrillDialog extends BaseDialogFragment<DeleteDrillDialogPresenter> {
    private static final String PARAM_DRILL_ID = "param_drill_id";

    public static DeleteDrillDialog newInstance(String drillId) {
        DeleteDrillDialog dialog = new DeleteDrillDialog();
        Bundle args = new Bundle();
        args.putString(PARAM_DRILL_ID, drillId);
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    protected boolean hasTitle() {
        return true;
    }

    @Override
    protected void setDialogBuilderView(AlertDialog.Builder dialogBuilder) {

    }

    @Override
    protected String getTitle() {
        return getString(R.string.confirm_delete_drill);
    }

    private String getDrillId() {
        return getArguments().getString(PARAM_DRILL_ID);
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        presenter.deleteDrill(getDrillId());
    }

    @Override
    protected DeleteDrillDialogPresenter getPresenter() {
        return new DeleteDrillDialogPresenter();
    }
}
