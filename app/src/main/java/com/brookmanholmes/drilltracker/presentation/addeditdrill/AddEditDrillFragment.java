package com.brookmanholmes.drilltracker.presentation.addeditdrill;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.presentation.base.BaseFragment;
import com.brookmanholmes.drilltracker.presentation.drilldetail.DrillDetailsActivity;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;
import com.brookmanholmes.drilltracker.presentation.view.CustomNumberPickerV2;
import com.brookmanholmes.drilltracker.presentation.view.util.ImageHandler;
import com.crashlytics.android.Crashlytics;
import com.theartofdev.edmodo.cropper.CropImage;
import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;

/**
 * Created by Brookman Holmes on 7/23/2017.
 * TODO: make this look better
 */


public class AddEditDrillFragment extends BaseFragment<AddEditDrillPresenter> implements AddEditDrillView {
    private static final String TAG = AddEditDrillFragment.class.getName();
    private static final String PARAM_DRILL_ID = "param_drill_id";

    private static final int ACTION_TAKE_IMAGE = 101;
    private static final int ACTION_CHOOSE_IMAGE = 102;
    private static final int COMPRESSION_AMOUNT = 50;
    private final ValueChangedListener obPositionsListener = (value, action) -> presenter.setObPositions(value);
    private final ValueChangedListener cbPositionsListener = (value, action) -> presenter.setCbPositions(value);
    private final ValueChangedListener targetValueListener = (value, action) -> onTargetScoreChanged(value);
    private final ValueChangedListener maxValueListener = new ValueChangedListener() {
        @Override
        public void valueChanged(int value, ActionEnum action) {
            onMaximumScoreChanged(value);
            targetScorePicker.setMaxValue(value);
        }
    };
    private final ValueChangedListener targetPositionsListener = (value, action) -> presenter.setTargetPositions(value);
    @BindView(R.id.input_drill_name)
    EditText drillName;
    @BindView(R.id.input_drill_description)
    EditText drillDescription;
    @BindView(R.id.maxScorePicker)
    CustomNumberPickerV2 maxScorePicker;
    @BindView(R.id.targetScorePicker)
    CustomNumberPickerV2 targetScorePicker;
    @BindView(R.id.maxScoreDivider)
    View maxScoreDivider;
    @BindView(R.id.targetScoreDivider)
    View targetScoreDivider;
    @BindView(R.id.cbPositionsDivider)
    View cbPositionsDivider;
    @BindView(R.id.obPositionsDivider)
    View obPositionsDivider;
    @BindView(R.id.targetPositionsDivider)
    View targetPositionsDivider;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.spinner)
    Spinner drillTypeSpinner;
    @BindView(R.id.obPositionsPicker)
    CustomNumberPickerV2 obPositionsPicker;
    @BindView(R.id.cbPositionsPicker)
    CustomNumberPickerV2 cbPositionsPicker;
    @BindView(R.id.targetPositionsPicker)
    CustomNumberPickerV2 targetPositionsPicker;
    private String photoPath;

    public static AddEditDrillFragment newInstance(String drillId) {
        AddEditDrillFragment fragment = new AddEditDrillFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_DRILL_ID, drillId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        presenter = new AddEditDrillPresenter(getArguments().getString(PARAM_DRILL_ID));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_drill, container, false);
        unbinder = ButterKnife.bind(this, view);
        obPositionsPicker.setValueChangedListener(obPositionsListener);
        cbPositionsPicker.setValueChangedListener(cbPositionsListener);
        targetPositionsPicker.setValueChangedListener(targetPositionsListener);
        obPositionsPicker.setMinValue(1);
        cbPositionsPicker.setMinValue(1);
        targetPositionsPicker.setMinValue(1);
        obPositionsPicker.setMaxValue(15);
        cbPositionsPicker.setMaxValue(15);
        targetPositionsPicker.setMaxValue(15);
        maxScorePicker.setValueChangedListener(maxValueListener);
        targetScorePicker.setValueChangedListener(targetValueListener);
        targetScorePicker.setMaxValue(maxScorePicker.getValue());
        toolbar.setNavigationOnClickListener(view1 -> Objects.requireNonNull(getActivity()).finish());
        toolbar.inflateMenu(R.menu.fragment_create_drill_menu);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_save) {
                presenter.saveDrill();
                return true;
            }
            return true;
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.setView(this);
        presenter.setTargetScore(targetScorePicker.getValue());
        presenter.setMaximumScore(maxScorePicker.getValue());
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Objects.requireNonNull(getContext()).getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        photoPath = image.getAbsolutePath();
        return image;
    }

    private Uri getUri(String path) {
        return FileProvider.getUriForFile(Objects.requireNonNull(getContext()), "com.brookmanholmes.drilltracker", new File(path));
    }

    private void startTakePictureActivity() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(Objects.requireNonNull(getContext()).getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Crashlytics.logException(ex);
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = getUri(photoPath);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, ACTION_TAKE_IMAGE);
            }
        }
    }

    private void startChooseImageActivity() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, ACTION_CHOOSE_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTION_TAKE_IMAGE && resultCode == Activity.RESULT_OK) {
            if (photoPath != null) {
                CropImage.activity(getUri(photoPath))
                        .start(Objects.requireNonNull(getContext()), this);
            }
        } else if (requestCode == ACTION_CHOOSE_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                CropImage.activity(data.getData())
                        .start(Objects.requireNonNull(getContext()), this);
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Uri resultUri = result.getUri();
            image.setImageURI(resultUri);
            onImageCropped(image);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }

    private void onMaximumScoreChanged(int value) {
        presenter.setMaximumScore(value);
    }

    private void onTargetScoreChanged(int value) {
        presenter.setTargetScore(value);
    }

    @OnTextChanged(value = R.id.input_drill_description, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void onDescriptionChanged(Editable editable) {
        presenter.setDrillDescription(editable.toString());
    }

    @OnTextChanged(value = R.id.input_drill_name, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void onDrillNameChanged(Editable editable) {
        presenter.setDrillName(editable.toString());
    }

    @OnItemSelected(R.id.spinner)
    void onDrillTypeSelected(int position) {
        presenter.setDrillType(transformSelectionToModel(position));
        switch (transformSelectionToModel(position)) {
            case BANKING:
            case AIMING:
            case KICKING:
                setMaxScoreVisibility(View.GONE);
                setTargetScoreVisibility(View.VISIBLE);
                setTargetPositionsVisibility(View.GONE);
                setCbPositionsVisibility(View.VISIBLE);
                setObPositionsVisibility(View.VISIBLE);
                targetScorePicker.setMaxValue(100);
                targetScorePicker.setStepValue(10);
                targetScorePicker.setValue(targetScorePicker.getValue() * 10);
                break;
            case SAFETY:
                setMaxScoreVisibility(View.GONE);
                setTargetScoreVisibility(View.GONE);
                setTargetPositionsVisibility(View.GONE);
                setCbPositionsVisibility(View.VISIBLE);
                setObPositionsVisibility(View.VISIBLE);
                break;
            case SPEED:
                setMaxScoreVisibility(View.GONE);
                setTargetScoreVisibility(View.GONE);
                setTargetPositionsVisibility(View.GONE);
                setCbPositionsVisibility(View.VISIBLE);
                setObPositionsVisibility(View.VISIBLE);
                break;
            case POSITIONAL:
                setMaxScoreVisibility(View.GONE);
                setTargetScoreVisibility(View.GONE);
                setTargetPositionsVisibility(View.VISIBLE);
                setCbPositionsVisibility(View.VISIBLE);
                setObPositionsVisibility(View.GONE);
                break;
            default:
                setTargetScoreVisibility(View.VISIBLE);
                setMaxScoreVisibility(View.VISIBLE);
                setTargetPositionsVisibility(View.VISIBLE);
                setObPositionsVisibility(View.VISIBLE);
                setCbPositionsVisibility(View.VISIBLE);
                targetScorePicker.setMaxValue(maxScorePicker.getValue());
                targetScorePicker.setValue(7);
                targetScorePicker.setStepValue(1);
        }
    }

    private DrillModel.Type transformSelectionToModel(int selection) {
        switch(selection) {
            case 0:
                return DrillModel.Type.ANY;
            case 5:
                return DrillModel.Type.POSITIONAL;
            case 1:
                return DrillModel.Type.AIMING;
            case 6:
                return DrillModel.Type.SAFETY;
            case 4:
                return DrillModel.Type.PATTERN;
            case 3:
                return DrillModel.Type.KICKING;
            case 2:
                return DrillModel.Type.BANKING;
            case 7:
                return DrillModel.Type.SPEED;
            default:
                throw new IllegalArgumentException("No such selection possible: " + selection);
        }
    }

    // TODO: 7/27/2017 add in ability to use a url
    @OnClick(R.id.image)
    void onDrillImageClicked() {
        new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                .setTitle(R.string.change_drill_diagram)
                .setItems(R.array.create_drill_array, (dialogInterface, i) -> {
                    switch (i) {
                        case 0:
                            startTakePictureActivity();
                            break;
                        case 1:
                            startChooseImageActivity();
                            break;
                    }
                })
                .create()
                .show();
    }

    private void onImageCropped(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, COMPRESSION_AMOUNT, outputStream);
        presenter.setImage(outputStream.toByteArray());
    }

    @Override
    public void showDrillDetailsView(DrillModel drill) {
        startActivity(DrillDetailsActivity.getIntent(
                getContext(),
                drill.id,
                drill.drillType,
                drill.imageUrl,
                drill.maxScore,
                drill.defaultTargetScore,
                drill.obPositions,
                drill.cbPositions,
                drill.targetPositions
        ));
        Objects.requireNonNull(getActivity()).finish();
    }

    @Override
    public void isDrillComplete(boolean isComplete) {
        MenuItem menuItem = toolbar.getMenu().findItem(R.id.action_save);
        menuItem.setEnabled(isComplete);
        menuItem.getIcon().setAlpha(isComplete ? 255 : 127);
    }

    @Override
    public void loadDrillData(DrillModel model) {
        drillName.setText(model.name);
        drillDescription.setText(model.description);
        maxScorePicker.setValue(model.maxScore);
        targetScorePicker.setValue(model.defaultTargetScore);

        drillTypeSpinner.setSelection(model.drillType.ordinal());
        drillTypeSpinner.setEnabled(false);
        obPositionsPicker.setValue(model.obPositions);
        cbPositionsPicker.setValue(model.cbPositions);
        targetPositionsPicker.setValue(model.targetPositions);
        toolbar.setTitle(R.string.title_edit_drill);
        ImageHandler.loadImage(image, model.imageUrl);
    }

    @Override
    public void finish() {
        Objects.requireNonNull(getActivity()).finish();
    }

    private void setMaxScoreVisibility(int visibility) {
        maxScorePicker.setVisibility(visibility);
        maxScoreDivider.setVisibility(visibility);
    }

    private void setTargetScoreVisibility(int visibility) {
        targetScorePicker.setVisibility(visibility);
        targetScoreDivider.setVisibility(visibility);
    }

    private void setObPositionsVisibility(int visibility) {
        obPositionsPicker.setVisibility(visibility);
        obPositionsDivider.setVisibility(visibility);
    }

    private void setCbPositionsVisibility(int visibility) {
        cbPositionsPicker.setVisibility(visibility);
        cbPositionsDivider.setVisibility(visibility);
    }

    private void setTargetPositionsVisibility(int visibility) {
        targetPositionsPicker.setVisibility(visibility);
        targetPositionsDivider.setVisibility(visibility);
    }
}
