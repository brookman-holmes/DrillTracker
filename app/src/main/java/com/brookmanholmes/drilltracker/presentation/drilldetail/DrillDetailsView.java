package com.brookmanholmes.drilltracker.presentation.drilldetail;

import androidx.annotation.NonNull;

import com.brookmanholmes.drilltracker.presentation.base.LoadDataView;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;
import com.brookmanholmes.drilltracker.presentation.model.English;
import com.brookmanholmes.drilltracker.presentation.model.EnglishDataModel;
import com.brookmanholmes.drilltracker.presentation.model.ShotDataModel;
import com.brookmanholmes.drilltracker.presentation.model.Speed;
import com.brookmanholmes.drilltracker.presentation.model.SpeedDataModel;
import com.brookmanholmes.drilltracker.presentation.model.SpinDataModel;
import com.brookmanholmes.drilltracker.presentation.model.TargetDataModel;
import com.brookmanholmes.drilltracker.presentation.model.VSpin;

import java.util.List;

/**
 * Created by Brookman Holmes on 7/11/2017.
 * Interface representing a view in MVP, displays a {@link DrillModel} for the user to interact with
 */

interface DrillDetailsView extends LoadDataView {
    /**
     * Renders a drill for the user to see
     *
     * @param drill The drill to be displayed
     */
    void renderDrill(final DrillModel drill);


    void setObPositionsSpinnerVisibility(boolean visible);

    void setCbPositionsSpinnerVisibility(boolean visible);

    void setTargetPositionsVisibility(boolean visible);

    void setName(@NonNull String name);

    void setImage(@NonNull String imageUrl);

    void setDescription(@NonNull String description);

    void setPositionSpinners(int cbPositions, int obPositions, int targetPositions);

    void setShowEnglishData(boolean visible);

    void setShowSpeedData(boolean visible);

    void setShowDistanceData(boolean visible);

    void setShowSpinData(boolean visible);

    void setShowShotData(boolean visible);

    void setDistanceData(@NonNull TargetDataModel today, @NonNull TargetDataModel history);

    void setShotData(@NonNull ShotDataModel today, @NonNull ShotDataModel history);

    void setSpinData(@NonNull SpinDataModel today, @NonNull SpinDataModel history);

    void setEnglishData(@NonNull EnglishDataModel today, @NonNull EnglishDataModel history);

    void setSpeedData(@NonNull SpeedDataModel today, @NonNull SpeedDataModel history);

    /**
     * Displays the dialog for adding an attempt to a drill
     *
     * @param drill                  The selected drill
     * @param selectedCbPosition     The selected position for the cue ball
     * @param selectedObPosition     The selected position of the object ball
     * @param selectedTargetPosition The selected position of the target
     * @param selectedEnglish        The selected english type
     * @param selectedPattern        The selected pattern of the attempt
     */
    void showAddAttemptDialog(DrillModel drill, int selectedCbPosition, int selectedObPosition, int selectedTargetPosition,
                              VSpin vSpin,
                              Speed selectedSpeed, English selectedEnglish, List<Integer> selectedPattern);
}
