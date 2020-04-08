package com.brookmanholmes.drilltracker.presentation.addattempt;

import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.brookmanholmes.drilltracker.presentation.base.BaseDialogFragment;

/**
 * Created by brookman on 1/29/18.
 */

public class UnimplementedAttemptDialog extends BaseDialogFragment<UnimplementedAttemptDialog.Presenter> {
    @Override
    protected boolean hasTitle() {
        return false;
    }

    @Override
    protected void setDialogBuilderView(AlertDialog.Builder dialogBuilder) {
        dialogBuilder.setMessage("This type of drill has not yet been finished. Please test a different type.");
    }

    @Override
    protected String getTitle() {
        return "";
    }

    @Override
    protected Presenter getPresenter() {
        return new Presenter();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

    }

    @Override
    protected boolean hasNegativeButton() {
        return false;
    }

    public static class Presenter implements com.brookmanholmes.drilltracker.presentation.base.Presenter {
        @Override
        public void resume() {

        }

        @Override
        public void pause() {

        }

        @Override
        public void destroy() {

        }
    }
}
