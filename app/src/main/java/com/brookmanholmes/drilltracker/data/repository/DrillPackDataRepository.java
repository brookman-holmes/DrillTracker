package com.brookmanholmes.drilltracker.data.repository;

import com.brookmanholmes.drilltracker.data.entity.DrillPackEntity;
import com.brookmanholmes.drilltracker.data.entity.mapper.DrillPackEntityDataMapper;
import com.brookmanholmes.drilltracker.data.repository.datasource.DrillPackDataStore;
import com.brookmanholmes.drilltracker.domain.DrillPack;
import com.brookmanholmes.drilltracker.domain.repository.DrillPackRepository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by Brookman Holmes on 8/9/2017.
 */

public class DrillPackDataRepository implements DrillPackRepository {
    private final DrillPackDataStore dataStore;
    private final DrillPackEntityDataMapper mapper;

    public DrillPackDataRepository(DrillPackDataStore drillPackDataStore, DrillPackEntityDataMapper mapper) {
        this.dataStore = drillPackDataStore;
        this.mapper = mapper;
    }

    public DrillPackDataRepository(DrillPackDataStore drillPackDataStore) {
        this.dataStore = drillPackDataStore;
        mapper = new DrillPackEntityDataMapper();
    }

    @Override
    public Observable<List<DrillPack>> observeDrillPacks() {
        return dataStore.drillPackEntityList()
                .map(transformEntities());
    }

    @Override
    public Observable<DrillPack> observeDrillPack(String sku) {
        return dataStore.drillPackEntity(sku)
                .map(transformEntity());
    }

    private Function<DrillPackEntity, DrillPack> transformEntity() {
        return new Function<DrillPackEntity, DrillPack>() {
            @Override
            public DrillPack apply(@NonNull DrillPackEntity drillPackEntity) throws Exception {
                return mapper.transform(drillPackEntity);
            }
        };
    }

    private Function<List<DrillPackEntity>, List<DrillPack>> transformEntities() {
        return new Function<List<DrillPackEntity>, List<DrillPack>>() {
            @Override
            public List<DrillPack> apply(@NonNull List<DrillPackEntity> drillPackEntities) throws Exception {
                return mapper.transform(drillPackEntities);
            }
        };
    }
}
