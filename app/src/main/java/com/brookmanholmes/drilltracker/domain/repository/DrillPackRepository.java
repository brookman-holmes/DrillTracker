package com.brookmanholmes.drilltracker.domain.repository;

import com.brookmanholmes.drilltracker.domain.DrillPack;

import java.util.List;

import io.reactivex.Observable;

/**
 * Interface that represents a Repository for getting {@link DrillPack} related data.
 */
public interface DrillPackRepository {
    /**
     * Get an {@link Observable} which will emit a List of {@link DrillPack}.
     */
    Observable<List<DrillPack>> observeDrillPacks();

    /**
     * Get an {@link Observable} which will emit a {@link DrillPack}.
     *
     * @param sku The sku used to retrieve the product data.
     */
    Observable<DrillPack> observeDrillPack(String sku);
}
