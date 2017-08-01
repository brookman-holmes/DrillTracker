package com.brookmanholmes.drilltracker.data.repository.datasource;

import android.util.Log;

import com.brookmanholmes.drilltracker.data.entity.DrillEntity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;

import durdinapps.rxfirebase2.DataSnapshotMapper;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import durdinapps.rxfirebase2.RxFirebaseStorage;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by Brookman Holmes on 7/14/2017.
 */
class FirebaseDataStore implements DrillDataStore {
    private static final String TAG = FirebaseDataStore.class.getName();
    private static boolean moveComplete = false;
    private DatabaseReference ref;

    FirebaseDataStore(String userId) {
        ref = FirebaseDatabase.getInstance().getReference().child(userId).child("drills");

        DatabaseReference oldData = FirebaseDatabase.getInstance().getReference().child("test");

        oldData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!moveComplete)
                    ref.getParent().setValue(dataSnapshot.getValue()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            moveComplete = true;
                        }
                    });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public Observable<DrillEntity> addDrill(DrillEntity entity) {
        String key = ref.push().getKey();
        entity.id = key;
        ref.child(key).setValue(entity);
        return RxFirebaseDatabase.observeValueEvent(ref.child(key), DrillEntity.class).toObservable();
    }

    @Override
    public Observable<List<DrillEntity>> drillEntityList() {
        return RxFirebaseDatabase.observeValueEvent(ref, DataSnapshotMapper.listOf(DrillEntity.class)).toObservable();
    }

    @Override
    public Observable<DrillEntity> drillEntity(String id) {
        return RxFirebaseDatabase.observeValueEvent(ref.child(id), DrillEntity.class).toObservable();
    }

    @Override
    public Observable<DrillEntity> addAttempt(String id, DrillEntity.AttemptEntity attempt) {
        String attemptId = ref.child(id).child("attempts").push().getKey();
        ref.child(id).child("attempts").child(attemptId).setValue(attempt);
        return drillEntity(id);
    }

    @Override
    public Observable<DrillEntity> removeLastAttempt(String id) {
        ref.child(id).orderByKey().limitToLast(1).getRef().removeValue();
        return drillEntity(id);
    }

    @Override
    public Maybe<UploadTask.TaskSnapshot> uploadImage(String id, byte[] image) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("test").child(id).child("image.png");
        return RxFirebaseStorage.putBytes(storageRef, image);
    }

    @Override
    public Observable<DrillEntity> updateDrill(DrillEntity entity) {
        ref.child(entity.id).setValue(entity);
        return drillEntity(entity.id);
    }

    @Override
    public void deleteDrill(String drillId) {
        FirebaseStorage.getInstance().getReference().child("test").child(drillId).child("image.png").delete();
        ref.child(drillId).setValue(null);
    }
}
