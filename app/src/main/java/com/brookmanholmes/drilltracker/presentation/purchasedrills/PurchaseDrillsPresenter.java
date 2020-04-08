package com.brookmanholmes.drilltracker.presentation.purchasedrills;

import androidx.annotation.NonNull;

import com.brookmanholmes.drilltracker.data.repository.datasource.DataStoreFactory;
import com.brookmanholmes.drilltracker.domain.Drill;
import com.brookmanholmes.drilltracker.domain.DrillPack;
import com.brookmanholmes.drilltracker.domain.exception.DefaultErrorBundle;
import com.brookmanholmes.drilltracker.domain.exception.ErrorBundle;
import com.brookmanholmes.drilltracker.domain.interactor.AddDrill;
import com.brookmanholmes.drilltracker.domain.interactor.DefaultObserver;
import com.brookmanholmes.drilltracker.domain.interactor.GetDrillPackList;
import com.brookmanholmes.drilltracker.domain.interactor.PurchaseDrillPack;
import com.brookmanholmes.drilltracker.presentation.exception.ErrorMessageFactory;
import com.brookmanholmes.drilltracker.presentation.mapper.DrillPackModelDataMapper;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;
import com.brookmanholmes.drilltracker.presentation.model.DrillPackModel;

import java.util.List;

/**
 * Created by Brookman Holmes on 8/9/2017.
 */
class PurchaseDrillsPresenter implements PurchaseDrillsContract {
    private final GetDrillPackList getDrillPackList;
    private final PurchaseDrillPack purchaseDrillPack;
    private final AddDrill addDrill;
    private final DrillPackModelDataMapper mapper = new DrillPackModelDataMapper();
    private PurchaseDrillsView view;

    PurchaseDrillsPresenter() {
        addDrill = new AddDrill(DataStoreFactory.getDrillRepo());
        getDrillPackList = new GetDrillPackList(DataStoreFactory.getDrillPackRepo());
        purchaseDrillPack = new PurchaseDrillPack(DataStoreFactory.getDrillPackRepo());
    }

    PurchaseDrillsPresenter(GetDrillPackList getDrillPackList, PurchaseDrillPack purchaseDrillPack, AddDrill addDrill) {
        this.addDrill = addDrill;
        this.getDrillPackList = getDrillPackList;
        this.purchaseDrillPack = purchaseDrillPack;
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
    public void setView(@NonNull PurchaseDrillsView view) {
        this.view = view;
    }

    @Override
    public void loadDrillsList() {
        hideViewRetry();
        showViewLoading();
        getDrillPackList();
    }

    @Override
    public void purchaseDrillPack(String sku) {
        purchaseDrillPack.execute(new PurchaseDrillPackObserver(), PurchaseDrillPack.Params.forDrillPack(sku));
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
        String errorMessage = ErrorMessageFactory.create(errorBundle.getException());
        this.view.showError(errorMessage);
    }

    private void showDrillPackCollectionInView(List<DrillPackModel> drillPacks) {
        view.renderDrillPacks(drillPacks);
    }

    private void loadSkusInView(List<String> skus) {
        view.loadInventory(skus);
    }

    private void getDrillPackList() {
        getDrillPackList.execute(new DrillPackObserver(), null);
    }

    private class PurchaseDrillPackObserver extends DefaultObserver<List<Drill>> {
        @Override
        public void onNext(List<Drill> drills) {
            for (Drill drill : drills) {
                addDrill.execute(
                        new DefaultObserver<>(),
                        AddDrill.Params.create(
                                drill.getId(),
                                drill.getName(),
                                drill.getDescription(),
                                drill.getImageUrl(),
                                DrillModel.Type.values()[drill.getType().ordinal()],
                                drill.getMaxScore(),
                                drill.getDefaultTargetScore(),
                                drill.getObPositions(),
                                drill.getCbPositions(),
                                drill.getTargetPositions(),
                                true
                        )
                );
            }
            onComplete();
            dispose();
        }

        @Override
        public void onError(Throwable e) {
            hideViewLoading();
            showErrorMessage(new DefaultErrorBundle((Exception) e));
            showViewRetry();
        }
    }

    private class DrillPackObserver extends DefaultObserver<List<DrillPack>> {
        @Override
        public void onNext(List<DrillPack> drillPacks) {
            List<DrillPackModel> models = mapper.transform(drillPacks);
            List<String> skus = mapper.getSkus(models);

            showDrillPackCollectionInView(models);
            loadSkusInView(skus);
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
