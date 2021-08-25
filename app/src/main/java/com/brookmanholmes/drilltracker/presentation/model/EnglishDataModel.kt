package com.brookmanholmes.drilltracker.presentation.model

class EnglishDataModel (attempts: Collection<AttemptModel>){
    val spinResultModel: SpinResultModel = SpinResultModel(attempts.map { it.englishResult })

    val successRate: Float = if (this.spinResultModel.attempts == 0)
        0f
    else
        spinResultModel.correct.toFloat().div(this.spinResultModel.attempts.toFloat())
}