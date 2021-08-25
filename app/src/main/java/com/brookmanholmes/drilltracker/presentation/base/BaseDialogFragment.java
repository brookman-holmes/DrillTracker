package com.brookmanholmes.drilltracker.presentation.base;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.brookmanholmes.drilltracker.R;

/**
 * Created by Brookman Holmes on 7/28/2017.
 */

public abstract class BaseDialogFragment<T extends Presenter> extends DialogFragment implements DialogInterface.OnClickListener {

    protected T presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = getPresenter();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireContext(), getDialogTheme());

        if (hasTitle())
            dialogBuilder.setTitle(getTitle());

        setDialogBuilderView(dialogBuilder);
        dialogBuilder.setPositiveButton(android.R.string.ok, this);
        if (hasNegativeButton())
            dialogBuilder.setNegativeButton(android.R.string.cancel, null);
        return dialogBuilder.create();
    }

    @StyleRes
    private int getDialogTheme() {
        return R.style.AlertDialogTheme;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.destroy();
    }

    protected abstract boolean hasTitle();
    protected abstract void setDialogBuilderView(AlertDialog.Builder dialogBuilder);
    protected abstract String getTitle();
    protected abstract T getPresenter();

    protected boolean hasNegativeButton() {
        return true;
    }
}
