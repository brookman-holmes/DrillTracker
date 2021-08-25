package com.brookmanholmes.drilltracker.presentation.model

import kotlin.math.pow

class TargetDataModel(attempts: Collection<AttemptModel>) {
    private val onTarget: Int = attempts.count {
        it.hasDistanceResult(DistanceResult.ZERO)
    }
    private val halfDiamondFast: Int = attempts.count {
        it.hasDistanceResult(DistanceResult.ONE_HALF) &&
        it.filterBySpeedResult(SpinResult.TOO_LITTLE)
    }
    private val oneDiamondFast: Int = attempts.count {
        it.hasDistanceResult(DistanceResult.ONE) &&
        it.filterBySpeedResult(SpinResult.TOO_MUCH)
    }
    private val oneAndHalfDiamondFast: Int = attempts.count {
        it.hasDistanceResult(DistanceResult.ONE_AND_HALF) &&
        it.filterBySpeedResult(SpinResult.TOO_MUCH)
    }
    private val overOneAndHalfDiamondsFast:Int = attempts.count {
        it.hasDistanceResult(DistanceResult.OVER_ONE_AND_HALF) &&
        it.filterBySpeedResult(SpinResult.TOO_MUCH)
    }
    private val halfDiamondSlow: Int = attempts.count {
        it.hasDistanceResult(DistanceResult.ONE_HALF) &&
        it.filterBySpeedResult(SpinResult.TOO_LITTLE)
    }
    private val oneDiamondSlow: Int = attempts.count {
        it.hasDistanceResult(DistanceResult.ONE) &&
        it.filterBySpeedResult(SpinResult.TOO_LITTLE)
    }
    private val oneAndHalfDiamondSlow: Int = attempts.count {
        it.hasDistanceResult(DistanceResult.ONE_AND_HALF) &&
        it.filterBySpeedResult(SpinResult.TOO_LITTLE)
    }
    private val overOneAndHalfDiamondsSlow:Int = attempts.count {
        it.hasDistanceResult(DistanceResult.OVER_ONE_AND_HALF) &&
        it.filterBySpeedResult(SpinResult.TOO_LITTLE)
    }
    private val halfDiamondNoSpeed: Int = attempts.count {
        it.hasDistanceResult(DistanceResult.ONE_HALF)
    }
    private val oneDiamondNoSpeed: Int = attempts.count {
        it.hasDistanceResult(DistanceResult.ONE)
    }
    private val oneAndHalfDiamondNoSpeed: Int = attempts.count {
        it.hasDistanceResult(DistanceResult.ONE_AND_HALF)
    }
    private val overOneAndHalfDiamondsNoSpeed:Int = attempts.count {
        it.hasDistanceResult(DistanceResult.OVER_ONE_AND_HALF)
    }

    val averageError: Float = attempts.map {
        when (it.distanceResult) {
            DistanceResult.ZERO -> 0f
            DistanceResult.ONE_HALF -> .5f
            DistanceResult.ONE -> 1f
            DistanceResult.OVER_ONE_AND_HALF -> 2f
            DistanceResult.ONE_AND_HALF -> 1.5f
            DistanceResult.NO_DATA -> 0f
        }
    }.standardDeviation().toFloat()

    fun getDistanceValues(): List<Triple<DistanceResult, SpinResult, Int>> {
        return listOf(
            Triple(DistanceResult.OVER_ONE_AND_HALF, SpinResult.TOO_LITTLE, overOneAndHalfDiamondsSlow),
            Triple(DistanceResult.ONE_AND_HALF, SpinResult.TOO_LITTLE, oneAndHalfDiamondSlow),
            Triple(DistanceResult.ONE, SpinResult.TOO_LITTLE, oneDiamondSlow),
            Triple(DistanceResult.ONE_HALF, SpinResult.TOO_LITTLE, halfDiamondSlow),
            Triple(DistanceResult.ZERO, SpinResult.NO_DATA, onTarget),
            Triple(DistanceResult.ONE_HALF, SpinResult.TOO_LITTLE, halfDiamondFast),
            Triple(DistanceResult.ONE, SpinResult.TOO_LITTLE, oneDiamondFast),
            Triple(DistanceResult.ONE_AND_HALF, SpinResult.TOO_LITTLE, oneAndHalfDiamondFast),
            Triple(DistanceResult.OVER_ONE_AND_HALF, SpinResult.TOO_LITTLE, overOneAndHalfDiamondsFast)
        )
    }

    fun getDistanceValue(distanceResult: DistanceResult): Int {
        return when (distanceResult) {
            DistanceResult.ZERO -> onTarget
            DistanceResult.ONE_HALF -> halfDiamondNoSpeed
            DistanceResult.ONE -> oneDiamondNoSpeed
            DistanceResult.ONE_AND_HALF -> oneAndHalfDiamondNoSpeed
            DistanceResult.OVER_ONE_AND_HALF -> overOneAndHalfDiamondsNoSpeed
            DistanceResult.NO_DATA -> 0
        }
    }
}

fun Collection<Number>.standardDeviation(): Double {
    return if (this.isEmpty())
        0.0
    else {
        val mean = this.map { it.toDouble() }.average()
        this
            .map { it.toDouble() }
            .reduce { acc, it ->
                acc + (mean - it).pow(2)
            }.div(this.size)
            .pow(0.5)
    }
}