package com.brookmanholmes.drilltracker.presentation.addeditdrill

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.brookmanholmes.drilltracker.R
import com.brookmanholmes.drilltracker.databinding.FragmentCreateDrillBinding
import com.brookmanholmes.drilltracker.presentation.drilldetail.DrillDetailsActivity
import com.brookmanholmes.drilltracker.presentation.model.DataCollectionModel
import com.brookmanholmes.drilltracker.presentation.model.DrillModel
import com.brookmanholmes.drilltracker.presentation.model.Type
import com.brookmanholmes.drilltracker.presentation.view.util.ArrayAdapterNoFilter
import com.brookmanholmes.drilltracker.presentation.view.util.ImageHandler
import com.theartofdev.edmodo.cropper.CropImage
import com.travijuu.numberpicker.library.Enums.ActionEnum
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Brookman Holmes on 7/23/2017.
 * TODO: make this look better
 */
class AddEditDrillFragment : Fragment(), AddEditDrillView {
    private lateinit var presenter: AddEditDrillPresenter
    private var _binding: FragmentCreateDrillBinding? = null
    private val binding get() = _binding!!

    lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = AddEditDrillPresenter(requireArguments().getString(PARAM_DRILL_ID))


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateDrillBinding.inflate(inflater, container, false)
        binding.let {
            it.inputDrillName.doAfterTextChanged { text -> presenter.setDrillName(text.toString()) }
            it.inputDrillDescription.doAfterTextChanged { text-> presenter.setDrillDescription(text.toString()) }
            it.image.setOnClickListener {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"

                startActivityForResult(intent, ACTION_CHOOSE_IMAGE)
            }
            it.shotDataCheckBox.setOnCheckedChangeListener { _, isChecked ->
                presenter.enableShotDataCollection(isChecked)
            }
            it.englishDataCheckBox.setOnCheckedChangeListener { _, isChecked ->
                presenter.enableEnglishDataCollection(isChecked)
            }
            it.speedDataCheckBox.setOnCheckedChangeListener { _, isChecked ->
                presenter.enableSpeedDataCollection(isChecked)
            }
            it.vSpinDataCheckBox.setOnCheckedChangeListener { _, isChecked ->
                presenter.enableVSpinDataCollection(isChecked)
            }
            it.distanceDataCheckBox.setOnCheckedChangeListener { _, isChecked ->
                presenter.enableDistanceDataCollection(isChecked)
            }
            it.drillTypeInputLayout.hint = "Drill Type"
            it.drillTypeSpinner.setAdapter(ArrayAdapterNoFilter(requireContext(), resources.getStringArray(R.array.drill_types).toList()))
            it.drillTypeSpinner.setOnItemClickListener { parent, view, position, id ->
                presenter.setDrillType(transformSelectionToModel(position))
            }
            it.cbPositionsPicker.setValueChangedListener { value: Int, action: ActionEnum? ->
                presenter.setCbPositions(value)
            }
            it.obPositionsPicker.setValueChangedListener { value: Int, action: ActionEnum? ->
                presenter.setCbPositions(value)
            }

            it.maxScorePicker.setValueChangedListener { value: Int, action: ActionEnum? ->
                presenter.setMaximumScore(value)
                binding.targetPositionsPicker.setMaxValue(value)
            }
            it.targetPositionsPicker.setValueChangedListener { value: Int, action: ActionEnum? ->
                presenter.setTargetPositions(value)
            }
            it.targetPositionsPicker.setMaxValue(it.maxScorePicker.value)

            it.toolbar.setNavigationOnClickListener {
                requireActivity().finish()
            }
            it.toolbar.inflateMenu(R.menu.fragment_create_drill_menu)
            it.toolbar.setOnMenuItemClickListener { item ->
                if (item.itemId == R.id.action_save) {
                    presenter.saveDrill()
                }
                return@setOnMenuItemClickListener true
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.setView(this)
        presenter.setTargetScore(0)
        presenter.setMaximumScore(0)
    }

    override fun onResume() {
        super.onResume()
        presenter.resume()
    }

    override fun onPause() {
        super.onPause()
        presenter.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    fun showToastMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(requireContext().packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        requireContext().packageName + ".provider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, ACTION_TAKE_IMAGE)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyMMdd_HHmmss").format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ACTION_TAKE_IMAGE && resultCode == Activity.RESULT_OK) {
            CropImage.activity(FileProvider
                .getUriForFile(requireContext(), "com.brookmanholmes.drilltracker", File(currentPhotoPath)))
                .setFixAspectRatio(true)
                .setAspectRatio(2,1)
                .start(requireActivity(), this)
        } else if (requestCode == ACTION_CHOOSE_IMAGE && resultCode == Activity.RESULT_OK) {
            CropImage.activity(data?.data)
                .setFixAspectRatio(true)
                .setAspectRatio(2,1)
                .start(requireActivity(), this)
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            binding.image.setImageURI(CropImage.getActivityResult(data).uri)
            val bitmap = (binding.image.drawable as BitmapDrawable).bitmap
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, COMPRESSION_AMOUNT, outputStream)
            presenter.setImage(outputStream.toByteArray())
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            45432 -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay!
                    Timber.d("Storage access granted")
                } else {
                    Timber.d("User disabled permission!  Can't select a photo!")
                }
            }
        }
    }

    private fun transformSelectionToModel(selection: Int): Type {
        return when (selection) {
            0 -> Type.ANY
            5 -> Type.POSITIONAL
            1 -> Type.AIMING
            6 -> Type.SAFETY
            4 -> Type.PATTERN
            3 -> Type.KICKING
            2 -> Type.BANKING
            7 -> Type.SPEED
            else -> throw IllegalArgumentException("No such selection possible: $selection")
        }
    }

    override fun showDrillDetailsView(drill: DrillModel) {
        startActivity(DrillDetailsActivity.getIntent(context, drill.id))
        requireActivity().finish()
    }

    override fun isDrillComplete(isComplete: Boolean) {
        val menuItem = binding.toolbar.menu.findItem(R.id.action_save)
        menuItem.isEnabled = isComplete
        menuItem.icon.alpha = if (isComplete) 255 else 127
    }

    override fun loadDrillData(model: DrillModel) {
        binding.inputDrillName.setText(model.name)
        binding.inputDrillDescription.setText(model.description)
        binding.maxScorePicker.value = model.maxScore
        binding.drillTypeSpinner.setText(resources.getStringArray(R.array.drill_types)[model.drillType.ordinal])
        binding.targetPositionsPicker.value = model.targetPositions
        binding.obPositionsPicker.value = model.obPositions
        binding.cbPositionsPicker.value = model.cbPositions
        binding.targetScorePicker.value = model.defaultTargetScore
        binding.toolbar.setTitle(R.string.title_edit_drill)
        ImageHandler.loadImage(binding.image, model.imageUrl)
        binding.distanceDataCheckBox.isChecked = model.dataToCollect.contains(DataCollectionModel.COLLECT_TARGET_DATA)
        binding.vSpinDataCheckBox.isChecked = model.dataToCollect.contains(DataCollectionModel.COLLECT_SPIN_DATA)
        binding.speedDataCheckBox.isChecked = model.dataToCollect.contains(DataCollectionModel.COLLECT_SPEED_DATA)
        binding.englishDataCheckBox.isChecked = model.dataToCollect.contains(DataCollectionModel.COLLECT_ENGLISH_DATA)
        binding.shotDataCheckBox.isChecked = model.dataToCollect.contains(DataCollectionModel.COLLECT_SHOT_DATA)
    }

    override fun finish() {
        requireActivity().finish()
    }

    companion object {
        private val TAG = AddEditDrillFragment::class.java.name
        private const val PARAM_DRILL_ID = "param_drill_id"
        private const val ACTION_TAKE_IMAGE = 101
        private const val ACTION_CHOOSE_IMAGE = 102
        private const val COMPRESSION_AMOUNT = 50

        @JvmStatic
        fun newInstance(drillId: String?): AddEditDrillFragment {
            val fragment = AddEditDrillFragment()
            val args = Bundle()
            args.putString(PARAM_DRILL_ID, drillId)
            fragment.arguments = args
            return fragment
        }
    }
}