package com.brookmanholmes.drilltracker.presentation.base;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.brookmanholmes.drilltracker.data.entity.mapper.DrillEntityDataMapper;
import com.brookmanholmes.drilltracker.data.executor.JobExecutor;
import com.brookmanholmes.drilltracker.data.repository.DrillDataRepository;
import com.brookmanholmes.drilltracker.data.repository.datasource.DrillDataStoreFactory;
import com.brookmanholmes.drilltracker.domain.executor.PostExecutionThread;
import com.brookmanholmes.drilltracker.domain.executor.ThreadExecutor;
import com.brookmanholmes.drilltracker.domain.repository.DrillRepository;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Brookman Holmes on 7/7/2017.
 */

public class BaseFragment extends Fragment {
    protected void showToastMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    protected ThreadExecutor getThreadExecutor() {
        return new JobExecutor();
    }

    protected PostExecutionThread getPostExecutionThread() {
        return new PostExecutionThread() {
            @Override
            public Scheduler getScheduler() {
                return AndroidSchedulers.mainThread();
            }
        };
    }

    protected DrillRepository getDrillRepository() {
        return new DrillDataRepository(new DrillEntityDataMapper(), DrillDataStoreFactory.getInstance());
    }
}
