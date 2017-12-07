package com.brookmanholmes.drilltracker.domain.repository;

import com.brookmanholmes.drilltracker.domain.Drill;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;
import com.google.firebase.storage.UploadTask;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Observable;

/**
 * Interface that represents a repository for getting {@link Drill} related data
 */
public interface DrillRepository {
    /**
     * Get a {@link Observable} which will emit a {@link Drill} of the drill that was just added
     *
     * @param name        The name of the drill
     * @param description The description of the drill
     * @param image       The image of the drill
     * @param type        The type of the drill
     * @param maxScore    The maximum possible score in the drill
     * @param targetScore The target goal for the drill
     * @param purchased   If the drill was purchased
     */
    Observable<Drill> addDrill(String name, String description, byte[] image, String type, int maxScore, int targetScore, boolean purchased);

    /**
     * Get a {@link Observable} which will emit a {@link Drill} of the drill that was just added
     *
     * @param name        The name of the drill
     * @param description The description of the drill
     * @param imageUrl    The url of the image of the drill
     * @param type        The type of the drill
     * @param maxScore    The maximum possible score in the drill
     * @param targetScore The target goal for the drill
     * @param purchased   If the drill was purchased
     */
    Observable<Drill> addDrill(String name, String description, String imageUrl, String type, int maxScore, int targetScore, boolean purchased);

    /**
     * Get a {@link Observable} which will emit a list of {@link Drill}
     * @param filter The filter used to remove unwanted drills from the list
     */
    Observable<List<Drill>> observeDrills(DrillModel.Type filter);

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
    Observable<Drill> addAttempt(final String id, Drill.Attempt attempt);

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
     * @param name The name of the drill
     * @param description The description of the drill
     * @param id The id of the drill
     * @param image The image of the drill
     * @param type The type of the drill
     * @param maxScore The maximum possible score in the drill
     * @param targetScore The target goal for the drill
     */
    Observable<Drill> updateDrill(String name, String description, String id, byte[] image, String type, int maxScore, int targetScore);

    /**
     * Get a {@link Maybe} which will emit a {@link com.google.firebase.storage.UploadTask.TaskSnapshot}
     * of an image that was just uploaded to the database
     * @param id The id of the drill for this image
     * @param image The image for the drill
     */
    Maybe<UploadTask.TaskSnapshot> uploadImage(String id, byte[] image);

    /**
     * Removes a drill from the repository
     * @param drillId The id of the drill to remove
     */
    void deleteDrill(String drillId);
}
