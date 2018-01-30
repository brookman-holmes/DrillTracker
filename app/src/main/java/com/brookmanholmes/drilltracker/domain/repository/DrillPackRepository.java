package com.brookmanholmes.drilltracker.domain.repository;

import com.brookmanholmes.drilltracker.domain.Drill;
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
     * Get an {@link Observable} which will emit a List of {@link Drill} that the drill pack contains.
     *
     * @param sku The sku used to retrieve the product data.
     */
    Observable<List<Drill>> observeDrillPack(String sku);

    /**
     * Get an {@link Observable} which will emit a list of {@link Drill} that the drill pack contains and
     * that the user purchased
     *
     * @param sku The sku of the drill pack to be purchased
     */
    Observable<List<Drill>> purchaseDrillPack(String sku);
}
