package com.brookmanholmes.drilltracker.presentation.deletedrill;

import com.brookmanholmes.drilltracker.data.repository.datasource.DataStoreFactory;
import com.brookmanholmes.drilltracker.domain.interactor.DefaultObserver;
import com.brookmanholmes.drilltracker.domain.interactor.DeleteDrill;
import com.brookmanholmes.drilltracker.presentation.base.Presenter;

/**
 * Created by Brookman Holmes on 7/28/2017.
 */

class DeleteDrillDialogPresenter implements Presenter {
    private final DeleteDrill deleteDrill;

    DeleteDrillDialogPresenter() {
        deleteDrill = new DeleteDrill(DataStoreFactory.getDrillRepo());
    }

    DeleteDrillDialogPresenter(DeleteDrill deleteDrill) {
        this.deleteDrill = deleteDrill;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    void deleteDrill(String id) {
        this.deleteDrill.execute(new DeleteDrillObserver(), id);
    }

    private static final class DeleteDrillObserver extends DefaultObserver<Boolean> {
        @Override
        public void onNext(Boolean aBoolean) {
            super.onNext(aBoolean);
            onComplete();
            dispose();
        }
    }
}
