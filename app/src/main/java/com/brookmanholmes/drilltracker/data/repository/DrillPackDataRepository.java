package com.brookmanholmes.drilltracker.data.repository;

import com.brookmanholmes.drilltracker.data.entity.DrillPackEntity;
import com.brookmanholmes.drilltracker.data.entity.mapper.DrillEntityDataMapper;
import com.brookmanholmes.drilltracker.data.entity.mapper.DrillPackEntityDataMapper;
import com.brookmanholmes.drilltracker.data.repository.datasource.DrillPackDataStore;
import com.brookmanholmes.drilltracker.domain.Drill;
import com.brookmanholmes.drilltracker.domain.DrillPack;
import com.brookmanholmes.drilltracker.domain.repository.DrillPackRepository;

import java.util.List;

import io.reactivex.Observable;
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
                .map(drillEntityList -> {
                    DrillEntityDataMapper drillEntityDataMapper = new DrillEntityDataMapper();
                    return drillEntityDataMapper.transform(drillEntityList);
                });
    }

    @Override
    public Observable<List<Drill>> purchaseDrillPack(String sku) {
        dataStore.purchaseDrillPack(sku);
        return observeDrillPack(sku);
    }

    private Function<List<DrillPackEntity>, List<DrillPack>> transformEntities() {
        return mapper::transform;
    }
}
