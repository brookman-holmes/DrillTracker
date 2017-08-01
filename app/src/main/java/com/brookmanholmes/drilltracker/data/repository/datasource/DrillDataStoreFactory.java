package com.brookmanholmes.drilltracker.data.repository.datasource;

import com.brookmanholmes.drilltracker.data.entity.DrillEntity;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.UploadTask;

import java.util.List;

import durdinapps.rxfirebase2.RxFirebaseAuth;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by Brookman Holmes on 7/14/2017.
 */

public class DrillDataStoreFactory implements DrillDataStore {
    DrillDataStore dataStore;

    public static DrillDataStore getInstance() {
        return new FirebaseDataStore(FirebaseAuth.getInstance().signInAnonymously().getResult().getUser().getUid());
    }

    private DrillDataStoreFactory(DrillDataStore dataStore) {
        this.dataStore = dataStore;
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
    public Observable<DrillEntity>  removeLastAttempt(String id) {
        return dataStore.removeLastAttempt(id);
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
