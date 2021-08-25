package com.brookmanholmes.drilltracker.data.entity.mapper

import com.brookmanholmes.drilltracker.data.entity.AttemptEntity
import com.brookmanholmes.drilltracker.data.entity.DrillEntity
import com.brookmanholmes.drilltracker.domain.Attempt
import com.brookmanholmes.drilltracker.domain.Drill
import com.brookmanholmes.drilltracker.domain.Type
import com.google.common.collect.ImmutableCollection
import com.google.common.collect.ImmutableList
import org.apache.commons.lang3.StringUtils
import java.util.*

/**
 * Mapper class used to transform [DrillEntity] (in the data layer) to [Drill] in the
 * domain layer.
 */
class DrillEntityDataMapper {
    /**
     * Transform a [AttemptEntity] into an [Attempt].
     *
     * @param attempt Object to be transformed.
     */
    fun transform(attempt: Attempt): AttemptEntity {
        return AttemptEntity(
            attempt.distanceResult,
            attempt.speedRequired,
            attempt.spinRequired,
            attempt.englishRequired,
            attempt.spinResult,
            attempt.speedResult,
            attempt.englishResult,
            attempt.thicknessResult,
            attempt.shotResult,
            attempt.targetPosition,
            attempt.score,
            attempt.target,
            attempt.obPosition,
            attempt.cbPosition,
            attempt.date.time,
            ""
        )
    }

    fun transform(patterns: ImmutableCollection<ImmutableList<Int>>): Map<String, String> {
        val result: MutableMap<String, String> = HashMap()
        for ((count, pattern) in patterns.withIndex()) {
            var string = ""
            for (i in pattern.indices) {
                string += pattern[i]
                if (i + 1 != pattern.size) string += ","
            }
            result["pattern$count"] = string
        }
        return result
    }

    /**
     * Transform a [DrillEntity] into an [Drill].
     *
     * @param entity Object to be transformed.
     */
    fun transform(entity: DrillEntity): Drill {
        return Drill(
            entity.id?:"",
            entity.name?:"",
            entity.description?:"",
            entity.imageUrl?:"place_holder",
            transform(entity.type),
            entity.maxScore,
            entity.targetScore,
            entity.obPositions,
            entity.cbPositions,
            entity.targetPositions,
            entity.purchased,
            entity.dataToCollect)
    }

    fun transform(drill: Drill) : DrillEntity {
        return DrillEntity(
            drill.id,
            drill.name,
            drill.imageUrl,
            drill.isPurchased,
            drill.description,
            drill.maxScore,
            drill.targetPositions,
            drill.cbPositions,
            drill.obPositions,
            drill.targetPositions,
            drill.type.name,
            drill.dataToCollect)
    }

    fun transform(attemptEntity: AttemptEntity): Attempt {
        return Attempt(
            attemptEntity.distanceResult,
            attemptEntity.speedRequired,
            attemptEntity.spinRequired,
            attemptEntity.englishResult,
            attemptEntity.spinResult,
            attemptEntity.speedResult,
            attemptEntity.thicknessResult,
            attemptEntity.englishRequired,
            attemptEntity.shotResult,
            attemptEntity.targetPosition,
            attemptEntity.score,
            attemptEntity.target,
            Date(attemptEntity.date),
            attemptEntity.obPosition,
            attemptEntity.cbPosition,
            ArrayList()
        )
    }

    private fun transformAttempts(attemptEntities: Collection<AttemptEntity>): List<Attempt> {
        val attempts: MutableList<Attempt> = ArrayList()
        for (attemptEntity in attemptEntities) {
            val attempt = transform(attemptEntity)
            attempts.add(attempt)
        }
        return attempts
    }

    /**
     * Transforms a list of patterns encoded as integers into a list of a list of integers
     * A pattern would be encoded as: 1,2,3,4,5 etc. with , used as a delimiter
     *
     * @param patterns The list of patterns
     * @return A list of a list of integers representing a list of patterns for a run out
     */
    private fun transform(patterns: Map<String, String>): List<List<Int>> {
        val result: MutableList<List<Int>> = ArrayList()
        for (pattern in patterns.values) {
            val list: MutableList<Int> = ArrayList()
            val strings = StringUtils.split(pattern, ",")
            for (string in strings) {
                list.add(Integer.valueOf(string))
            }
            result.add(list)
        }
        return result
    }

    /**
     * Transform a list of [DrillEntity] into an list of [Drill].
     *
     * @param drillEntityList Object to be transformed.
     */
    fun transform(drillEntityList: Collection<DrillEntity>): List<Drill> {
        val drillList: MutableList<Drill> = ArrayList()
        for (entity in drillEntityList) {
            drillList.add(transform(entity))
        }
        return drillList
    }

    /**
     * Transform a [com.brookmanholmes.drilltracker.domain.Type] into a String
     *
     * @param type The type to transform
     */
    fun transform(type: Type?): String {
        return when (type) {
            Type.POSITIONAL -> "POSITIONAL"
            Type.SAFETY -> "SAFETY"
            Type.AIMING -> "AIMING"
            Type.PATTERN -> "PATTERN"
            Type.KICKING -> "KICKING"
            Type.BANKING -> "BANKING"
            Type.SPEED -> "SPEED"
            Type.ANY -> "ANY"
            else -> throw IllegalArgumentException("Drill.Type $type does not exist")
        }
    }

    /**
     * Transform a [Type] into a string
     * @param type The type to transform
     */
    fun transform(type: com.brookmanholmes.drilltracker.presentation.model.Type): String {
        return when (type) {
            com.brookmanholmes.drilltracker.presentation.model.Type.POSITIONAL -> "POSITIONAL"
            com.brookmanholmes.drilltracker.presentation.model.Type.SAFETY -> "SAFETY"
            com.brookmanholmes.drilltracker.presentation.model.Type.AIMING -> "AIMING"
            com.brookmanholmes.drilltracker.presentation.model.Type.PATTERN -> "PATTERN"
            com.brookmanholmes.drilltracker.presentation.model.Type.KICKING -> "KICKING"
            com.brookmanholmes.drilltracker.presentation.model.Type.BANKING -> "BANKING"
            com.brookmanholmes.drilltracker.presentation.model.Type.SPEED -> "SPEED"
            com.brookmanholmes.drilltracker.presentation.model.Type.ANY -> "ANY"
            else -> throw IllegalArgumentException("Drill.Type $type does not exist")
        }
    }

    private fun transform(type: String?): Type {
        return when (type) {
            "POSITIONAL" -> Type.POSITIONAL
            "SAFETY" -> Type.SAFETY
            "AIMING" -> Type.AIMING
            "PATTERN" -> Type.PATTERN
            "KICKING" -> Type.KICKING
            "BANKING" -> Type.BANKING
            "SPEED" -> Type.SPEED
            "ANY" -> Type.ANY
            else -> throw IllegalArgumentException("Drill.Type $type does not exist")
        }
    }
}