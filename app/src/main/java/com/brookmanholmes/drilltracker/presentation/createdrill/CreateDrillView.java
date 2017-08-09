package com.brookmanholmes.drilltracker.presentation.createdrill;

import android.text.Editable;
import android.widget.ImageView;

import com.brookmanholmes.drilltracker.presentation.model.DrillModel;

/**
 * Created by Brookman Holmes on 7/23/2017.
 */

interface CreateDrillView {
    void onMaximumScoreChanged(int value);
    void onTargetScoreChanged(int value);
    void onDescriptionChanged(Editable editable);
    void onDrillNameChanged(Editable editable);
    void onDrillImageClicked();
    void showImageChoiceDialog();
    void onImageCropped(ImageView image);
    void onDrillUploaded(String drillId, int maxScore, int targetScore);
    void isDrillComplete(boolean isComplete);
    void onDrillTypeSelected(int position);

    void loadDrillData(DrillModel model);

    void finish();

    void setIsEditing(boolean b);
}
