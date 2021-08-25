package com.brookmanholmes.drilltracker.presentation.mapper

import com.brookmanholmes.drilltracker.domain.Attempt
import com.brookmanholmes.drilltracker.domain.Drill
import com.brookmanholmes.drilltracker.domain.Type
import com.brookmanholmes.drilltracker.presentation.model.*
import timber.log.Timber
import java.util.*

/**
 * Created by Brookman Holmes on 7/7/2017.
 */
class DrillModelDataMapper {
    companion object {
        @JvmStatic
        fun transform(model: AttemptModel): Attempt {
            Timber.d("transforming to Attempt (here is the incoming AttemptModel): %s", model)
            return Attempt(
                model.distanceResult.ordinal,
                model.speedRequired.ordinal,
                model.spinRequired.ordinal,
                model.englishResult.ordinal,
                model.spinResult.ordinal,
                model.speedResult.ordinal,
                model.thicknessResult.ordinal,
                model.englishRequired.ordinal,
                model.shotResult.ordinal,
                model.targetPosition,
                model.score,
                model.target,
                model.date,
                model.obPosition,
                model.cbPosition,
                model.pattern
            )
        }

        @JvmStatic
        fun transform(model: DrillModel): Drill {
            return Drill(
                model.id,
                model.name,
                model.description,
                model.imageUrl,
                Type.values()[model.drillType.ordinal],
                model.maxScore,
                model.defaultTargetScore,
                model.obPositions,
                model.cbPositions,
                model.targetPositions,
                model.purchased,
                encode(model.dataToCollect),
            )
        }

        @JvmStatic
        fun transform(drill: Drill): DrillModel {
            return DrillModel(
                drill.id!!,
                drill.name,
                drill.description,
                drill.imageUrl,
                drill.maxScore,
                drill.defaultTargetScore,
                drill.obPositions,
                drill.cbPositions,
                drill.targetPositions,
                com.brookmanholmes.drilltracker.presentation.model.Type.values()[drill.type.ordinal],
                drill.isPurchased,
                decode(drill.dataToCollect)
            )
        }

        @JvmStatic
        fun encode(set: EnumSet<DataCollectionModel>): Int {
            var result = 0
            for (`val` in set) {
                result = result or (1 shl `val`.ordinal)
            }
            return result
        }

        @JvmStatic
        fun decode(code: Int): EnumSet<DataCollectionModel> {
            var newCode = code
            val result = EnumSet.noneOf(
                DataCollectionModel::class.java
            )
            while (newCode != 0) {
                val ordinal = Integer.numberOfTrailingZeros(newCode)
                newCode = newCode xor Integer.lowestOneBit(newCode)
                result.add(DataCollectionModel.values()[ordinal])
            }
            return result
        }

        @JvmStatic
        public fun transform(attempt: Attempt): AttemptModel {

            val attemptModel = AttemptModel(
                Speed.values()[attempt.speedRequired],
                English.values()[attempt.englishRequired],
                VSpin.values()[attempt.spinRequired],
                DistanceResult.values()[attempt.distanceResult],
                SpinResult.values()[attempt.spinResult],
                SpinResult.values()[attempt.englishResult],
                SpinResult.values()[attempt.speedResult],
                SpinResult.values()[attempt.thicknessResult],
                ShotResult.values()[attempt.shotResult],
                attempt.targetPosition,
                attempt.score,
                attempt.target,
                attempt.date,
                attempt.obPosition,
                attempt.cbPosition,
                attempt.pattern
            )
            Timber.d("Transforming attempt to attemptModel, here is the outgoing AttemptModel: %s", attemptModel)
            return attemptModel
        }

        @JvmStatic
        private fun transformAttemptModels(attemptModels: Collection<AttemptModel>?): List<Attempt> {
            val result: List<Attempt>
            if (attemptModels != null && !attemptModels.isEmpty()) {
                result = ArrayList()
                for (attemptModel in attemptModels) {
                    result.add(transform(attemptModel))
                }
            } else {
                result = emptyList()
            }
            return result
        }

        @JvmStatic
        private fun transformAttempts(attemptCollection: Collection<Attempt>?): List<AttemptModel> {
            val attemptModels: List<AttemptModel>
            if (attemptCollection != null && !attemptCollection.isEmpty()) {
                attemptModels = ArrayList()
                for (attempt in attemptCollection) {
                    attemptModels.add(transform(attempt))
                }
            } else {
                attemptModels = emptyList()
            }
            return attemptModels
        }

        @JvmStatic
        fun transform(drillCollection: List<Drill>?): List<DrillModel> {
            val drillModelCollection: List<DrillModel>
            if (drillCollection != null && !drillCollection.isEmpty()) {
                drillModelCollection = ArrayList()
                for (drill in drillCollection) {
                    drillModelCollection.add(transform(drill))
                }
            } else {
                drillModelCollection = emptyList()
            }
            return drillModelCollection
        }
    }
}