package com.brookmanholmes.drilltracker.presentation.base;

import android.widget.Toast;

import androidx.fragment.app.Fragment;

import butterknife.Unbinder;

/**
 * Created by Brookman Holmes on 7/7/2017.
 */

public abstract class BaseFragment<T extends Presenter> extends Fragment {
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
}