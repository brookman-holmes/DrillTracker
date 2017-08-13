package com.brookmanholmes.drilltracker.presentation.base;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.brookmanholmes.drilltracker.data.repository.DrillDataRepository;
import com.brookmanholmes.drilltracker.data.repository.datasource.DataStoreFactory;
import com.brookmanholmes.drilltracker.domain.repository.DrillRepository;

import butterknife.Unbinder;

/**
 * Created by Brookman Holmes on 7/7/2017.
 */

public abstract class BaseFragment<T extends Presenter> extends Fragment {
    private final DrillRepository drillRepository = new DrillDataRepository(DataStoreFactory.getDrillDataStore());
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

    protected DrillRepository getDrillRepository() {
        return drillRepository;
    }
}
