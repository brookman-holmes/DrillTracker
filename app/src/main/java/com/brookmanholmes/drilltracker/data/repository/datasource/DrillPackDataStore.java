package com.brookmanholmes.drilltracker.data.repository.datasource;

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
     * Get an {@link Observable} which will emit a {@link DrillPackEntity} by its id.
     *
     * @param id The id to retrieve drill data.
     */
    Observable<DrillPackEntity> drillPackEntity(String id);
}
