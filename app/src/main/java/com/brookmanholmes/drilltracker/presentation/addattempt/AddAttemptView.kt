package com.brookmanholmes.drilltracker.presentation.addattempt

import com.brookmanholmes.drilltracker.presentation.model.DrillModel

interface AddAttemptView {
    fun loadDrillData(drillModel: DrillModel)
    fun setTargetPositions(positions: Int)
    fun setCbPositions(positions: Int)
    fun setObPositions(positions: Int)
    fun setShowEnglishData(visible: Boolean)
    fun setShowSpeedData(visible: Boolean)
    fun setShowTargetDistanceData(visible: Boolean)
    fun setShowSpinData(visible: Boolean)
    fun setShowShotData(visible: Boolean)
    fun getDrillId():String
}