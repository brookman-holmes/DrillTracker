package com.brookmanholmes.drilltracker.data.repository.datasource;

import com.brookmanholmes.drilltracker.data.entity.DrillEntity;
import com.brookmanholmes.drilltracker.data.entity.DrillPackEntity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Objects;

import durdinapps.rxfirebase2.DataSnapshotMapper;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Observable;

/**
 * Created by Brookman Holmes on 8/9/2017.
 */
class FirebaseDrillPackDataStore implements DrillPackDataStore {
    private DatabaseReference drillsRef = FirebaseDatabase.getInstance().getReference();

    FirebaseDrillPackDataStore() {
        drillsRef = FirebaseDatabase.getInstance().getReference().child("iap");
        drillsRef.keepSynced(true);
    }

    @Override
    public Observable<List<DrillPackEntity>> drillPackEntityList() {
        return RxFirebaseDatabase.observeValueEvent(drillsRef.child("drill_packs"), DataSnapshotMapper.listOf(DrillPackEntity.class)).toObservable();
    }

    @Override
    public Observable<List<DrillEntity>> drillPackEntity(String id) {
        return RxFirebaseDatabase.observeValueEvent(drillsRef.child("drills").child(id), DataSnapshotMapper.listOf(DrillEntity.class)).toObservable();
    }

    @Override
    public void purchaseDrillPack(String sku) {
        DatabaseReference userRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());

        userRef.child("purchased_drill_packs")
                .child(sku)
                .setValue(true);
    }
}
