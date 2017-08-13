package com.brookmanholmes.drilltracker.presentation.purchasedrills;

import com.brookmanholmes.drilltracker.domain.DrillPack;
import com.brookmanholmes.drilltracker.domain.exception.DefaultErrorBundle;
import com.brookmanholmes.drilltracker.domain.exception.ErrorBundle;
import com.brookmanholmes.drilltracker.domain.interactor.DefaultObserver;
import com.brookmanholmes.drilltracker.domain.interactor.GetDrillPackList;
import com.brookmanholmes.drilltracker.presentation.exception.ErrorMessageFactory;
import com.brookmanholmes.drilltracker.presentation.mapper.DrillPackModelDataMapper;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;

import java.util.List;

/**
 * Created by Brookman Holmes on 8/9/2017.
 */
class PurchaseDrillsPresenter implements PurchaseDrillsContract {
    private final GetDrillPackList getDrillPackList;
    private final DrillPackModelDataMapper mapper = new DrillPackModelDataMapper();
    private PurchaseDrillsView view;

    PurchaseDrillsPresenter(GetDrillPackList getDrillPackList) {
        this.getDrillPackList = getDrillPackList;
    }

    /*
        Contract methods
     */

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        this.view = null;
        getDrillPackList.dispose();
    }

    @Override
    public void setView(PurchaseDrillsView view) {
        this.view = view;
    }

    @Override
    public void loadDrillsList(DrillModel.Type typeSelection) {
        hideViewRetry();
        showViewLoading();
        getDrillPackList();
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

    private void showDrillPackCollectionInView(List<DrillPack> drillPacks) {
        view.renderDrillPacks(mapper.transform(drillPacks));
    }

    private void getDrillPackList() {
        getDrillPackList.execute(new DrillPackObserver(), null);
    }

    private class DrillPackObserver extends DefaultObserver<List<DrillPack>> {
        @Override
        public void onNext(List<DrillPack> drillPacks) {
            showDrillPackCollectionInView(drillPacks);
            hideViewLoading();
        }

        @Override
        public void onError(Throwable e) {
            hideViewLoading();
            showErrorMessage(new DefaultErrorBundle((Exception) e));
            showViewRetry();
        }
    }
}
