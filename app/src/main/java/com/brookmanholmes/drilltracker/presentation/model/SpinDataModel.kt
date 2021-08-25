package com.brookmanholmes.drilltracker.presentation.model

class SpinDataModel(attempts: Collection<AttemptModel>) {
    val spinResultModel: SpinResultModel = SpinResultModel(attempts.map { it.spinResult })

    val successRate: Float = if (this.spinResultModel.attempts == 0)
        0f
    else
        spinResultModel.correct.toFloat().div(this.spinResultModel.attempts.toFloat())
}