package com.brookmanholmes.drilltracker.presentation.model

import java.util.*

data class AttemptModel(
    val speedRequired: Speed,
    val englishRequired: English,
    val spinRequired: VSpin,
    val distanceResult: DistanceResult,
    val spinResult: SpinResult,
    val englishResult: SpinResult,
    val speedResult: SpinResult,
    val thicknessResult: SpinResult,
    val shotResult: ShotResult,
    val targetPosition: Int,
    val score: Int,
    val target: Int,
    val date: Date,
    val obPosition: Int,
    val cbPosition: Int,
    val pattern: List<Int>?
) : Comparable<AttemptModel> {
    override fun compareTo(other: AttemptModel): Int {
        return date.compareTo(other.date)
    }

    fun hasDistanceResult(distanceResult: DistanceResult): Boolean {
        return this.distanceResult == distanceResult
    }

    fun filterBySpinResult(spinResult: SpinResult): Boolean {
        return this.spinResult == spinResult
    }

    fun filterByEnglishResult(spinResult: SpinResult): Boolean {
        return this.englishResult == spinResult
    }

    fun filterBySpeedResult(spinResult: SpinResult): Boolean {
        return spinResult == speedResult
    }

    fun filterByShotResult(shotResult: ShotResult): Boolean {
        return if (shotResult == ShotResult.ANY)
            true
        else
            (this.shotResult == shotResult)
    }

    fun filterByCb(selectedCb: Int): Boolean {
        return if (selectedCb == 0)
            true
        else
            this.cbPosition == selectedCb
    }

    fun filterByOb(selectedOb: Int): Boolean {
        return if (selectedOb == 0)
            true
        else this.obPosition == selectedOb
    }

    fun filterByTarget(selectedTarget: Int): Boolean {
        return if (selectedTarget == 0)
            true
        else this.targetPosition == selectedTarget
    }

    fun filterBySpeedRequired(selectedSpeed: Speed): Boolean {
        return if (selectedSpeed == Speed.ANY)
            true
        else this.speedRequired == selectedSpeed
    }

    fun filterByEnglishRequired(selectedEnglish: English): Boolean {
        return if (selectedEnglish == English.ANY)
            true
        else this.englishRequired == selectedEnglish
    }

    fun filterBySpinRequired(selectedSpin: VSpin): Boolean {
        return if (selectedSpin == VSpin.ANY)
            true
        else spinRequired == selectedSpin
    }

    fun filterByToday(): Boolean {
        val today = Date()
        val diff: Long = today.time - date.time
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        return (days < 1f)
    }

    fun filterByHistory(): Boolean {
        return !filterByToday()
    }
}

fun Collection<AttemptModel>.toTargetDataModel(): TargetDataModel {
    return TargetDataModel(this)
}

fun Collection<AttemptModel>.toShotDataModel(): ShotDataModel {
    return ShotDataModel(this)
}