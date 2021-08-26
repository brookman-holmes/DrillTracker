package com.brookmanholmes.drilltracker.presentation.addattempt

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.AutoCompleteTextView
import androidx.fragment.app.DialogFragment
import com.brookmanholmes.drilltracker.R
import com.brookmanholmes.drilltracker.databinding.DialogAddAttemptBinding
import com.brookmanholmes.drilltracker.presentation.model.*
import com.brookmanholmes.drilltracker.presentation.view.util.ArrayAdapterNoFilter
import com.brookmanholmes.drilltracker.presentation.view.util.setVisibleOrGone
import com.goodiebag.horizontalpicker.HorizontalPicker
import com.google.android.material.textfield.TextInputLayout
import java.util.*
import kotlin.collections.ArrayList

class AddAttemptDialog : DialogFragment(), AddAttemptView {
    private lateinit var presenter: AddAttemptPresenter
    private var _binding: DialogAddAttemptBinding? = null
    private val binding get() = _binding!!

    // values set via fragment arguments
    private var englishRequiredSelection = English.NONE
    private var speedRequiredSelection = Speed.THREE
    private var spinRequiredSelection = VSpin.CENTER
    private var cbPositionSelection = 1
    private var obPositionSelection = 1
    private var targetPositionSelection = 1
    private var dataToCollect = EnumSet.noneOf(DataCollectionModel::class.java)
    private lateinit var drillId: String
    override fun getDrillId(): String {
        return drillId
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = AddAttemptPresenter()

        obPositionSelection = requireArguments().getInt(PARAM_SELECTED_OB_POS)
        cbPositionSelection = requireArguments().getInt(PARAM_SELECTED_CB_POS)
        targetPositionSelection = requireArguments().getInt(PARAM_SELECTED_TARGET_POS)
        englishRequiredSelection = requireArguments().getSerializable(PARAM_SELECTED_ENGLISH) as English
        speedRequiredSelection = requireArguments().getSerializable(PARAM_SELECTED_SPEED) as Speed
        spinRequiredSelection = requireArguments().getSerializable(PARAM_SELECTED_V_SPIN) as VSpin
        val drillModel = requireArguments().getSerializable(PARAM_DRILL) as DrillModel
        drillId = drillModel.id
        dataToCollect.addAll(drillModel.dataToCollect)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogAddAttemptBinding.inflate(LayoutInflater.from(requireContext()), null, false)
        val dialogBuilder = AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme)
        with(dialogBuilder) {
            setTitle(R.string.dialog_add_attempt_title)
            setPositiveButton(android.R.string.ok) { _, _ ->
                presenter.addAttempt(
                    drillId,
                    getDistanceResult(),
                    getSpeedRequired(),
                    getSpinRequired(),
                    getEnglishRequired(),
                    getSpinResult(binding.layoutSpinResult.picker.selectedIndex),
                    getSpinResult(binding.layoutEnglishResult.picker.selectedIndex),
                    getSpinResult(binding.layoutSpeedResult.picker.selectedIndex),
                    SpinResult.NO_DATA,
                    getShotResult(),
                    targetPositionSelection,
                    0,
                    0,
                    obPositionSelection,
                    cbPositionSelection,
                    ArrayList()
                )
            }
            setNegativeButton(android.R.string.cancel, null)
            setView(binding.root)
        }

        presenter.setView(this)

        presenter.setDataToCollect(dataToCollect)

        binding.targetInputLayout.hint = "Target"
        binding.obInputLayout.hint = "Object Ball"
        binding.cbInputLayout.hint = "Cue Ball"

        setPickerItems()
        setPickerIndices()

        return dialogBuilder.create()
    }

    private fun getDistanceResult(): DistanceResult {
        return when (binding.layoutDistanceResult.picker.selectedIndex) {
            0 -> DistanceResult.ZERO
            1 -> DistanceResult.ONE_HALF
            2 -> DistanceResult.ONE
            3 -> DistanceResult.ONE_AND_HALF
            4 -> DistanceResult.OVER_ONE_AND_HALF
            else -> DistanceResult.NO_DATA
        }
    }

    private fun getSpeedRequired(): Speed {
        return when (binding.layoutSpeedRequired.picker.selectedIndex) {
            0 -> Speed.ONE
            1 -> Speed.TWO
            2 -> Speed.THREE
            3 -> Speed.FOUR
            4 -> Speed.FIVE
            else -> Speed.NO_DATA
        }
    }

    private fun getEnglishRequired(): English {
        return when (binding.layoutEnglishRequired.picker.selectedIndex) {
            0 -> English.FULL_INSIDE
            1 -> English.INSIDE
            2 -> English.NONE
            3 -> English.OUTSIDE
            4 -> English.FULL_OUTSIDE
            else -> English.NO_DATA
        }
    }

    private fun getSpinRequired(): VSpin {
        return when (binding.layoutSpinRequired.picker.selectedIndex) {
            0 -> VSpin.BOTTOM
            1 -> VSpin.CENTER
            2 -> VSpin.TOP
            else -> VSpin.NO_DATA
        }
    }

    private fun getSpinResult(index: Int): SpinResult {
        return when (index) {
            0 -> SpinResult.TOO_LITTLE
            1 -> SpinResult.CORRECT
            2 -> SpinResult.TOO_MUCH
            else -> SpinResult.NO_DATA
        }
    }

    private fun getShotResult(): ShotResult {
        return when (binding.layoutShotResult.picker.selectedIndex) {
            0 -> ShotResult.MISS_OVER_CUT
            1 -> ShotResult.MAKE_CENTER
            2 -> ShotResult.MAKE_UNDER_CUT
            else -> ShotResult.NO_DATA
        }
    }

    private fun setPickerIndices() {
        binding.layoutEnglishRequired.picker.selectedIndex = getSelectedIndex(englishRequiredSelection)
        binding.layoutSpeedRequired.picker.selectedIndex = getSelectedIndex(speedRequiredSelection)
        binding.layoutSpinRequired.picker.selectedIndex = getSelectedIndex(spinRequiredSelection)
        binding.layoutShotResult.picker.selectedIndex = getSelectedIndex(ShotResult.MAKE_CENTER)
        binding.layoutSpeedResult.picker.selectedIndex = getSelectedIndex(SpinResult.CORRECT)
        binding.layoutDistanceResult.picker.selectedIndex = getSelectedIndex(DistanceResult.ZERO)
        binding.layoutEnglishResult.picker.selectedIndex = getSelectedIndex(SpinResult.CORRECT)
        binding.layoutSpinResult.picker.selectedIndex = getSelectedIndex(SpinResult.CORRECT)
    }

    private fun setPickerItems() {
        setPickerItems(
            binding.layoutEnglishRequired.picker, listOf(
                "Full inside", "Inside", "None", "Outside", "Full" +
                        " outside"
            )
        )
        setPickerItems(binding.layoutSpeedRequired.picker, listOf("1", "2", "3", "4", "5"))
        setPickerItems(binding.layoutShotResult.picker, listOf("Over cut", "Make", "Under cut"))
        setPickerItems(binding.layoutSpinRequired.picker, listOf("Bottom", "Center", "Top"))
        setPickerItems(binding.layoutSpeedResult.picker, listOf("Too soft", "Correct", "Too hard"))
        setPickerItems(binding.layoutEnglishResult.picker, listOf("Too little", "Correct", "Too much"))
        setPickerItems(binding.layoutSpinResult.picker, listOf("Too low", "Correct", "Too high"))
        setPickerItems(binding.layoutDistanceResult.picker, listOf("In target", ".5", "1", "1.5", "1.5+"))

    }

    private fun setPickerItems(picker: HorizontalPicker, items: Collection<String>) {
        picker.items = items.map {
            HorizontalPicker.TextItem(it)
        }
    }

    private fun getSelectedIndex(distanceResult: DistanceResult): Int {
        return when (distanceResult) {
            DistanceResult.ZERO -> 0
            DistanceResult.ONE_HALF -> 1
            DistanceResult.ONE -> 2
            DistanceResult.ONE_AND_HALF -> 3
            DistanceResult.OVER_ONE_AND_HALF -> 4
            DistanceResult.NO_DATA -> 0
        }
    }

    private fun getSelectedIndex(spinResult: SpinResult): Int {
        return when (spinResult) {
            SpinResult.TOO_LITTLE -> 0
            SpinResult.CORRECT -> 1
            SpinResult.TOO_MUCH -> 2
            SpinResult.NO_DATA -> 1
        }
    }

    private fun getSelectedIndex(vSpinUsed: VSpin): Int {
        return when (vSpinUsed) {
            VSpin.BOTTOM -> 0
            VSpin.CENTER -> 1
            VSpin.TOP -> 2
            VSpin.NO_DATA -> 1
            VSpin.ANY -> 1
        }
    }

    private fun getSelectedIndex(englishSelection: English): Int {
        return when(englishSelection) {
            English.ANY -> 2
            English.FULL_INSIDE -> 0
            English.INSIDE -> 1
            English.NONE -> 2
            English.OUTSIDE -> 3
            English.FULL_OUTSIDE -> 4
            English.NO_DATA -> 2
        }
    }

    private fun getSelectedIndex(speed: Speed): Int {
        return when (speed) {
            Speed.ONE -> 0
            Speed.TWO -> 1
            Speed.THREE -> 2
            Speed.FOUR -> 3
            Speed.FIVE -> 4
            Speed.NO_DATA -> 5
            Speed.ANY -> 2
        }
    }

    private fun getSelectedIndex(shotResult: ShotResult): Int {
        return when (shotResult) {
            ShotResult.MISS_OVER_CUT -> 0
            ShotResult.MAKE_OVER_CUT -> 1
            ShotResult.MAKE_CENTER -> 1
            ShotResult.MAKE_UNDER_CUT -> 1
            ShotResult.MISS_UNDER_CUT -> 2
            ShotResult.NO_DATA -> 2
            ShotResult.ANY -> 2
        }
    }

    override fun loadDrillData(drillModel: DrillModel) {
    }

    override fun setTargetPositions(positions: Int) {
        setSpinnerValues(binding.targetPositionsSpinner, binding.targetInputLayout, positions, targetPositionSelection)
    }

    override fun setCbPositions(positions: Int) {
        setSpinnerValues(binding.cbPositionsSpinner, binding.cbInputLayout, positions, cbPositionSelection)
    }

    override fun setObPositions(positions: Int) {
        setSpinnerValues(binding.obPositionsSpinner, binding.obInputLayout, positions, obPositionSelection)
    }

    private fun setSpinnerValues(textView: AutoCompleteTextView, textInputLayout: TextInputLayout, positions: Int, defaultPosition: Int) {
        if (positions > 1) {
            textInputLayout.setVisibleOrGone(true)
            setupSpinner(textView, positions)
            textView.setText(defaultPosition.toString())
        } else {
            textInputLayout.setVisibleOrGone(false)
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun setupSpinner(textView: AutoCompleteTextView?, size: Int) {
        val list = buildList {
            for (i in 1..size) {
                add(i.toString())
            }
        }
        if (textView?.adapter == null || textView.adapter?.count != size) {
            textView?.setAdapter(ArrayAdapterNoFilter(requireContext(), list))
            textView?.setText(list[0])
        }
    }

    override fun setShowEnglishData(visible: Boolean) {
        _binding?.layoutEnglishRequired?.root?.setVisibleOrGone(visible)
        _binding?.layoutEnglishResult?.root?.setVisibleOrGone(visible)
    }

    override fun setShowSpeedData(visible: Boolean) {
        _binding?.layoutSpeedRequired?.root?.setVisibleOrGone(visible)
        _binding?.layoutSpeedResult?.root?.setVisibleOrGone(visible)
    }

    override fun setShowTargetDistanceData(visible: Boolean) {
        _binding?.layoutDistanceResult?.root?.setVisibleOrGone(visible)
    }

    override fun setShowSpinData(visible: Boolean) {
        _binding?.layoutSpinRequired?.root?.setVisibleOrGone(visible)
        _binding?.layoutSpinResult?.root?.setVisibleOrGone(visible)
    }

    override fun setShowShotData(visible: Boolean) {
        _binding?.layoutShotResult?.root?.setVisibleOrGone(visible)
    }

    companion object {
        const val PARAM_DRILL = "param_drill"
        const val PARAM_SELECTED_CB_POS = "param_selected_cb_pos"
        const val PARAM_DRILL_ID = "param_drill_id"
        const val PARAM_SELECTED_OB_POS = "param_selected_ob_pos"
        const val PARAM_SELECTED_TARGET_POS = "param_selected_target_pos"
        const val PARAM_SELECTED_ENGLISH = "param_selected_english"
        const val PARAM_SELECTED_SPEED = "param_selected_speed"
        const val PARAM_SELECTED_V_SPIN = "param_v_spin_used"
    }

}