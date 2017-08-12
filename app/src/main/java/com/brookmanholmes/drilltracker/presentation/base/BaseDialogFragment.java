package com.brookmanholmes.drilltracker.presentation.base;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.data.executor.JobExecutor;
import com.brookmanholmes.drilltracker.data.repository.DrillDataRepository;
import com.brookmanholmes.drilltracker.data.repository.datasource.DataStoreFactory;
import com.brookmanholmes.drilltracker.domain.executor.PostExecutionThread;
import com.brookmanholmes.drilltracker.domain.executor.ThreadExecutor;
import com.brookmanholmes.drilltracker.domain.repository.DrillRepository;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Brookman Holmes on 7/28/2017.
 */

public abstract class BaseDialogFragment<T extends Presenter> extends DialogFragment implements DialogInterface.OnClickListener {
    protected AlertDialog.Builder dialogBuilder;
    protected ThreadExecutor threadExecutor = new JobExecutor();
    protected DrillRepository drillRepository = new DrillDataRepository(DataStoreFactory.getDrillDataStore());
    protected PostExecutionThread postExecutionThread = new PostExecutionThread() {
        @Override
        public Scheduler getScheduler() {
            return AndroidSchedulers.mainThread();
        }
    };

    protected T presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = getPresenter();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialogBuilder = new AlertDialog.Builder(getContext(), getDialogTheme());

        if (hasTitle())
            dialogBuilder.setTitle(getTitle());

        setDialogBuilderView(dialogBuilder);
        dialogBuilder.setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, this);
        return dialogBuilder.create();
    }

    protected @StyleRes int getDialogTheme() {
        return R.style.AlertDialogTheme;
    }

    protected abstract boolean hasTitle();
    protected abstract void setDialogBuilderView(AlertDialog.Builder dialogBuilder);
    protected abstract String getTitle();
    protected abstract T getPresenter();
}
