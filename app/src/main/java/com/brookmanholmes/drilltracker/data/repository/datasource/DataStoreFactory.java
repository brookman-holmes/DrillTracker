package com.brookmanholmes.drilltracker.data.repository.datasource;

import com.brookmanholmes.drilltracker.data.repository.DrillDataRepository;
import com.brookmanholmes.drilltracker.data.repository.DrillPackDataRepository;
import com.brookmanholmes.drilltracker.domain.repository.DrillPackRepository;
import com.brookmanholmes.drilltracker.domain.repository.DrillRepository;

/**
 * Created by Brookman Holmes on 7/14/2017.
 */

public class DataStoreFactory {
    private static DrillDataStore dataStore;
    private static DrillPackDataStore drillPackDataStore;
    private static DrillDataRepository drillRepo;
    private static DrillPackDataRepository drillPackRepo;

    private DataStoreFactory() {
    }

    private static DrillDataStore getDrillDataStore() {
        if (dataStore == null)
            dataStore = new FirebaseDataStore();

        return dataStore;
    }

    private static DrillPackDataStore getDrillPackDataStore() {
        if (drillPackDataStore == null) {
            drillPackDataStore = new FirebaseDrillPackDataStore();
        }

        return drillPackDataStore;
    }

    public static DrillRepository getDrillRepo() {
        if (drillRepo == null) {
            drillRepo = new DrillDataRepository(getDrillDataStore());
        }

        return drillRepo;
    }


    public static DrillPackRepository getDrillPackRepo() {
        if (drillPackRepo == null) {
            drillPackRepo = new DrillPackDataRepository(getDrillPackDataStore());
        }

        return drillPackRepo;
    }
}
