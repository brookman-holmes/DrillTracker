package com.brookmanholmes.drilltracker.data.repository.datasource;

import com.brookmanholmes.drilltracker.data.entity.DrillPackEntity;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Brookman Holmes on 8/9/2017.
 */

public interface DrillPackDataStore {
    Observable<List<DrillPackEntity>> drillPackEntityList();

    Observable<DrillPackEntity> drillPackEntity(String id);
}
