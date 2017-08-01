package com.brookmanholmes.drilltracker.domain.repository;

import com.brookmanholmes.drilltracker.domain.Drill;
import com.brookmanholmes.drilltracker.domain.interactor.UseCase;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;
import com.google.firebase.storage.UploadTask;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;

/**
 * Created by Brookman Holmes on 7/8/2017.
 */

public interface DrillRepository {
    Observable<Drill> addDrill(String name, String description, byte[] image, String type, int maxScore, int targetScore);
    Observable<List<Drill>> observeDrills(DrillModel.Type filter);
    Observable<Drill> observeDrill(final String id);
    Observable<Drill> addAttempt(final String id, Drill.Attempt attempt);
    Observable<Drill> removeAttempt(final String id);
    Observable<Drill> updateDrill(Drill drill);
    Observable<Drill> updateDrill(String name, String description, String id, byte[] image, String type, int maxScore, int targetScore);
    Maybe<UploadTask.TaskSnapshot> uploadImage(String id, byte[] image);
    void deleteDrill(String drillId);
}
