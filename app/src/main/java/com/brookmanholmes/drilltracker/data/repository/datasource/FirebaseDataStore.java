package com.brookmanholmes.drilltracker.data.repository.datasource;

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
import com.google.firebase.storage.UploadTask;

import java.util.Iterator;
import java.util.List;

import durdinapps.rxfirebase2.DataSnapshotMapper;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import durdinapps.rxfirebase2.RxFirebaseStorage;
import io.reactivex.Maybe;
import io.reactivex.Observable;

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
        init(user.getUid());
    }

    private void init(String userId) {
        imagesRef = FirebaseStorage.getInstance().getReference().child(userId);
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        drillsRef = userRef.child("drills");

        final DatabaseReference testData = FirebaseDatabase.getInstance().getReference().child("test");

        userRef.child("firstLogin").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean firstLogin = dataSnapshot.getValue(Boolean.class);
                if (firstLogin == null || firstLogin) {
                    testData.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            userRef.setValue(dataSnapshot.getValue());
                            userRef.child("firstLogin").setValue(false);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public Observable<DrillEntity> addDrill(DrillEntity entity) {
        String key = drillsRef.push().getKey();
        entity.id = key;
        drillsRef.child(key).setValue(entity);
        return RxFirebaseDatabase.observeValueEvent(drillsRef.child(key), DrillEntity.class).toObservable();
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
        drillsRef.child(id).child("attempts").child(attemptId).setValue(attempt);
        return drillEntity(id);
    }

    @Override
    public void removeLastAttempt(String id) {
        drillsRef.child(id).child("attempts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public Maybe<UploadTask.TaskSnapshot> uploadImage(String drillId, byte[] image) {
        StorageReference storageRef = imagesRef.child(drillId + ".jpg");
        return RxFirebaseStorage.putBytes(storageRef, image);
    }

    @Override
    public Observable<DrillEntity> updateDrill(DrillEntity entity) {
        drillsRef.child(entity.id).setValue(entity);
        return drillEntity(entity.id);
    }

    @Override
    public void deleteDrill(String drillId) {
        imagesRef.child(drillId + ".jpg").delete();
        drillsRef.child(drillId).setValue(null);
    }
}
