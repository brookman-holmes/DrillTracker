package com.brookmanholmes.drilltracker.presentation.base;

import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.data.executor.JobExecutor;
import com.brookmanholmes.drilltracker.data.repository.DrillDataRepository;
import com.brookmanholmes.drilltracker.data.repository.datasource.DrillDataStoreFactory;
import com.brookmanholmes.drilltracker.domain.executor.PostExecutionThread;
import com.brookmanholmes.drilltracker.domain.executor.ThreadExecutor;
import com.brookmanholmes.drilltracker.domain.repository.DrillRepository;

import butterknife.BindView;
import butterknife.Unbinder;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Brookman Holmes on 7/7/2017.
 */

public abstract class BaseFragment<T extends Presenter> extends Fragment {
    private final DrillRepository drillRepository = new DrillDataRepository(DrillDataStoreFactory.getInstance());
    private final ThreadExecutor threadExecutor = new JobExecutor();
    private final PostExecutionThread postExecutionThread = new PostExecutionThread() {
        @Override
        public Scheduler getScheduler() {
            return AndroidSchedulers.mainThread();
        }
    };
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    protected T presenter;
    protected Unbinder unbinder;


    public BaseFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.pause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }

    protected void showToastMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    protected ThreadExecutor getThreadExecutor() {
        return threadExecutor;
    }

    protected PostExecutionThread getPostExecutionThread() {
        return postExecutionThread;
    }

    protected DrillRepository getDrillRepository() {
        return drillRepository;
    }
}
