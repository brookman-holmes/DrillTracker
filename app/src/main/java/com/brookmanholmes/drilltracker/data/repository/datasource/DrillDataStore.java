package com.brookmanholmes.drilltracker.data.repository.datasource;

import com.brookmanholmes.drilltracker.data.entity.DrillEntity;
import com.google.firebase.storage.UploadTask;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;

/**
 * Created by Brookman Holmes on 7/9/2017.
 */

public interface DrillDataStore {
    Observable<DrillEntity> addDrill(DrillEntity entity);
    Observable<List<DrillEntity>> drillEntityList();
    Observable<DrillEntity> drillEntity(final String id);
    Observable<DrillEntity> addAttempt(final String id, DrillEntity.AttemptEntity attempt);
    Observable<DrillEntity> removeLastAttempt(final String id);
    Maybe<UploadTask.TaskSnapshot> uploadImage(String id, byte[] image);
    Observable<DrillEntity> updateDrill(DrillEntity entity);
    void deleteDrill(String drillId);
}
