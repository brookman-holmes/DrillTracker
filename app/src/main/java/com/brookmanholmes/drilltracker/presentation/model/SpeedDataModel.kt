package com.brookmanholmes.drilltracker.presentation.model

class SpeedDataModel(attempts: Collection<AttemptModel>) {
    val spinResultModel: SpinResultModel = SpinResultModel(attempts.map { it.speedResult })

    val successRate: Float = if (this.spinResultModel.attempts == 0)
        0f
    else
        spinResultModel.correct.toFloat().div(this.spinResultModel.attempts.toFloat())
}