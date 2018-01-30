package com.brookmanholmes.drilltracker.data.repository.datasource;

import com.brookmanholmes.drilltracker.data.entity.DrillEntity;
import com.brookmanholmes.drilltracker.data.entity.DrillPackEntity;

import java.util.List;

import io.reactivex.Observable;

/**
 * Interface that represents a data store from where data is retrieved.
 */
public interface DrillPackDataStore {
    /**
     * Get an {@link Observable} which will emit a List of {@link DrillPackEntity}.
     */
    Observable<List<DrillPackEntity>> drillPackEntityList();

    /**
     * Get an {@link Observable} which will emit a list of {@link DrillEntity} for that
     * {@link DrillPackEntity}'s id.
     *
     * @param id The id to retrieve drill data.
     */
    Observable<List<DrillEntity>> drillPackEntity(String id);

    /**
     * Records that the user has purchased a drill pack
     *
     * @param sku The sku of the drill pack that was purchased
     */
    void purchaseDrillPack(String sku);
}
