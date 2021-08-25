package com.brookmanholmes.drilltracker.domain

/**
 * A class that represents a Drill in the domain layer
 */
data class Drill (
    val id: String?,
    val name: String,
    val description: String,
    val imageUrl: String,
    val type: Type,
    val maxScore: Int,
    val defaultTargetScore: Int,
    val obPositions: Int,
    val cbPositions: Int,
    val targetPositions: Int,
    val isPurchased: Boolean,
    val dataToCollect: Int
)