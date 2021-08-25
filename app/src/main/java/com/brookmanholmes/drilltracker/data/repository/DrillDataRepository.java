package com.brookmanholmes.drilltracker.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.brookmanholmes.drilltracker.data.entity.AttemptEntity;
import com.brookmanholmes.drilltracker.data.entity.DrillEntity;
import com.brookmanholmes.drilltracker.data.entity.mapper.DrillEntityDataMapper;
import com.brookmanholmes.drilltracker.data.repository.datasource.DrillDataStore;
import com.brookmanholmes.drilltracker.domain.Attempt;
import com.brookmanholmes.drilltracker.domain.Drill;
import com.brookmanholmes.drilltracker.domain.repository.DrillRepository;
import com.brookmanholmes.drilltracker.presentation.model.Type;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import timber.log.Timber;

/**
 * {@link DrillRepository} for retrieving drill data.
 */
public class DrillDataRepository implements DrillRepository {
    private static final String TAG = DrillDataRepository.class.getName();

    private final DrillEntityDataMapper drillEntityDataMapper;
    private final DrillDataStore dataStore;

    private DrillDataRepository(DrillEntityDataMapper drillEntityDataMapper, DrillDataStore drillDataStore) {
        this.dataStore = drillDataStore;
        this.drillEntityDataMapper = drillEntityDataMapper;
    }

    public DrillDataRepository(DrillDataStore drillDataStore) {
        this.dataStore = drillDataStore;
        this.drillEntityDataMapper = new DrillEntityDataMapper();
    }

    @Override
    public Observable<Drill> addDrill(Drill drill, byte[] image) {
        return dataStore.addDrill(drillEntityDataMapper.transform(drill)).map(uploadImageMap(image)).map(transformDrillEntity());
    }

    @Override
    public Observable<Drill> addDrill(Drill drill) {
        return dataStore.addDrill(drillEntityDataMapper.transform(drill)).map(transformDrillEntity());
    }

    @Override
    public Observable<List<Drill>> observeDrills(final Type filter) {
        return dataStore.drillEntityList()
                .map(drillEntityList -> {
                    for (Iterator<DrillEntity> iterator = drillEntityList.iterator(); iterator.hasNext(); ) {
                        DrillEntity entity = iterator.next();
                        if (!drillEntityDataMapper.transform(filter).equals(entity.type) && !filter.equals(Type.ANY))
                            iterator.remove();
                    }
                    return drillEntityDataMapper.transform(drillEntityList);
                });
    }

    @Override
    public Observable<Drill> observeDrill(final String id) {
        return dataStore.drillEntity(id)
                .map(transformDrillEntity());
    }

    @Override
    public Observable<Drill> addAttempt(String id, Attempt attempt) {
        Timber.d("AddAttempt drilldatarepository attempt: %s", attempt);
        return dataStore.addAttempt(id, drillEntityDataMapper.transform(attempt))
                .map(transformDrillEntity());
    }

    @Override
    public Observable<Collection<Attempt>> getAttempts(String id) {
        return dataStore.getAttempts(id).map(new Function<List<AttemptEntity>, Collection<Attempt>>() {
            @Override
            public Collection<Attempt> apply(@NonNull List<AttemptEntity> attemptEntities) throws Exception {
                if (attemptEntities.size() > 0) {
                    Timber.d(attemptEntities.get(0).toString());
                }
                Collection<Attempt> result = new ArrayList<>();
                for (AttemptEntity attemptEntity : attemptEntities) {
                    result.add(drillEntityDataMapper.transform(attemptEntity));
                }
                return result;
            }
        });
    }

    @Override
    public void removeLastAttempt(String id) {
        dataStore.removeLastAttempt(id);
    }

    @Override
    public Single<UploadTask.TaskSnapshot> uploadImage(String id, byte[] image) {
        return dataStore.uploadImage(id, image);
    }

    @Override
    public Observable<Drill> updateDrill(Drill drill) {
        return dataStore.updateDrill(drillEntityDataMapper.transform(drill)).map(transformDrillEntity());
    }

    @Override
    public Observable<Drill> updateDrill(Drill drill, final byte[] image) {
        return dataStore.updateDrill(drillEntityDataMapper.transform(drill))
                .map(uploadImageMap(image))
                .map(transformDrillEntity());
    }

    @Override
    public void deleteDrill(String drillId) {
        dataStore.deleteDrill(drillId);
    }

    private Function<DrillEntity, DrillEntity> uploadImageMap(final byte[] image) {
        return entity -> {
            DrillEntity newEntity = entity.copy();
            uploadImage(entity.id, image)
                    .subscribe(new SingleObserver<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onSuccess(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                                newEntity.imageUrl = uri.toString();
                                dataStore.updateDrill(newEntity);
                            });
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.i(TAG, "onError: " + e);
                        }
                    });
            return entity;
        };
    }

    private Function<DrillEntity, Drill> transformDrillEntity() {
        return drillEntityDataMapper::transform;
    }
}
