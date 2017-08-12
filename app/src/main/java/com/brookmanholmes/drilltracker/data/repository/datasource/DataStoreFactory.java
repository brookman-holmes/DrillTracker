package com.brookmanholmes.drilltracker.data.repository.datasource;

/**
 * Created by Brookman Holmes on 7/14/2017.
 */

public class DataStoreFactory {
    private static DrillDataStore dataStore;
    private static DrillPackDataStore drillPackDataStore;

    private DataStoreFactory() {
    }

    public static DrillDataStore getDrillDataStore() {
        if (dataStore == null)
            dataStore = new FirebaseDataStore();

        return dataStore;
    }

    public static DrillPackDataStore getDrillPackDataStore() {
        if (drillPackDataStore == null) {
            drillPackDataStore = new FirebaseDrillPackDataStore();
        }

        return drillPackDataStore;
    }
}
