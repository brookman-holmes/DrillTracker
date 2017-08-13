package com.brookmanholmes.drilltracker.data.repository.datasource;

import com.brookmanholmes.drilltracker.data.entity.DrillEntity;
import com.google.firebase.storage.UploadTask;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Observable;

/**
 * Interface that represents a data store from where data is retrieved.
 */
public interface DrillDataStore {

    /**
     * Get an {@link Observable} which will add a drill to the data store and emit a {@link DrillEntity}
     *
     * @param entity The entity to be added
     */
    Observable<DrillEntity> addDrill(DrillEntity entity);

    /**
     * Get an {@link Observable} which will emit a List of {@link DrillEntity}.
     */
    Observable<List<DrillEntity>> drillEntityList();

    /**
     * Get an {@link Observable} which will emit a {@link DrillEntity} by its id.
     *
     * @param id The id to retrieve user data.
     */
    Observable<DrillEntity> drillEntity(final String id);

    /**
     * Get an {@link Observable} which will add an attempt to a drill and emit a {@link DrillEntity}
     * @param id The id of the drill to have the attempt added to
     * @param attempt The attempt to add to the drill
     */
    Observable<DrillEntity> addAttempt(final String id, DrillEntity.AttemptEntity attempt);

    /**
     * Removes the most recent attempt from a drill
     * @param id The id of the drill to remove the attempt from
     */
    void removeLastAttempt(final String id);

    /**
     * Get a {@link Maybe} which will upload an image to the data store and emit an
     * {@link UploadTask.TaskSnapshot} to allow access to the URL of the image
     * @param id The id of the drill
     * @param image The image of the drill
     */
    Maybe<UploadTask.TaskSnapshot> uploadImage(String id, byte[] image);

    /**
     * Get an {@link Observable} which will update a drill and emit a {@link DrillEntity}
     * @param entity The updated entity
     */
    Observable<DrillEntity> updateDrill(DrillEntity entity);

    /**
     * Removes a drill from the data store
     * @param drillId The id of the drill to remove
     */
    void deleteDrill(String drillId);
}
