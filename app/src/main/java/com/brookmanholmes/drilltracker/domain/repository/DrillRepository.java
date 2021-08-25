package com.brookmanholmes.drilltracker.domain.repository;

import com.brookmanholmes.drilltracker.domain.Attempt;
import com.brookmanholmes.drilltracker.domain.Drill;
import com.brookmanholmes.drilltracker.presentation.model.Type;
import com.google.firebase.storage.UploadTask;

import java.util.Collection;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Interface that represents a repository for getting {@link Drill} related data
 */
public interface DrillRepository {
    /**
     * Get a {@link Observable} which will emit a {@link Drill} of the drill that was just added
     *
     * @param drill The drill to be added
     * @param image The image of the drill
     */
    Observable<Drill> addDrill(Drill drill, byte[] image);

    /**
     * Get a {@link Observable} which will emit a {@link Drill} of the drill that was just added
     *
     * @param drill The drill to be added
     */
    Observable<Drill> addDrill(Drill drill);

    /**
     * Get a {@link Observable} which will emit a list of {@link Drill}
     * @param filter The filter used to remove unwanted drills from the list
     */
    Observable<List<Drill>> observeDrills(Type filter);

    /**
     * Get a {@link Observable} which will emit a {@link Drill}
     * @param id The drill id used to retrieve the drill data
     */
    Observable<Drill> observeDrill(final String id);

    /**
     * Get a {@link Observable} which will add an attempt to the drill and emit a {@link Drill}
     * @param id The id of the drill to add an attempt to
     * @param attempt The attempt to be added
     */
    Observable<Drill> addAttempt(final String id, Attempt attempt);

    /**
     * Get a {@link Observable}
     * @param id The id of the drill to get the attempts from
     * @return a list of attempts for that drill
     */
    Observable<Collection<Attempt>> getAttempts(final String id);

    /**
     * Removes the last attempt from a drill
     * @param id The id of the drill to modify
     */
    void removeLastAttempt(final String id);

    /**
     * Get a {@link Observable} which will update a {@link Drill} and re-emit it
     * @param drill The new drill information to update
     */
    Observable<Drill> updateDrill(Drill drill);

    /**
     * Get a {@link Observable} which will update a {@link Drill} and re-emit it
     * @param drill The new drill information to update
     * @param image The image of for the drill
     */
    Observable<Drill> updateDrill(Drill drill, byte[] image);

    /**
     * Get a {@link Single} which will emit a {@link com.google.firebase.storage.UploadTask.TaskSnapshot}
     * of an image that was just uploaded to the database
     * @param id The id of the drill for this image
     * @param image The image for the drill
     */
    Single<UploadTask.TaskSnapshot> uploadImage(String id, byte[] image);

    /**
     * Removes a drill from the repository
     * @param drillId The id of the drill to remove
     */
    void deleteDrill(String drillId);
}
