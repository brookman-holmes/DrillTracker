package com.brookmanholmes.drilltracker.presentation.addattempt;

import androidx.annotation.NonNull;

import com.brookmanholmes.drilltracker.data.repository.datasource.DataStoreFactory;
import com.brookmanholmes.drilltracker.domain.Drill;
import com.brookmanholmes.drilltracker.domain.interactor.AddAttempt;
import com.brookmanholmes.drilltracker.domain.interactor.DefaultObserver;
import com.brookmanholmes.drilltracker.domain.interactor.GetDrillDetails;
import com.brookmanholmes.drilltracker.presentation.base.Presenter;
import com.brookmanholmes.drilltracker.presentation.mapper.DrillModelDataMapper;
import com.brookmanholmes.drilltracker.presentation.model.AttemptModel;
import com.brookmanholmes.drilltracker.presentation.model.DataCollectionModel;
import com.brookmanholmes.drilltracker.presentation.model.DistanceResult;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;
import com.brookmanholmes.drilltracker.presentation.model.English;
import com.brookmanholmes.drilltracker.presentation.model.ShotResult;
import com.brookmanholmes.drilltracker.presentation.model.Speed;
import com.brookmanholmes.drilltracker.presentation.model.SpinResult;
import com.brookmanholmes.drilltracker.presentation.model.VSpin;

import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import timber.log.Timber;

/**
 * Created by Brookman Holmes on 7/28/2017.
 */
class AddAttemptPresenter implements Presenter {
    private AddAttemptView view;
    private final AddAttempt addAttempt;
    private final GetDrillDetails getDrillDetails;
    private DrillModel drill = null;

    AddAttemptPresenter() {
        addAttempt = new AddAttempt(DataStoreFactory.getDrillRepo());
        getDrillDetails = new GetDrillDetails(DataStoreFactory.getDrillRepo());
    }

    AddAttemptPresenter(AddAttempt addAttempt, GetDrillDetails getDrillDetails) {
        this.getDrillDetails = getDrillDetails;
        this.addAttempt = addAttempt;
    }

    public void setView(AddAttemptView addAttemptView) {
        view = addAttemptView;
        getDrillDetails.execute(new DrillDetailsObserver(), GetDrillDetails.Params.forDrill(view.getDrillId()));
    }

    public void addAttempt(String drillId, DistanceResult distanceResult, Speed speedRequired, VSpin spinRequired,
                           English englishRequired, SpinResult spinResult, SpinResult englishResult, SpinResult speedResult, SpinResult thicknessResult,
                           ShotResult shotResult, int targetPosition, int score,
                           int target, int obPosition, int cbPosition, List<Integer> pattern) {
        // sanitize data based one what is being collected
        if (!drill.getDataToCollect().contains(DataCollectionModel.COLLECT_SHOT_DATA)) {
            shotResult = ShotResult.NO_DATA;
        }
        if (!drill.getDataToCollect().contains(DataCollectionModel.COLLECT_SPEED_DATA)) {
            speedResult = SpinResult.NO_DATA;
            speedRequired = Speed.NO_DATA;
        }
        if (!drill.getDataToCollect().contains(DataCollectionModel.COLLECT_ENGLISH_DATA)) {
            englishResult = SpinResult.NO_DATA;
            englishRequired = English.NO_DATA;
        }
        if (!drill.getDataToCollect().contains(DataCollectionModel.COLLECT_SPIN_DATA)) {
            spinResult = SpinResult.NO_DATA;
            spinRequired = VSpin.NO_DATA;
        }
        if (!drill.getDataToCollect().contains(DataCollectionModel.COLLECT_TARGET_DATA)) {
            distanceResult = DistanceResult.NO_DATA;
        }
        AttemptModel attempt = new AttemptModel(
                speedRequired,
                englishRequired,
                spinRequired,
                distanceResult,
                spinResult,
                englishResult,
                speedResult,
                thicknessResult,
                shotResult,
                targetPosition,
                score,
                target,
                new Date(),
                obPosition,
                cbPosition,
                pattern

        );
        Timber.d("%s", attempt);
        addAttempt.execute(new DefaultObserver<>(),
                AddAttempt.Params.create(drillId,
                        attempt));
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        view = null;
        getDrillDetails.dispose();
        addAttempt.dispose();
    }

    public void setDataToCollect(@NonNull EnumSet<DataCollectionModel> dataToCollect) {
        view.setShowEnglishData(dataToCollect.contains(DataCollectionModel.COLLECT_ENGLISH_DATA));
        view.setShowShotData(dataToCollect.contains(DataCollectionModel.COLLECT_SHOT_DATA));
        view.setShowSpinData(dataToCollect.contains(DataCollectionModel.COLLECT_SPIN_DATA));
        view.setShowTargetDistanceData(dataToCollect.contains(DataCollectionModel.COLLECT_TARGET_DATA));
        view.setShowSpeedData(dataToCollect.contains(DataCollectionModel.COLLECT_SPEED_DATA));
    }

    private final class DrillDetailsObserver extends DefaultObserver<Drill> {
        @Override
        public void onNext(@NonNull Drill drill) {
            AddAttemptPresenter.this.setDrill(DrillModelDataMapper.transform(drill));
        }

        @Override
        public void onError(Throwable e) {
        }
    }

    private void setDrill(DrillModel drill) {
        this.drill = drill;
        view.loadDrillData(drill);
        view.setTargetPositions(drill.getTargetPositions());
        view.setCbPositions(drill.getCbPositions());
        view.setObPositions(drill.getObPositions());
        view.setShowEnglishData(drill.getDataToCollect().contains(DataCollectionModel.COLLECT_ENGLISH_DATA));
        view.setShowShotData(drill.getDataToCollect().contains(DataCollectionModel.COLLECT_SHOT_DATA));
        view.setShowSpinData(drill.getDataToCollect().contains(DataCollectionModel.COLLECT_SPIN_DATA));
        view.setShowTargetDistanceData(drill.getDataToCollect().contains(DataCollectionModel.COLLECT_TARGET_DATA));
        view.setShowSpeedData(drill.getDataToCollect().contains(DataCollectionModel.COLLECT_SPEED_DATA));
    }
}
