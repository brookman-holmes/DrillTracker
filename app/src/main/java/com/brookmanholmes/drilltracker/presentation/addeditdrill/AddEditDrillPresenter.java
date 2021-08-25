package com.brookmanholmes.drilltracker.presentation.addeditdrill;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.brookmanholmes.drilltracker.data.repository.datasource.DataStoreFactory;
import com.brookmanholmes.drilltracker.domain.Drill;
import com.brookmanholmes.drilltracker.domain.interactor.AddDrill;
import com.brookmanholmes.drilltracker.domain.interactor.DefaultObserver;
import com.brookmanholmes.drilltracker.domain.interactor.DrillParams;
import com.brookmanholmes.drilltracker.domain.interactor.GetDrillDetails;
import com.brookmanholmes.drilltracker.domain.interactor.UpdateDrill;
import com.brookmanholmes.drilltracker.presentation.mapper.DrillModelDataMapper;
import com.brookmanholmes.drilltracker.presentation.model.DataCollectionModel;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;
import com.brookmanholmes.drilltracker.presentation.model.Type;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import timber.log.Timber;

/**
 * Created by Brookman Holmes on 7/23/2017.
 */

class AddEditDrillPresenter implements AddEditDrillContract {
    private static final String TAG = AddEditDrillPresenter.class.getName();

    private AddEditDrillView view;

    private String drillName;
    private String drillDescription;
    private String imageUrl;
    private int maximumScore;
    private int targetScore;
    private int obPositions;
    private int cbPositions;
    private int targetPositions;
    private byte[] image;
    private Type type;
    private boolean purchased;
    private final List<DataCollectionModel> dataToCollect = new ArrayList<>();


    private final AddDrill addDrill;
    private final GetDrillDetails getDrillDetails;
    private final UpdateDrill updateDrill;
    private final String drillId;

    private AddEditDrillPresenter(AddDrill addDrill, GetDrillDetails getDrillDetails, UpdateDrill updateDrill, @Nullable String drillId) {
        this.addDrill = addDrill;
        this.getDrillDetails = getDrillDetails;
        this.drillId = drillId;
        this.updateDrill = updateDrill;
    }

    AddEditDrillPresenter(@Nullable String drillId) {
        addDrill = new AddDrill(DataStoreFactory.getDrillRepo());
        getDrillDetails = new GetDrillDetails(DataStoreFactory.getDrillRepo());
        updateDrill = new UpdateDrill(DataStoreFactory.getDrillRepo());
        this.drillId = drillId;
    }

    @Override
    public void setView(@NonNull AddEditDrillView view) {
        this.view = view;
        if (drillId != null) {
            getDrillDetails.execute(new GetDrillObserver(), GetDrillDetails.Params.forDrill(drillId));
        }
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
        addDrill.dispose();
        updateDrill.dispose();
        getDrillDetails.dispose();
    }

    @Override
    public void setDrillName(String drillName) {
        this.drillName = drillName;
        this.view.isDrillComplete(isDrillComplete());
    }

    @Override
    public void setDrillDescription(String drillDescription) {
        this.drillDescription = drillDescription;
        this.view.isDrillComplete(isDrillComplete());
    }

    private boolean isDrillComplete() {
        return !TextUtils.isEmpty(drillName) &&
                !TextUtils.isEmpty(drillDescription) &&
                (image != null || imageUrl != null);
    }

    @Override
    public void setMaximumScore(int maximumScore) {
        this.maximumScore = maximumScore;
        this.view.isDrillComplete(isDrillComplete());
    }

    @Override
    public void setTargetScore(int targetScore) {
        this.targetScore = targetScore;
        this.view.isDrillComplete(isDrillComplete());
    }

    @Override
    public void setCbPositions(int positions) {
        this.cbPositions = positions;
    }

    @Override
    public void setObPositions(int positions) {
        this.obPositions = positions;
    }

    @Override
    public void setTargetPositions(int targetPositions) {
        this.targetPositions = targetPositions;
    }

    @Override
    public void setImage(byte[] image) {
        this.image = image;
        this.imageUrl = null;
        this.view.isDrillComplete(isDrillComplete());
    }

    @Override
    public void saveDrill() {
        DrillParams params = new DrillParams(
                drillName,
                drillId,
                drillDescription,
                maximumScore,
                EnumSet.copyOf(dataToCollect),
                targetScore,
                obPositions,
                cbPositions,
                targetPositions,
                type.ordinal(),
                image,
                purchased,
                imageUrl
        );
        if (drillId != null) {
                updateDrill.execute(new EditDrillObserver(), params);
        } else {
            addDrill.execute(new AddDrillObserver(), params);
        }
    }

    @Override
    public void setDrillType(Type type) {
        Timber.i(type.name());
        this.type = type;
        view.isDrillComplete(isDrillComplete());
    }

    private void populateView(DrillModel model) {
        view.loadDrillData(model);
    }

    public void enableShotDataCollection(boolean enabled) {
        if (enabled) {
            dataToCollect.add(DataCollectionModel.COLLECT_SHOT_DATA);
        } else {
            dataToCollect.remove(DataCollectionModel.COLLECT_SHOT_DATA);
        }
    }

    public void enableSpeedDataCollection(boolean enabled) {
        if (enabled) {
            dataToCollect.add(DataCollectionModel.COLLECT_SPEED_DATA);
        } else {
            dataToCollect.remove(DataCollectionModel.COLLECT_SPEED_DATA);
        }
    }

    public void enableVSpinDataCollection(boolean enabled) {
        if (enabled) {
            dataToCollect.add(DataCollectionModel.COLLECT_SPIN_DATA);
        } else {
            dataToCollect.remove(DataCollectionModel.COLLECT_SPIN_DATA);
        }
    }

    public void enableDistanceDataCollection(boolean enabled) {
        if (enabled) {
            dataToCollect.add(DataCollectionModel.COLLECT_TARGET_DATA);
        } else {
            dataToCollect.remove(DataCollectionModel.COLLECT_TARGET_DATA);
        }
    }

    public void enableEnglishDataCollection(boolean enabled) {
        if (enabled) {
            dataToCollect.add(DataCollectionModel.COLLECT_ENGLISH_DATA);
        } else {
            dataToCollect.remove(DataCollectionModel.COLLECT_ENGLISH_DATA);
        }
    }

    private class EditDrillObserver extends DefaultObserver<Drill> {
        @Override
        public void onNext(Drill drill) {
            view.finish();
        }
    }

    private class AddDrillObserver extends DefaultObserver<Drill> {
        @Override
        public void onNext(Drill drill) {
            view.showDrillDetailsView(DrillModelDataMapper.transform(drill));
        }
    }

    private class GetDrillObserver extends DefaultObserver<Drill> {
        @Override
        public void onNext(Drill drill) {
            DrillModel model = DrillModelDataMapper.transform(drill);
            purchased = model.getPurchased();
            imageUrl = model.getImageUrl();
            cbPositions = model.getCbPositions();
            obPositions = model.getObPositions();
            targetPositions = model.getTargetPositions();
            imageUrl = model.getImageUrl();
            drillName = model.getName();
            drillDescription = model.getDescription();
            targetScore = model.getDefaultTargetScore();
            maximumScore = model.getMaxScore();
            type = model.getDrillType();

            image = null;
            populateView(model);
        }
    }
}
