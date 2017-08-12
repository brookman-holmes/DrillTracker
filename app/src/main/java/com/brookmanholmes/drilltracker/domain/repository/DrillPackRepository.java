package com.brookmanholmes.drilltracker.domain.repository;

import com.brookmanholmes.drilltracker.domain.DrillPack;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Brookman Holmes on 8/9/2017.
 */

public interface DrillPackRepository {
    Observable<List<DrillPack>> observeDrillPacks();

    Observable<DrillPack> observeDrillPack(String sku);
}
