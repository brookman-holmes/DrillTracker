package com.brookmanholmes.drilltracker.presentation.addeditdrill;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.domain.interactor.AddDrill;
import com.brookmanholmes.drilltracker.domain.interactor.GetDrillDetails;
import com.brookmanholmes.drilltracker.domain.interactor.UpdateDrill;
import com.brookmanholmes.drilltracker.presentation.base.BaseFragment;
import com.brookmanholmes.drilltracker.presentation.drilldetail.DrillDetailsActivity;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;
import com.brookmanholmes.drilltracker.presentation.view.CustomNumberPicker;
import com.brookmanholmes.drilltracker.presentation.view.util.ImageHandler;
import com.squareup.picasso.Callback;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;

/**
 * Created by Brookman Holmes on 7/23/2017.
 */

public class AddEditDrillFragment extends BaseFragment<AddEditDrillPresenter> implements AddEditDrillView {
    private static final String TAG = AddEditDrillFragment.class.getName();
    private static final String PARAM_DRILL_ID = "param_drill_id";

    private static final int ACTION_TAKE_IMAGE = 101;
    private static final int ACTION_CHOOSE_IMAGE = 102;
    private static final int COMPRESSION_AMOUNT = 50;


    private final CustomNumberPicker.OnValueChangeListener targetValueListener = new CustomNumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(int oldVal, int newVal) {
            onTargetScoreChanged(newVal);
        }
    };
    private final CustomNumberPicker.OnValueChangeListener maxValueListener = new CustomNumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(int oldVal, int newVal) {
            onMaximumScoreChanged(newVal);
        }
    };

    @BindView(R.id.input_drill_name)
    EditText drillName;
    @BindView(R.id.input_drill_description)
    EditText drillDescription;
    @BindView(R.id.maxScoreNumberPicker)
    CustomNumberPicker maxScorePicker;
    @BindView(R.id.targetScoreNumberPicker)
    CustomNumberPicker targetScorePicker;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.spinner)
    Spinner drillTypeSpinner;

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
        presenter = new AddEditDrillPresenter(
                new AddDrill(getDrillRepository()),
                new GetDrillDetails(getDrillRepository()),
                new UpdateDrill(getDrillRepository()),
                getArguments().getString(PARAM_DRILL_ID)
        );
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_drill, container, false);
        unbinder = ButterKnife.bind(this, view);
        maxScorePicker.setOnValueChangedListener(maxValueListener);
        targetScorePicker.setOnValueChangedListener(targetValueListener);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        toolbar.inflateMenu(R.menu.fragment_create_drill_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_save:
                        presenter.saveDrill();
                        return true;
                    default:
                        return true;
                }
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.setView(this);
        presenter.setTargetScore(targetScorePicker.getValue());
        presenter.setMaximumScore(maxScorePicker.getValue());
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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
        return FileProvider.getUriForFile(getContext(), "com.brookmanholmes.drilltracker", new File(path));
    }

    private void startTakePictureActivity() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.i(TAG, "startTakePictureActivity: " + ex);
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
                        .start(getContext(), this);
            }
        } else if (requestCode == ACTION_CHOOSE_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                CropImage.activity(data.getData())
                        .start(getContext(), this);
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
    }

    private DrillModel.Type transformSelectionToModel(int selection) {
        switch(selection) {
            case 0:
                return DrillModel.Type.ANY;
            case 1:
                return DrillModel.Type.POSITIONAL;
            case 2:
                return DrillModel.Type.AIMING;
            case 3:
                return DrillModel.Type.SAFETY;
            case 4:
                return DrillModel.Type.PATTERN;
            case 5:
                return DrillModel.Type.KICKING;
            case 6:
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
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.change_drill_diagram)
                .setItems(R.array.create_drill_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch(i) {
                            case 0:
                                startTakePictureActivity();
                                break;
                            case 1:
                                startChooseImageActivity();
                                break;
                        }
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
    public void showDrillDetailsView(String drillId, int maxScore, int targetScore) {
        startActivity(DrillDetailsActivity.getIntent(getContext(), drillId, maxScore, targetScore));
        getActivity().finish();
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
        toolbar.setTitle(R.string.title_edit_drill);
        ImageHandler.loadImage(image, model.imageUrl, new Callback() {
            @Override
            public void onSuccess() {
                Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, COMPRESSION_AMOUNT, outputStream);
                presenter.setImage(outputStream.toByteArray());
            }

            @Override
            public void onError() {
                presenter.setImage(null);
            }
        });
    }

    @Override
    public void finish() {
        getActivity().finish();
    }
}