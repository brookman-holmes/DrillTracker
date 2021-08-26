package com.brookmanholmes.drilltracker.presentation.drilldetail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import com.brookmanholmes.drilltracker.R
import com.brookmanholmes.drilltracker.databinding.FragmentDrillDetailsBinding
import com.brookmanholmes.drilltracker.presentation.addattempt.AddAttemptDialog
import com.brookmanholmes.drilltracker.presentation.addattempt.AddAttemptDialog.Companion.PARAM_DRILL
import com.brookmanholmes.drilltracker.presentation.addattempt.AddAttemptDialog.Companion.PARAM_DRILL_ID
import com.brookmanholmes.drilltracker.presentation.addattempt.AddAttemptDialog.Companion.PARAM_SELECTED_CB_POS
import com.brookmanholmes.drilltracker.presentation.addattempt.AddAttemptDialog.Companion.PARAM_SELECTED_ENGLISH
import com.brookmanholmes.drilltracker.presentation.addattempt.AddAttemptDialog.Companion.PARAM_SELECTED_OB_POS
import com.brookmanholmes.drilltracker.presentation.addattempt.AddAttemptDialog.Companion.PARAM_SELECTED_SPEED
import com.brookmanholmes.drilltracker.presentation.addattempt.AddAttemptDialog.Companion.PARAM_SELECTED_TARGET_POS
import com.brookmanholmes.drilltracker.presentation.addattempt.AddAttemptDialog.Companion.PARAM_SELECTED_V_SPIN
import com.brookmanholmes.drilltracker.presentation.addeditdrill.AddEditDrillActivity
import com.brookmanholmes.drilltracker.presentation.model.*
import com.brookmanholmes.drilltracker.presentation.view.setDistanceChart
import com.brookmanholmes.drilltracker.presentation.view.setEmptyDistanceChart
import com.brookmanholmes.drilltracker.presentation.view.setEmptySpinChart
import com.brookmanholmes.drilltracker.presentation.view.setSpinResultPieChart
import com.brookmanholmes.drilltracker.presentation.view.util.ArrayAdapterNoFilter
import com.brookmanholmes.drilltracker.presentation.view.util.ImageHandler
import com.brookmanholmes.drilltracker.presentation.view.util.setVisibleOrGone

class DrillDetailsFragment : Fragment(), DrillDetailsView {
    private lateinit var presenter: DrillDetailsContract
    private var _binding: FragmentDrillDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = DrillDetailsPresenter()
        requireArguments().getString(PARAM_DRILL_ID)?.let { presenter.initialize(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDrillDetailsBinding.inflate(inflater, container, false)
        presenter.setView(this)
        binding.fab.setOnClickListener {
            presenter.onAddAttemptClicked()
        }
        setupToolbar()

        binding.speedInputLayout.hint = "Speed used"
        binding.englishInputLayout.hint = "English used"
        binding.cbInputLayout.hint = "Cue ball"
        binding.obInputLayout.hint = "Object ball"
        binding.targetInputLayout.hint = "Target"
        binding.vSpinUsedInputLayout.hint = "Vertical spin used"
        binding.shotInputLayout.hint = "Shot result"
        binding.speedSpinner.setAdapter(ArrayAdapterNoFilter(requireContext(), listOf("Any", "1", "2", "3", "4", "5")))
        binding.speedSpinner.setText("Any")
        binding.vSpinUsedSpinner.setAdapter(ArrayAdapterNoFilter(requireContext(), listOf("Any", "Bottom", "Center", "Top")))
        binding.vSpinUsedSpinner.setText("Any")
        binding.englishSpinner.setAdapter(ArrayAdapterNoFilter(requireContext(), listOf("Any", "Full Inside", "Inside", "None", "Outside", "Full Outside")))
        binding.englishSpinner.setText("Any")
        binding.shotSpinner.setAdapter(ArrayAdapterNoFilter(requireContext(), listOf("Any", "Over cut", "Make", "Under cut")))
        binding.shotSpinner.setText("Any")
        binding.shotSpinner.setOnItemClickListener { parent, view, position, id ->
            when (position) {
                0 -> presenter.setSelectedShotResult(ShotResult.ANY)
                1 -> presenter.setSelectedShotResult(ShotResult.MISS_OVER_CUT)
                2 -> presenter.setSelectedShotResult(ShotResult.MAKE_CENTER)
                3 -> presenter.setSelectedShotResult(ShotResult.MISS_UNDER_CUT)
            }
        }
        binding.vSpinUsedSpinner.setOnItemClickListener { parent, view, position, id ->
            when (position) {
                1 -> presenter.setVSpinUsed(VSpin.BOTTOM)
                2 -> presenter.setVSpinUsed(VSpin.CENTER)
                3 -> presenter.setVSpinUsed(VSpin.TOP)
                else -> presenter.setVSpinUsed(VSpin.ANY)
            }
        }
        binding.targetPositionsSpinner.setOnItemClickListener { parent, view, position, id ->
            presenter.setSelectedTargetPosition(position)
        }
        binding.obPositionsSpinner.setOnItemClickListener { parent, view, position, id ->
            presenter.setSelectedObPosition(position)
        }
        binding.cbPositionsSpinner.setOnItemClickListener { parent, view, position, id ->
            presenter.setSelectedCbPosition(position)
        }
        binding.englishSpinner.setOnItemClickListener { parent, view, position, id ->
            when (position) {
                1 -> presenter.setSelectedEnglish(English.FULL_INSIDE)
                2 -> presenter.setSelectedEnglish(English.INSIDE)
                3 -> presenter.setSelectedEnglish(English.NONE)
                4 -> presenter.setSelectedEnglish(English.OUTSIDE)
                5 -> presenter.setSelectedEnglish(English.FULL_OUTSIDE)
                else -> presenter.setSelectedEnglish(English.ANY)
            }
        }
        binding.speedSpinner.setOnItemClickListener { parent, view, position, id ->
            when (position) {
                1 -> presenter.setSelectedSpeed(Speed.ONE)
                2 -> presenter.setSelectedSpeed(Speed.TWO)
                3 -> presenter.setSelectedSpeed(Speed.THREE)
                4 -> presenter.setSelectedSpeed(Speed.FOUR)
                5 -> presenter.setSelectedSpeed(Speed.FIVE)
                else -> presenter.setSelectedSpeed(Speed.ANY)
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        presenter.initialize(requireArguments().getString(PARAM_DRILL_ID, "error, no value for PARAM_DRILL_ID"))
    }

    private fun setupToolbar() {
        binding.toolbar.let { it ->
            it.inflateMenu(R.menu.fragment_drill_details_menu)
            it.setNavigationIcon(R.drawable.ic_arrow_back)
            it.setNavigationOnClickListener {
                requireActivity().finish()
            }
            it.setOnMenuItemClickListener { that ->
                if (that.itemId == R.id.ic_edit) {
                    startActivity(
                        AddEditDrillActivity.newInstance(
                            requireContext(),
                            requireArguments().getString(PARAM_DRILL_ID)
                        )
                    )
                } else if (that.itemId == R.id.ic_undo_attempt) {
                    presenter.onUndoClicked()
                }
                true
            }
        }
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showRetry() {
    }

    override fun hideRetry() {
    }

    override fun showError(message: String?) {

    }

    override fun context(): Context {
        return requireContext()
    }

    override fun setObPositionsSpinnerVisibility(visible: Boolean) {
        _binding?.obInputLayout?.setVisibleOrGone(visible)
    }

    override fun setCbPositionsSpinnerVisibility(visible: Boolean) {
        _binding?.cbInputLayout?.setVisibleOrGone(visible)
    }

    override fun setTargetPositionsVisibility(visible: Boolean) {
        _binding?.targetInputLayout?.setVisibleOrGone(visible)
    }

    override fun setShowEnglishData(visible: Boolean) {
        _binding?.englishData?.let {
            it.title.text = "English Data"
            setEmptySpinChart(it.spinChart)
            setEmptySpinChart(it.spinHistoryChart)
            it.root.setVisibleOrGone(visible)
        }
        _binding?.englishInputLayout?.setVisibleOrGone(visible)
    }

    override fun setShowSpeedData(visible: Boolean) {
        _binding?.speedInputLayout?.setVisibleOrGone(visible)
        _binding?.speedData?.let {
            it.title.text = "Speed Data"
            setEmptySpinChart(it.spinChart)
            setEmptySpinChart(it.spinHistoryChart)
            it.root.setVisibleOrGone(visible)
        }
    }

    override fun setShowDistanceData(visible: Boolean) {
        _binding?.distanceData?.let {
            setEmptyDistanceChart(it.targetDataChart)
            it.root.setVisibleOrGone(visible)
        }
    }

    override fun setShowSpinData(visible: Boolean) {
        _binding?.vSpinUsedInputLayout?.setVisibleOrGone(visible)
        _binding?.spinData?.let {
            it.title.text = "Spin Data"
            setEmptySpinChart(it.spinChart)
            setEmptySpinChart(it.spinHistoryChart)
            it.root.setVisibleOrGone(visible)
        }
    }

    override fun setShowShotData(visible: Boolean) {
        _binding?.aimData?.root?.setVisibleOrGone(visible)
        _binding?.shotInputLayout?.setVisibleOrGone(visible)
    }

    override fun setShotData(today: ShotDataModel, history: ShotDataModel) {
        _binding?.aimData?.let {
            it.sessionAttempts.text = today.attempts.toString()
            it.sessionMakes.text = today.makes.toString()
            it.sessionOverCuts.text = today.missThin.toString()
            it.sessionUnderCuts.text = today.missThick.toString()
            it.sessionAvg.setText("%.2f".format(today.successRate))

            it.lifetimeAttempts.text = history.attempts.toString()
            it.lifetimeAvg.text = "%.2f".format(history.successRate)
            it.lifetimeMakes.text = history.makes.toString()
            it.lifetimeOverCuts.text = history.missThin.toString()
            it.lifetimeUnderCuts.text = history.missThick.toString()
        }
    }

    override fun setSpinData(today: SpinDataModel, history: SpinDataModel) {
        _binding?.spinData?.let {
            it.sessionAttempts.text = today.spinResultModel.attempts.toString()
            it.sessionHard.text = today.spinResultModel.more.toString()
            it.sessionSoft.text = today.spinResultModel.less.toString()
            it.sessionMakes.text = today.spinResultModel.correct.toString()
            it.sessionAvg.text = "%.2f".format(today.successRate)
            it.lifetimeAttempts.text = history.spinResultModel.attempts.toString()
            it.lifetimeHard.text = history.spinResultModel.more.toString()
            it.lifetimeSoft.text = history.spinResultModel.less.toString()
            it.lifetimeMakes.text = history.spinResultModel.correct.toString()
            it.lifetimeAvg.text = "%.2f".format(history.successRate)
            setSpinResultPieChart(it.spinChart, today.spinResultModel)
            setSpinResultPieChart(it.spinHistoryChart, history.spinResultModel)
        }
    }

    override fun setEnglishData(today: EnglishDataModel, history: EnglishDataModel) {
        _binding?.englishData?.let {
            it.sessionAttempts.text = today.spinResultModel.attempts.toString()
            it.sessionHard.text = today.spinResultModel.more.toString()
            it.sessionSoft.text = today.spinResultModel.less.toString()
            it.sessionMakes.text = today.spinResultModel.correct.toString()
            it.sessionAvg.text = "%.2f".format(today.successRate)
            it.lifetimeAttempts.text = history.spinResultModel.attempts.toString()
            it.lifetimeHard.text = history.spinResultModel.more.toString()
            it.lifetimeSoft.text = history.spinResultModel.less.toString()
            it.lifetimeMakes.text = history.spinResultModel.correct.toString()
            it.lifetimeAvg.text = "%.2f".format(history.successRate)
            setSpinResultPieChart(it.spinChart, today.spinResultModel)
            setSpinResultPieChart(it.spinHistoryChart, history.spinResultModel)
        }
    }

    override fun setSpeedData(today: SpeedDataModel, history: SpeedDataModel) {
        _binding?.speedData?.let {
            it.sessionAttempts.text = today.spinResultModel.attempts.toString()
            it.sessionHard.text = today.spinResultModel.more.toString()
            it.sessionSoft.text = today.spinResultModel.less.toString()
            it.sessionMakes.text = today.spinResultModel.correct.toString()
            it.sessionAvg.text = "%.2f".format(today.successRate)
            it.lifetimeAttempts.text = history.spinResultModel.attempts.toString()
            it.lifetimeHard.text = history.spinResultModel.more.toString()
            it.lifetimeSoft.text = history.spinResultModel.less.toString()
            it.lifetimeMakes.text = history.spinResultModel.correct.toString()
            it.lifetimeAvg.text = "%.2f".format(history.successRate)
            setSpinResultPieChart(it.spinChart, today.spinResultModel)
            setSpinResultPieChart(it.spinHistoryChart, history.spinResultModel)
        }
    }

    override fun setDistanceData(today: TargetDataModel, history: TargetDataModel) {
        setDistanceChart(binding.distanceData.targetDataChart, today)
        binding.distanceData.sessionError.text = "%.2f".format(today.averageError)
        binding.distanceData.lifetimeError.text = "%.2f".format(history.averageError)
    }

    override fun setName(name: String) {
        _binding?.toolbar?.title = name
    }

    override fun setImage(imageUrl: String) {
        ImageHandler.loadImage(_binding?.image, imageUrl)
    }

    override fun setDescription(description: String) {
        _binding?.description?.text = description
    }

    override fun setPositionSpinners(cbPositions: Int, obPositions: Int, targetPositions: Int) {
        setupSpinner(_binding?.targetPositionsSpinner, targetPositions)
        setupSpinner(_binding?.cbPositionsSpinner, cbPositions)
        setupSpinner(_binding?.obPositionsSpinner, obPositions)
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun setupSpinner(textView: AutoCompleteTextView?, size: Int) {
        val list = buildList {
            add("Any")
            for (i in 1..size) {
                add(i.toString())
            }
        }
        if (textView?.adapter == null || textView.adapter?.count != size) {
            textView?.setAdapter(ArrayAdapterNoFilter(requireContext(), list))
            textView?.setText(list[0])
        }
    }

    override fun renderDrill(drill: DrillModel?) {
    }

    override fun showAddAttemptDialog(
        drill: DrillModel,
        selectedCbPosition: Int,
        selectedObPosition: Int,
        selectedTargetPosition: Int,
        selectedVSpin: VSpin,
        selectedSpeed: Speed,
        selectedEnglish: English,
        selectedPattern: MutableList<Int>?
    ) {
        val dialogFragment = AddAttemptDialog()
        val args = Bundle()

        args.putInt(PARAM_SELECTED_CB_POS, selectedCbPosition)
        args.putInt(PARAM_SELECTED_OB_POS, selectedObPosition)
        args.putInt(PARAM_SELECTED_TARGET_POS, selectedTargetPosition)
        args.putSerializable(PARAM_SELECTED_ENGLISH, selectedEnglish)
        args.putSerializable(PARAM_SELECTED_SPEED, selectedSpeed)
        args.putSerializable(PARAM_SELECTED_V_SPIN, selectedVSpin)
        args.putSerializable(PARAM_SELECTED_SPEED, selectedSpeed);
        args.putSerializable(PARAM_DRILL, drill);
        dialogFragment.arguments = args

        dialogFragment.show(requireFragmentManager(), DrillDetailsFragment::class.simpleName)
    }
}