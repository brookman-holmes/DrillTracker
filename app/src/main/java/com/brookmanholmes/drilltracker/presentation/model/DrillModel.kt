package com.brookmanholmes.drilltracker.presentation.model

import java.io.Serializable
import java.util.*

/**
 * Created by Brookman Holmes on 7/7/2017.
 */
data class DrillModel(val id: String,
                      val name: String,
                      val description: String,
                      val imageUrl: String,
                      val maxScore: Int,
                      val defaultTargetScore: Int,
                      val obPositions: Int,
                      val cbPositions: Int,
                      val targetPositions: Int,
                      val drillType: Type,
                      val purchased: Boolean,
                      val dataToCollect: EnumSet<DataCollectionModel>) : Model, Serializable {

    override fun getModelId(): String {
        return id;
    }

    object Dates {
        @JvmField
        val ALL_TIME = Date(0)
        @JvmField
        val TODAY = getDateInPast(Calendar.HOUR_OF_DAY, 0)
        @JvmField
        val LAST_WEEK = getDateInPast(Calendar.WEEK_OF_YEAR, -1)
        @JvmField
        val LAST_MONTH = getDateInPast(Calendar.MONTH, -1)
        @JvmField
        val LAST_THREE_MONTHS = getDateInPast(Calendar.MONTH, -3)
        @JvmField
        val LAST_SIX_MONTHS = getDateInPast(Calendar.MONTH, -6)
        private fun getDateInPast(field: Int, value: Int): Date {
            val cal = Calendar.getInstance(TimeZone.getDefault())
            cal.time = Date()
            cal.add(field, value)
            cal[Calendar.HOUR_OF_DAY] = 0
            cal[Calendar.MINUTE] = 0
            cal[Calendar.SECOND] = 0
            cal[Calendar.MILLISECOND] = 0
            return cal.time
        }
    }

    companion object {
        fun getSessionAttempts(attempts: Collection<AttemptModel>): Collection<AttemptModel> {
            val cal = Calendar.getInstance(TimeZone.getDefault())
            if (cal[Calendar.HOUR_OF_DAY] < 5) cal.add(
                Calendar.HOUR,
                -24
            ) else cal[Calendar.HOUR_OF_DAY] = 0
            val sessionTime = cal.time
            val sessionAttempts: MutableCollection<AttemptModel> = ArrayList()
            for (model in attempts) if (model.date.after(sessionTime)) sessionAttempts.add(model)
            return sessionAttempts
        }
    }
}