package com.brookmanholmes.drilltracker.presentation.drilldetail;

import android.support.annotation.NonNull;
import android.util.Log;

import com.brookmanholmes.drilltracker.domain.Drill;
import com.brookmanholmes.drilltracker.domain.exception.DefaultErrorBundle;
import com.brookmanholmes.drilltracker.domain.exception.ErrorBundle;
import com.brookmanholmes.drilltracker.domain.interactor.DefaultObserver;
import com.brookmanholmes.drilltracker.domain.interactor.DeleteAttempt;
import com.brookmanholmes.drilltracker.domain.interactor.GetDrillDetails;
import com.brookmanholmes.drilltracker.presentation.base.Presenter;
import com.brookmanholmes.drilltracker.presentation.exception.ErrorMessageFactory;
import com.brookmanholmes.drilltracker.presentation.mapper.DrillModelDataMapper;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;

/**
 * Created by Brookman Holmes on 7/11/2017.
 */
// TODO: 7/27/2017 add in ability to edit the drill
class DrillDetailsPresenter implements Presenter {
    private static final String TAG = DrillDetailsPresenter.class.getName();
    private final GetDrillDetails getDrillDetailsUseCase;
    private final DeleteAttempt deleteAttemptUseCase;
    private final DrillModelDataMapper drillModelDataMapper;
    private DrillDetailView drillDetailView;
    private DrillModel model;

    DrillDetailsPresenter(GetDrillDetails getDrillDetailsUseCase, DeleteAttempt deleteAttemptUseCase, DrillModelDataMapper drillModelDataMapper) {
        this.getDrillDetailsUseCase = getDrillDetailsUseCase;
        this.drillModelDataMapper = drillModelDataMapper;
        this.deleteAttemptUseCase = deleteAttemptUseCase;
    }

    public void setView(@NonNull DrillDetailView view) {
        this.drillDetailView = view;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        getDrillDetailsUseCase.dispose();
        drillDetailView = null;
    }

    void initialize(String drillId) {
        hideViewRetry();
        showViewLoading();
        getDrillDetails(drillId);

    }

    private void getDrillDetails(String drillId) {
        getDrillDetailsUseCase.execute(new DrillDetailsObserver(), GetDrillDetails.Params.forDrill(drillId));
    }

    private void showViewLoading() {
        this.drillDetailView.showLoading();
    }

    private void hideViewLoading() {
        drillDetailView.hideLoading();
    }

    private void showViewRetry() {
        drillDetailView.showRetry();
    }

    private void hideViewRetry() {
        drillDetailView.hideRetry();
    }

    private void showErrorMessage(ErrorBundle errorBundle) {
        String error = ErrorMessageFactory.create(drillDetailView.context(), errorBundle.getException());
        drillDetailView.showError(error);
    }

    private void showDrillDetailsInView(Drill drill) {
        final DrillModel drillModel = drillModelDataMapper.transform(drill);
        if (drillModel != null) {
            drillDetailView.renderDrill(drillModel);
            this.model = drillModel;
        }
        else Log.w(TAG, "showDrillDetailsInView: drill model is null and drill == null ? " + (drill == null));
    }

    void showAddAttemptView() {
        drillDetailView.showAddAttemptView();
    }

    void onDrillImageClicked() {
        drillDetailView.showDrillImageFullScreen(model);
    }

    void onEditClicked() {
        drillDetailView.showEditDrillView(model.id);
    }

    void onUndoClicked() {
        deleteAttemptUseCase.execute(new DeleteAttemptObserver(), model.id);
    }

    private final class DrillDetailsObserver extends DefaultObserver<Drill> {
        @Override
        public void onNext(Drill drill) {
            DrillDetailsPresenter.this.showDrillDetailsInView(drill);
            DrillDetailsPresenter.this.hideViewLoading();
        }

        @Override
        public void onError(Throwable e) {
            DrillDetailsPresenter.this.hideViewLoading();
            DrillDetailsPresenter.this.showErrorMessage(new DefaultErrorBundle((Exception)e));
            DrillDetailsPresenter.this.showViewRetry();
        }
    }

    private final class DeleteAttemptObserver extends DefaultObserver<Void> {
        @Override
        public void onComplete() {
            deleteAttemptUseCase.dispose();
        }
    }
}
