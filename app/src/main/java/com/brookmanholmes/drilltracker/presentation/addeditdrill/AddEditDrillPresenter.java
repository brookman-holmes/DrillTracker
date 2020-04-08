package com.brookmanholmes.drilltracker.presentation.addeditdrill;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.brookmanholmes.drilltracker.data.repository.datasource.DataStoreFactory;
import com.brookmanholmes.drilltracker.domain.Drill;
import com.brookmanholmes.drilltracker.domain.interactor.AddDrill;
import com.brookmanholmes.drilltracker.domain.interactor.DefaultObserver;
import com.brookmanholmes.drilltracker.domain.interactor.GetDrillDetails;
import com.brookmanholmes.drilltracker.domain.interactor.UpdateDrill;
import com.brookmanholmes.drilltracker.presentation.mapper.DrillModelDataMapper;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;

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
    private DrillModel.Type type;
    private boolean purchased;

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
        if (drillId != null) {
            if (imageUrl != null) {
                updateDrill.execute(new EditDrillObserver(), UpdateDrill.Params.create(
                        drillName,
                        drillDescription,
                        drillId,
                        imageUrl,
                        type,
                        maximumScore,
                        targetScore,
                        obPositions,
                        cbPositions,
                        targetPositions));
            } else {
                updateDrill.execute(new EditDrillObserver(), UpdateDrill.Params.create(
                        drillName,
                        drillDescription,
                        drillId,
                        image,
                        type,
                        maximumScore,
                        targetScore,
                        obPositions,
                        cbPositions,
                        targetPositions));
            }
        } else {
            addDrill.execute(new AddDrillObserver(), AddDrill.Params.create(
                    drillName,
                    drillDescription,
                    image,
                    type,
                    maximumScore,
                    targetScore,
                    obPositions,
                    cbPositions,
                    targetPositions,
                    purchased));
        }
    }

    @Override
    public void setDrillType(DrillModel.Type type) {
        this.type = type;
        view.isDrillComplete(isDrillComplete());
    }

    private void populateView(DrillModel model) {
        view.loadDrillData(model);
    }

    private class EditDrillObserver extends DefaultObserver<Drill> {
        @Override
        public void onNext(Drill drill) {
            view.finish();
            onComplete();
            dispose();
        }
    }

    private class AddDrillObserver extends DefaultObserver<Drill> {
        @Override
        public void onNext(Drill drill) {
            view.showDrillDetailsView(DrillModelDataMapper.transform(drill));
            onComplete();
            dispose();
        }
    }

    private class GetDrillObserver extends DefaultObserver<Drill> {
        @Override
        public void onNext(Drill drill) {
            DrillModel model = DrillModelDataMapper.transform(drill);
            purchased = model.purchased;
            imageUrl = model.imageUrl;
            cbPositions = model.cbPositions;
            obPositions = model.obPositions;
            targetPositions = model.targetPositions;
            imageUrl = model.imageUrl;
            drillName = model.name;
            drillDescription = model.description;
            targetScore = model.defaultTargetScore;
            maximumScore = model.maxScore;
            type = model.drillType;

            image = null;
            populateView(model);
            onComplete();
            dispose();
        }
    }
}
