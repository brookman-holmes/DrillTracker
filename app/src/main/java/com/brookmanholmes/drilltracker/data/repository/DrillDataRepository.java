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

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by Brookman Holmes on 7/9/2017.
 */

public class DrillDataRepository implements DrillRepository {
    private static final String TAG = DrillDataRepository.class.getName();

    private final DrillEntityDataMapper drillEntityDataMapper;
    private final DrillDataStore drillDataStore;

    public DrillDataRepository(DrillEntityDataMapper drillEntityDataMapper, DrillDataStore drillDataStore) {
        this.drillDataStore = drillDataStore;
        this.drillEntityDataMapper = drillEntityDataMapper;
    }

    @Override
    public Observable<Drill> addDrill(final String name, final String description, final byte[] image, final String type, final int maxScore, final int targetScore) {
        return drillDataStore.addDrill(new DrillEntity(name, description, null, null, type, maxScore, targetScore))
                .map(uploadImageMap(image))
                .map(transformDrillEntity());
    }

    @Override
    public Observable<List<Drill>> observeDrills(final DrillModel.Type filter) {
        return drillDataStore.drillEntityList()
                .map(new Function<List<DrillEntity>, List<Drill>>() {
                    @Override
                    public List<Drill> apply(@NonNull List<DrillEntity> drillEntityList) throws Exception {
                        for (Iterator<DrillEntity> iterator = drillEntityList.iterator(); iterator.hasNext();) {
                            DrillEntity entity = iterator.next();
                            if (!drillEntityDataMapper.transform(filter).equals(entity.type) && !filter.equals(DrillModel.Type.ANY))
                                iterator.remove();
                        }
                        return drillEntityDataMapper.transform(drillEntityList);
                    }
                });
    }

    @Override
    public Observable<Drill> observeDrill(final String id) {
        return drillDataStore.drillEntity(id)
                .map(transformDrillEntity());
    }

    @Override
    public Observable<Drill> addAttempt(String id, Drill.Attempt attempt) {
        return drillDataStore.addAttempt(id, drillEntityDataMapper.transform(attempt))
                .map(transformDrillEntity());
    }

    @Override
    public Observable<Drill> removeAttempt(String id) {
        return drillDataStore.removeLastAttempt(id)
                .map(transformDrillEntity());
    }

    @Override
    public Maybe<UploadTask.TaskSnapshot> uploadImage(String id, byte[] image) {
        return drillDataStore.uploadImage(id, image);
    }

    @Override
    public Observable<Drill> updateDrill(Drill drill) {
        return drillDataStore.updateDrill(new DrillEntity(drill.getName(), drill.getDescription(), drill.getId(), drill.getImageUrl(), drillEntityDataMapper.transform(drill.getType()), drill.getMaxScore(), drill.getDefaultTargetScore()))
                .map(transformDrillEntity());
    }

    @Override
    public Observable<Drill> updateDrill(String name, String description, final String id, final byte[] image, String type, int maxScore, int targetScore) {
        return drillDataStore.updateDrill(new DrillEntity(name, description, id, null, type, maxScore, targetScore))
                .map(uploadImageMap(image))
                .map(transformDrillEntity());
    }

    @Override
    public void deleteDrill(String drillId) {
        drillDataStore.deleteDrill(drillId);
    }

    private Function<DrillEntity, DrillEntity> uploadImageMap(final byte[] image) {
        return new Function<DrillEntity, DrillEntity>() {
            @Override
            public DrillEntity apply(@NonNull final DrillEntity entity) throws Exception {
                uploadImage(entity.id, image)
                        .subscribe(new Consumer<UploadTask.TaskSnapshot>() {
                            @Override
                            public void accept(@NonNull UploadTask.TaskSnapshot taskSnapshot) throws Exception {
                                entity.imageUrl = taskSnapshot.getDownloadUrl().toString();
                                drillDataStore.updateDrill(entity);
                            }
                        });

                return entity;
            }
        };
    }

    private Function<DrillEntity, Drill> transformDrillEntity() {
        return new Function<DrillEntity, Drill>() {
            @Override
            public Drill apply(@NonNull DrillEntity entity) throws Exception {
                return drillEntityDataMapper.transform(entity);
            }
        };
    }
}
