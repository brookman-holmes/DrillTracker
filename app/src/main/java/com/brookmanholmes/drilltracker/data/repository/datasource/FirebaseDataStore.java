package com.brookmanholmes.drilltracker.data.repository.datasource;

import android.util.Log;

import androidx.annotation.NonNull;

import com.brookmanholmes.drilltracker.data.entity.DrillEntity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import durdinapps.rxfirebase2.DataSnapshotMapper;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by Brookman Holmes on 7/14/2017.
 */
class FirebaseDataStore implements DrillDataStore {
    private static final String TAG = FirebaseDataStore.class.getName();

    private StorageReference imagesRef = FirebaseStorage.getInstance().getReference();
    private DatabaseReference drillsRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference userRef = FirebaseDatabase.getInstance().getReference();

    FirebaseDataStore() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        init(Objects.requireNonNull(user).getUid());
    }

    private void init(String userId) {
        imagesRef = FirebaseStorage.getInstance().getReference().child("user").child(userId);
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        drillsRef = userRef.child("drills");
        drillsRef.keepSynced(true);
        userRef.keepSynced(true);
        onLogin();
    }

    @Override
    public Observable<DrillEntity> addDrill(DrillEntity entity) {
        if (entity.id == null) {
            entity.id = drillsRef.push().getKey();
        }
        drillsRef.child(Objects.requireNonNull(entity.id)).updateChildren(DrillEntity.toMap(entity));
        return RxFirebaseDatabase.observeValueEvent(drillsRef.child(entity.id), DrillEntity.class).toObservable();
    }

    @Override
    public Observable<List<DrillEntity>> drillEntityList() {
        return RxFirebaseDatabase.observeValueEvent(drillsRef, DataSnapshotMapper.listOf(DrillEntity.class)).toObservable();
    }

    @Override
    public Observable<DrillEntity> drillEntity(String id) {
        return RxFirebaseDatabase.observeValueEvent(drillsRef.child(id), DrillEntity.class).toObservable();
    }

    @Override
    public Observable<DrillEntity> addAttempt(String id, DrillEntity.AttemptEntity attempt) {
        String attemptId = drillsRef.child(id).child("attempts").push().getKey();
        drillsRef.child(id).child("attempts").child(Objects.requireNonNull(attemptId)).setValue(attempt);
        return drillEntity(id);
    }

    @Override
    public void removeLastAttempt(String id) {
        drillsRef.child(id).child("attempts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) { // iterate through list of children
                    Iterator<DataSnapshot> children = dataSnapshot.getChildren().iterator();
                    while (children.hasNext()) {
                        DataSnapshot snapshot = children.next();

                        // if we're at the last item in the list remove it
                        if (!children.hasNext()) {
                            snapshot.getRef().setValue(null);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public Single<UploadTask.TaskSnapshot> uploadImage(final String drillId, final byte[] image) {
        final StorageTask<UploadTask.TaskSnapshot> task = imagesRef.child(drillId + ".jpg").putBytes(image);

        return Single.create(emitter -> task
                .addOnSuccessListener(taskSnapshot -> {
                    Log.i(TAG, "uploadImage: onSuccess");
                    emitter.onSuccess(taskSnapshot);
                }).addOnFailureListener(e -> {
                    Log.i(TAG, "uploadImage: onFailure" + e);
                    if (!emitter.isDisposed())
                        emitter.onError(e);
                }).addOnCanceledListener(() -> Log.i(TAG, "onCanceled: ")));
    }

    @Override
    public Observable<DrillEntity> updateDrill(DrillEntity entity) {
        drillsRef.child(entity.id).updateChildren(DrillEntity.toMap(entity));
        return drillEntity(entity.id);
    }

    @Override
    public void deleteDrill(String drillId) {
        imagesRef.child(drillId + ".jpg").delete();
        drillsRef.child(drillId).setValue(null);
    }

    private void onLogin() {
        final FirebaseDrillPackDataStore drillPacks = new FirebaseDrillPackDataStore();

        userRef.child("purchased_drill_packs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    drillPacks.drillPackEntity(snapshot.getKey())
                            .subscribe(drillEntities -> {
                                for (DrillEntity entity : drillEntities) {
                                    updateDrill(entity);
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        userRef.child("lastLogin").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                drillPacks.drillPackEntity("drill_pack_3")
                        .subscribe(drillEntities -> {
                            for (DrillEntity entity : drillEntities) {
                                addDrill(entity);
                            }
                        });

                userRef.child("lastLogin").setValue(System.currentTimeMillis());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
