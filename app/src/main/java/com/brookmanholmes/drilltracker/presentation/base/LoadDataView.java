package com.brookmanholmes.drilltracker.presentation.base;

import android.content.Context;

/**
 * Created by Brookman Holmes on 7/7/2017.
 */

public interface LoadDataView {
    void showLoading();
    void hideLoading();
    void showRetry();
    void hideRetry();
    void showError(String message);
    Context context();
}
