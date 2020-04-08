package com.brookmanholmes.drilltracker.data.repository;

import android.util.Log;

import com.brookmanholmes.drilltracker.data.entity.DrillEntity;
import com.brookmanholmes.drilltracker.data.entity.mapper.DrillEntityDataMapper;
import com.brookmanholmes.drilltracker.data.repository.datasource.DrillDataStore;
import com.brookmanholmes.drilltracker.domain.Drill;
import com.brookmanholmes.drilltracker.domain.repository.DrillRepository;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;
import com.google.firebase.storage.UploadTask;

import java.util.Iterator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

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
        return dataStore.addDrill(
                new DrillEntity(
                        drill.getName(),
                        drill.getDescription(),
                        null,
                        null,
                        drillEntityDataMapper.transform(drill.getType()),
                        drill.getMaxScore(),
                        drill.getDefaultTargetScore(),
                        drill.getObPositions(),
                        drill.getCbPositions(),
                        drill.getTargetPositions(),
                        drill.isPurchased(),
                        drillEntityDataMapper.transform(drill.getPatterns())
                )).map(uploadImageMap(image)).map(transformDrillEntity());
    }

    @Override
    public Observable<Drill> addDrill(Drill drill) {
        return dataStore.addDrill(
                new DrillEntity(
                        drill.getName(),
                        drill.getDescription(),
                        drill.getId(),
                        drill.getImageUrl(),
                        drillEntityDataMapper.transform(drill.getType()),
                        drill.getMaxScore(),
                        drill.getDefaultTargetScore(),
                        drill.getObPositions(),
                        drill.getCbPositions(),
                        drill.getTargetPositions(),
                        drill.isPurchased(),
                        drillEntityDataMapper.transform(drill.getPatterns())
                )).map(transformDrillEntity());
    }

    @Override
    public Observable<List<Drill>> observeDrills(final DrillModel.Type filter) {
        return dataStore.drillEntityList()
                .map(drillEntityList -> {
                    for (Iterator<DrillEntity> iterator = drillEntityList.iterator(); iterator.hasNext(); ) {
                        DrillEntity entity = iterator.next();
                        if (!drillEntityDataMapper.transform(filter).equals(entity.type) && !filter.equals(DrillModel.Type.ANY))
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
    public Observable<Drill> addAttempt(String id, Drill.Attempt attempt) {
        return dataStore.addAttempt(id, drillEntityDataMapper.transform(attempt))
                .map(transformDrillEntity());
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
        return dataStore.updateDrill(
                new DrillEntity(
                        drill.getName(),
                        drill.getDescription(),
                        drill.getId(),
                        drill.getImageUrl(),
                        drillEntityDataMapper.transform(drill.getType()),
                        drill.getMaxScore(),
                        drill.getDefaultTargetScore(),
                        drill.getObPositions(),
                        drill.getCbPositions(),
                        drill.getTargetPositions(),
                        drillEntityDataMapper.transform(drill.getPatterns())
                )
        )
                .map(transformDrillEntity());
    }

    @Override
    public Observable<Drill> updateDrill(Drill drill, final byte[] image) {
        return dataStore.updateDrill(
                new DrillEntity(
                        drill.getName(),
                        drill.getDescription(),
                        drill.getId(),
                        null,
                        drillEntityDataMapper.transform(drill.getType()),
                        drill.getMaxScore(),
                        drill.getDefaultTargetScore(),
                        drill.getObPositions(),
                        drill.getCbPositions(),
                        drill.getTargetPositions(),
                        drillEntityDataMapper.transform(drill.getPatterns())
                )
        )
                .map(uploadImageMap(image))
                .map(transformDrillEntity());
    }

    @Override
    public void deleteDrill(String drillId) {
        dataStore.deleteDrill(drillId);
    }

    private Function<DrillEntity, DrillEntity> uploadImageMap(final byte[] image) {
        return entity -> {
            DrillEntity newEntity = new DrillEntity(entity.name, entity.description, entity.id, entity.imageUrl, entity.type, entity.maxScore, entity.targetScore, entity.obPositions, entity.cbPositions, entity.targetPositions, entity.patterns);
            uploadImage(entity.id, image)
                    .subscribe(new SingleObserver<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            Log.i(TAG, "onSubscribe: ");
                        }

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
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
