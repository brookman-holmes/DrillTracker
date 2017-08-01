package com.brookmanholmes.drilltracker.presentation.drills;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.brookmanholmes.drilltracker.domain.Drill;
import com.brookmanholmes.drilltracker.domain.exception.DefaultErrorBundle;
import com.brookmanholmes.drilltracker.domain.exception.ErrorBundle;
import com.brookmanholmes.drilltracker.domain.interactor.AddDrill;
import com.brookmanholmes.drilltracker.domain.interactor.DefaultObserver;
import com.brookmanholmes.drilltracker.domain.interactor.DeleteDrill;
import com.brookmanholmes.drilltracker.domain.interactor.GetDrillList;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;
import com.brookmanholmes.drilltracker.presentation.exception.ErrorMessageFactory;
import com.brookmanholmes.drilltracker.presentation.mapper.DrillModelDataMapper;
import com.brookmanholmes.drilltracker.presentation.base.Presenter;

import java.util.Collection;
import java.util.List;

/**
 * Created by Brookman Holmes on 7/7/2017.
 */

class DrillsListPresenter implements Presenter {
    private static final String TAG = DrillsListPresenter.class.getName();

    private DrillsListView view;

    private final GetDrillList getDrillListUseCase;
    private final DrillModelDataMapper drillModelDataMapper;

    public DrillsListPresenter(GetDrillList getDrillListUseCase, DrillModelDataMapper drillModelDataMapper) {
        this.getDrillListUseCase = getDrillListUseCase;
        this.drillModelDataMapper = drillModelDataMapper;
    }

    public void setView(@NonNull DrillsListView view) {
        this.view = view;
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        this.getDrillListUseCase.dispose();
        this.view = null;
    }

    public void initialize(DrillModel.Type filter) {
        this.loadDrillsList(filter);
    }

    public void loadDrillsList(DrillModel.Type filter) {
        this.hideViewRetry();
        this.showViewLoading();
        this.getDrillsList(filter);
    }

    public void onDrillClicked(DrillModel drillModel) {
        this.view.viewDrill(drillModel);
    }

    private void showViewLoading() {
        this.view.showLoading();
    }

    private void hideViewLoading() {
        this.view.hideLoading();
    }

    private void showViewRetry() {
        this.view.hideRetry();
    }

    private void hideViewRetry() {
        this.view.hideRetry();
    }

    private void showErrorMessage(ErrorBundle errorBundle) {
        String errorMessage = ErrorMessageFactory.create(this.view.context(), errorBundle.getException());
        this.view.showError(errorMessage);
    }

    private void showDrillCollectionInView(List<Drill> drillCollection) {
        final List<DrillModel> drillModelCollection = this.drillModelDataMapper.transform(drillCollection);
        this.view.renderUserList(drillModelCollection);
    }

    private void getDrillsList(DrillModel.Type filter) {
        this.getDrillListUseCase.execute(new DrillListObserver(), GetDrillList.Params.newInstance(filter));
    }

    void showDeleteConfirmation(DrillModel drillModel) {
        this.view.showDeleteConfirmation(drillModel);
    }

    private final class DrillListObserver extends DefaultObserver<List<Drill>> {
        @Override
        public void onNext(List<Drill> drills) {
            DrillsListPresenter.this.showDrillCollectionInView(drills);
            DrillsListPresenter.this.hideViewLoading();
        }

        @Override
        public void onError(Throwable e) {
            DrillsListPresenter.this.hideViewLoading();
            DrillsListPresenter.this.showErrorMessage(new DefaultErrorBundle((Exception)e));
            DrillsListPresenter.this.showViewRetry();
        }
    }
}
