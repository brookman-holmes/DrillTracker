package com.brookmanholmes.drilltracker.presentation.drilldetail;

import androidx.annotation.NonNull;

import com.brookmanholmes.drilltracker.data.repository.datasource.DataStoreFactory;
import com.brookmanholmes.drilltracker.domain.Drill;
import com.brookmanholmes.drilltracker.domain.exception.DefaultErrorBundle;
import com.brookmanholmes.drilltracker.domain.exception.ErrorBundle;
import com.brookmanholmes.drilltracker.domain.interactor.DefaultObserver;
import com.brookmanholmes.drilltracker.domain.interactor.DeleteAttempt;
import com.brookmanholmes.drilltracker.domain.interactor.GetDrillDetails;
import com.brookmanholmes.drilltracker.presentation.exception.ErrorMessageFactory;
import com.brookmanholmes.drilltracker.presentation.mapper.DrillModelDataMapper;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;

import java.util.List;

/**
 * Created by Brookman Holmes on 7/11/2017.
 */
class DrillDetailsPresenter implements DrillDetailsContract {
    private static final String TAG = DrillDetailsPresenter.class.getName();
    private final GetDrillDetails getDrillDetailsUseCase;
    private final DeleteAttempt deleteAttemptUseCase;
    private DrillDetailsView view;
    private Drill drill;

    DrillDetailsPresenter() {
        getDrillDetailsUseCase = new GetDrillDetails(DataStoreFactory.getDrillRepo());
        deleteAttemptUseCase = new DeleteAttempt(DataStoreFactory.getDrillRepo());
    }

    private DrillDetailsPresenter(GetDrillDetails getDrillDetailsUseCase, DeleteAttempt deleteAttemptUseCase) {
        this.getDrillDetailsUseCase = getDrillDetailsUseCase;
        this.deleteAttemptUseCase = deleteAttemptUseCase;
    }

    @Override
    public void setView(@NonNull DrillDetailsView view) {
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
        getDrillDetailsUseCase.dispose();
        view = null;
    }

    @Override
    public void initialize(String drillId) {
        hideViewRetry();
        showViewLoading();
        getDrillDetails(drillId);
    }

    private void getDrillDetails(String drillId) {
        getDrillDetailsUseCase.execute(new DrillDetailsObserver(), GetDrillDetails.Params.forDrill(drillId));
    }

    private void showViewLoading() {
        this.view.showLoading();
    }

    private void hideViewLoading() {
        view.hideLoading();
    }

    private void showViewRetry() {
        view.showRetry();
    }

    private void hideViewRetry() {
        view.hideRetry();
    }

    private void showErrorMessage(ErrorBundle errorBundle) {
        String error = ErrorMessageFactory.create(errorBundle.getException());
        view.showError(error);
    }

    private void showDrillDetailsInView(Drill drill) {
        this.drill = drill;
        final DrillModel drillModel = DrillModelDataMapper.transform(drill);
        if (drillModel != null) {
            view.renderDrill(drillModel);
        }
    }

    @Override
    public void onUndoClicked() {
        deleteAttemptUseCase.execute(new DeleteAttemptObserver(), drill.getId());
    }

    @Override
    public void onAddAttemptClicked(String drillId, DrillModel.Type type, int maxScore,
                                    int targetScore, int cbPositions, int obPositions,
                                    int targetPositions, int selectedCbPosition,
                                    int selectedObPosition, int selectedTargetPosition,
                                    List<Integer> selectedPattern) {
        view.showAddAttemptDialog(drillId, type, maxScore, targetScore, cbPositions, obPositions,
                targetPositions, selectedCbPosition, selectedObPosition, selectedTargetPosition,
                selectedPattern);
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
