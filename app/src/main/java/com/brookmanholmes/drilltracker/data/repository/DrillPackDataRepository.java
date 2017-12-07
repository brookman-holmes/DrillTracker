package com.brookmanholmes.drilltracker.data.repository;

import com.brookmanholmes.drilltracker.data.entity.DrillEntity;
import com.brookmanholmes.drilltracker.data.entity.DrillPackEntity;
import com.brookmanholmes.drilltracker.data.entity.mapper.DrillEntityDataMapper;
import com.brookmanholmes.drilltracker.data.entity.mapper.DrillPackEntityDataMapper;
import com.brookmanholmes.drilltracker.data.repository.datasource.DrillPackDataStore;
import com.brookmanholmes.drilltracker.domain.Drill;
import com.brookmanholmes.drilltracker.domain.DrillPack;
import com.brookmanholmes.drilltracker.domain.repository.DrillPackRepository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * {@link DrillPackRepository} for retrieving drill pack data.
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
    public Observable<List<Drill>> observeDrillPack(String sku) {
        return dataStore.drillPackEntity(sku)
                .map(new Function<List<DrillEntity>, List<Drill>>() {
                    @Override
                    public List<Drill> apply(@NonNull List<DrillEntity> drillEntityList) throws Exception {
                        DrillEntityDataMapper drillEntityDataMapper = new DrillEntityDataMapper();
                        return drillEntityDataMapper.transform(drillEntityList);
                    }
                });
    }

    @Override
    public Observable<List<Drill>> purchaseDrillPack(String sku) {
        return observeDrillPack(sku);
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
