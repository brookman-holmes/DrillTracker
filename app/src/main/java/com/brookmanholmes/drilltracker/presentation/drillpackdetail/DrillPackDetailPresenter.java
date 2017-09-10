package com.brookmanholmes.drilltracker.presentation.drillpackdetail;

import android.support.annotation.NonNull;

import com.brookmanholmes.drilltracker.data.repository.datasource.DataStoreFactory;
import com.brookmanholmes.drilltracker.domain.Drill;
import com.brookmanholmes.drilltracker.domain.interactor.DefaultObserver;
import com.brookmanholmes.drilltracker.domain.interactor.GetDrillPackDrillsList;
import com.brookmanholmes.drilltracker.presentation.base.Presenter;
import com.brookmanholmes.drilltracker.presentation.mapper.DrillModelDataMapper;

import java.util.List;

/**
 * Created by Brookman Holmes on 8/16/2017.
 */

class DrillPackDetailPresenter implements Presenter {
    private DrillPackDetailView view;
    private GetDrillPackDrillsList getDrillPackDrillsList;
    private DrillModelDataMapper mapper;

    DrillPackDetailPresenter() {
        getDrillPackDrillsList = new GetDrillPackDrillsList(DataStoreFactory.getDrillPackRepo());
        mapper = new DrillModelDataMapper();
    }

    DrillPackDetailPresenter(GetDrillPackDrillsList getDrillPackDrillsList, DrillModelDataMapper drillModelDataMapper) {
        this.getDrillPackDrillsList = getDrillPackDrillsList;
        mapper = drillModelDataMapper;
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
        getDrillPackDrillsList.dispose();
        this.view = null;
    }

    void getDrillPackDrillList(String id) {
        getDrillPackDrillsList.execute(new GetDrillPackDrillListObserver(),
                GetDrillPackDrillsList.Params.forDrillPack(id));
    }

    private class GetDrillPackDrillListObserver extends DefaultObserver<List<Drill>> {
        @Override
        public void onNext(List<Drill> drills) {
            view.renderDrillList(mapper.transform(drills));
        }
    }
}
