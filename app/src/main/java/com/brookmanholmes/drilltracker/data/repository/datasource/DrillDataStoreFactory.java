package com.brookmanholmes.drilltracker.data.repository.datasource;

import com.brookmanholmes.drilltracker.data.entity.DrillEntity;
import com.google.firebase.storage.UploadTask;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Observable;

/**
 * Created by Brookman Holmes on 7/14/2017.
 */

public class DrillDataStoreFactory implements DrillDataStore {
    private static DrillDataStore dataStore;

    public static DrillDataStore getInstance() {
        if (dataStore == null)
            dataStore = new FirebaseDataStore();

        return dataStore;
    }

    @Override
    public Observable<DrillEntity> addDrill(DrillEntity entity) {
        return dataStore.addDrill(entity);
    }

    @Override
    public Observable<List<DrillEntity>> drillEntityList() {
        return dataStore.drillEntityList();
    }

    @Override
    public Observable<DrillEntity> drillEntity(String id) {
        return dataStore.drillEntity(id);
    }

    @Override
    public Observable<DrillEntity>  addAttempt(String id, DrillEntity.AttemptEntity attempt) {
        return dataStore.addAttempt(id, attempt);
    }

    @Override
    public void removeLastAttempt(String id) {
        dataStore.removeLastAttempt(id);
    }

    @Override
    public Maybe<UploadTask.TaskSnapshot> uploadImage(String id, byte[] image) {
        return dataStore.uploadImage(id, image);
    }

    @Override
    public Observable<DrillEntity> updateDrill(DrillEntity entity) {
        return dataStore.updateDrill(entity);
    }

    @Override
    public void deleteDrill(String drillId) {
        dataStore.deleteDrill(drillId);
    }
}
