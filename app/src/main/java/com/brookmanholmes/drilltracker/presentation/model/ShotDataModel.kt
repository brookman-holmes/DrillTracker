package com.brookmanholmes.drilltracker.presentation.model

class ShotDataModel(attemptModels: Collection<AttemptModel>) {
    val attempts: Int = attemptModels.count { it.shotResult != ShotResult.NO_DATA }
    val makes: Int = attemptModels.count {
        it.filterByShotResult(ShotResult.MAKE_CENTER)
                || it.filterByShotResult(ShotResult.MAKE_OVER_CUT)
                || it.filterByShotResult(ShotResult.MAKE_UNDER_CUT)
    }
    val missThin: Int = attemptModels.count { it.filterByShotResult(ShotResult.MISS_OVER_CUT) }
    val missThick: Int = attemptModels.count { it.filterByShotResult(ShotResult.MISS_UNDER_CUT) }
    val successRate: Float = getSuccessRate(makes, attempts)

    private fun getSuccessRate(makes: Int, attempts: Int): Float {
        return if (attempts == 0)
            0f
        else
            makes.toFloat() / attempts.toFloat()
    }
}

