package com.brookmanholmes.drilltracker.presentation.drillpackdetail;

import android.support.annotation.NonNull;

import com.brookmanholmes.drilltracker.data.repository.datasource.DataStoreFactory;
import com.brookmanholmes.drilltracker.domain.Drill;
import com.brookmanholmes.drilltracker.domain.interactor.DefaultObserver;
import com.brookmanholmes.drilltracker.domain.interactor.GetDrillPackDetails;
import com.brookmanholmes.drilltracker.presentation.base.Presenter;
import com.brookmanholmes.drilltracker.presentation.mapper.DrillModelDataMapper;

import java.util.List;

/**
 * Created by Brookman Holmes on 8/16/2017.
 */

class DrillPackDetailPresenter implements Presenter {
    private DrillPackDetailView view;
    private GetDrillPackDetails getDrillPackDetails;

    DrillPackDetailPresenter() {
        getDrillPackDetails = new GetDrillPackDetails(DataStoreFactory.getDrillPackRepo());
    }

    DrillPackDetailPresenter(GetDrillPackDetails getDrillPackDetails) {
        this.getDrillPackDetails = getDrillPackDetails;
    }

    void setView(@NonNull DrillPackDetailView view) {
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
        getDrillPackDetails.dispose();
        this.view = null;
    }

    void getDrillPackDrillList(String id) {
        getDrillPackDetails.execute(new GetDrillPackDrillListObserver(),
                GetDrillPackDetails.Params.forDrillPack(id));
    }

    private class GetDrillPackDrillListObserver extends DefaultObserver<List<Drill>> {
        @Override
        public void onNext(List<Drill> drills) {
            view.renderDrillList(DrillModelDataMapper.transform(drills));
        }
    }
}
