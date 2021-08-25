package com.brookmanholmes.drilltracker.presentation.model

class SpinResultModel(spinResults: Collection<SpinResult>) {
    val correct = spinResults.count { it == SpinResult.CORRECT }
    val less = spinResults.count { it == SpinResult.TOO_LITTLE }
    val more = spinResults.count { it == SpinResult.TOO_MUCH }
    val attempts = spinResults.size
    val successRate: Float = if (attempts == 0) 0f else correct.toFloat() / attempts.toFloat()
}