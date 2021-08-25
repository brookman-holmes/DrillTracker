package com.brookmanholmes.drilltracker.presentation.drilldetail

import com.brookmanholmes.drilltracker.data.repository.datasource.DataStoreFactory
import com.brookmanholmes.drilltracker.domain.Attempt
import com.brookmanholmes.drilltracker.domain.Drill
import com.brookmanholmes.drilltracker.domain.exception.DefaultErrorBundle
import com.brookmanholmes.drilltracker.domain.exception.ErrorBundle
import com.brookmanholmes.drilltracker.domain.interactor.*
import com.brookmanholmes.drilltracker.presentation.exception.ErrorMessageFactory
import com.brookmanholmes.drilltracker.presentation.mapper.DrillModelDataMapper
import com.brookmanholmes.drilltracker.presentation.model.*
import timber.log.Timber
import java.util.*

/**
 * Created by Brookman Holmes on 7/11/2017.
 */
internal class DrillDetailsPresenter : DrillDetailsContract {
    private val getDrillDetailsUseCase: GetDrillDetails
    private val deleteAttemptUseCase: DeleteAttempt
    private val getAttemptsUseCase: GetAttemptsUseCase
    private var view: DrillDetailsView? = null
    private var drill: DrillModel? = null
    private var attempts: Collection<AttemptModel> = ArrayList()
    private var selectedCbPosition = 0
    private var selectedObPosition = 0
    private var selectedTargetPosition = 0
    private var selectedSpin = VSpin.ANY
    private var selectedSpeed = Speed.ANY
    private var selectedEnglish = English.ANY
    private val selectedPattern: MutableList<Int> = ArrayList()
    private var selectedShotResult = ShotResult.ANY

    constructor() {
        getDrillDetailsUseCase = GetDrillDetails(DataStoreFactory.getDrillRepo())
        deleteAttemptUseCase = DeleteAttempt(DataStoreFactory.getDrillRepo())
        getAttemptsUseCase = GetAttemptsUseCase(DataStoreFactory.getDrillRepo())
    }

    private constructor(getDrillDetailsUseCase: GetDrillDetails, deleteAttemptUseCase: DeleteAttempt, getAttemptsUseCase: GetAttemptsUseCase) {
        this.getDrillDetailsUseCase = getDrillDetailsUseCase
        this.deleteAttemptUseCase = deleteAttemptUseCase
        this.getAttemptsUseCase = getAttemptsUseCase
    }

    override fun setView(view: DrillDetailsView) {
        this.view = view
    }

    override fun resume() {}
    override fun pause() {}
    override fun destroy() {
        getDrillDetailsUseCase.dispose()
        view = null
    }

    override fun initialize(drillId: String) {
        hideViewRetry()
        showViewLoading()
        getDrillDetails(drillId)
    }

    private fun getDrillDetails(drillId: String) {
        getDrillDetailsUseCase.execute(
            DrillDetailsObserver(),
            GetDrillDetails.Params.forDrill(drillId)
        )
        getAttemptsUseCase.execute(GetAttemptsObserver(),
            AttemptFilter(drillId)
        )
    }

    private fun showViewLoading() {
        view?.showLoading()
    }

    private fun hideViewLoading() {
        view?.hideLoading()
    }

    private fun showViewRetry() {
        view?.showRetry()
    }

    private fun hideViewRetry() {
        view?.hideRetry()
    }

    private fun showErrorMessage(errorBundle: ErrorBundle) {
        val error = ErrorMessageFactory.create(errorBundle.exception)
        view?.showError(error)
    }

    private fun showDrillDetailsInView(drill: DrillModel) {
        this.drill = drill
        view?.let {
            it.setDescription(drill.description)
            it.setImage(drill.imageUrl)
            it.setCbPositionsSpinnerVisibility(drill.cbPositions > 1)
            it.setObPositionsSpinnerVisibility(drill.obPositions > 1)
            it.setTargetPositionsVisibility(drill.targetPositions > 1)
            it.setName(drill.name)
            it.setPositionSpinners(drill.cbPositions, drill.obPositions, drill.targetPositions)
            it.setShowEnglishData(drill.dataToCollect.contains(DataCollectionModel.COLLECT_ENGLISH_DATA))
            it.setShowShotData(drill.dataToCollect.contains(DataCollectionModel.COLLECT_SHOT_DATA))
            it.setShowSpeedData(drill.dataToCollect.contains(DataCollectionModel.COLLECT_SPEED_DATA))
            it.setShowSpinData(drill.dataToCollect.contains(DataCollectionModel.COLLECT_SPIN_DATA))
            it.setShowTargetDistanceData(drill.dataToCollect.contains(DataCollectionModel.COLLECT_TARGET_DATA))
        }
    }

    private fun loadAttemptData(attempts: Collection<AttemptModel>) {
        val filteredModels = attempts.filter {
            it.filterByCb(selectedCbPosition)
                    && it.filterByTarget(selectedTargetPosition)
                    && it.filterByOb(selectedObPosition)
                    && it.filterBySpeedRequired(selectedSpeed)
                    && it.filterByEnglishRequired(selectedEnglish)
                    && it.filterBySpinRequired(selectedSpin)
                    && it.filterByShotResult(selectedShotResult)
        }



        val todayModels = filteredModels.filter {
            it.filterByToday()
        }


        val historyModels = filteredModels.filter {
            it.filterByHistory()
        }

        if (collectData(DataCollectionModel.COLLECT_TARGET_DATA) && collectData(DataCollectionModel.COLLECT_SPEED_DATA)) {
            view?.setDistanceDataWithSpeed(todayModels.toTargetDataModel(), historyModels.toTargetDataModel())
        } else if (collectData(DataCollectionModel.COLLECT_TARGET_DATA)){
            view?.setDistanceData(todayModels.toTargetDataModel(), historyModels.toTargetDataModel())
        }

        if (collectData(DataCollectionModel.COLLECT_SHOT_DATA)) {
            view?.setShotData(todayModels.toShotDataModel(), historyModels.toShotDataModel())
        }

        if (collectData(DataCollectionModel.COLLECT_ENGLISH_DATA)) {
            view?.setEnglishData(EnglishDataModel(todayModels), EnglishDataModel(historyModels))
        }

        if (collectData(DataCollectionModel.COLLECT_SPIN_DATA)) {
            view?.setSpinData(SpinDataModel(todayModels), SpinDataModel(historyModels))
        }

        if (collectData(DataCollectionModel.COLLECT_SPEED_DATA)) {
            view?.setSpeedData(SpeedDataModel(todayModels), SpeedDataModel(historyModels))
        }
    }

    private fun collectData(dataToCollect: DataCollectionModel): Boolean {
        return drill?.dataToCollect?.contains(dataToCollect) == true
    }

    override fun onUndoClicked() {
        deleteAttemptUseCase.execute(DeleteAttemptObserver(), drill?.id)
    }

    override fun onAddAttemptClicked() {
        if (drill != null)
            view?.showAddAttemptDialog(
                drill!!,
                if (selectedCbPosition == 0) 1 else selectedCbPosition,
                if (selectedObPosition == 0) 1 else selectedObPosition,
                if (selectedTargetPosition == 0) 1 else selectedTargetPosition,
                selectedSpin,
                selectedSpeed,
                selectedEnglish,
                selectedPattern
            )
    }

    override fun setSelectedCbPosition(position: Int) {
        selectedCbPosition = position
        loadAttemptData(attempts)
    }

    override fun setSelectedObPosition(position: Int) {
        selectedObPosition = position
        loadAttemptData(attempts)
    }

    override fun setSelectedTargetPosition(position: Int) {
        selectedTargetPosition = position
        loadAttemptData(attempts)
    }

    override fun setSelectedEnglish(english: English) {
        selectedEnglish = english
        loadAttemptData(attempts)
    }

    override fun setSelectedPattern(pattern: List<Int>) {
        selectedPattern.clear()
        selectedPattern.addAll(pattern)
        loadAttemptData(attempts)
    }

    override fun setSelectedSpeed(speed: Speed) {
        selectedSpeed = speed
        loadAttemptData(attempts)
    }

    override fun setVSpinUsed(spin: VSpin) {
        this.selectedSpin = spin
        loadAttemptData(attempts)
    }

    override fun setSelectedShotResult(shotResult: ShotResult) {
        this.selectedShotResult = shotResult
        loadAttemptData(attempts)
    }

    private inner class DrillDetailsObserver : DefaultObserver<Drill?>() {
        override fun onNext(drill: Drill) {
            Timber.i(drill.toString())
            showDrillDetailsInView(DrillModelDataMapper.transform(drill))
            hideViewLoading()
        }

        override fun onError(e: Throwable) {
            hideViewLoading()
            showErrorMessage(DefaultErrorBundle(e as Exception))
            showViewRetry()
        }
    }

    private inner class DeleteAttemptObserver : DefaultObserver<Void?>() {
        override fun onComplete() {
            deleteAttemptUseCase.dispose()
        }
    }

    private inner class GetAttemptsObserver : DefaultObserver<Collection<Attempt>>() {
        override fun onNext(attempts: Collection<Attempt>) {
            Timber.d("attempts list updated")
            this@DrillDetailsPresenter.attempts = attempts.map { DrillModelDataMapper.transform(it) }
            loadAttemptData(this@DrillDetailsPresenter.attempts)
        }

        override fun onError(e: Throwable) {
            super.onError(e)
        }
    }

    companion object {
        private val TAG = DrillDetailsPresenter::class.java.name
    }
}