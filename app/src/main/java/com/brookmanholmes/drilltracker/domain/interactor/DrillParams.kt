package com.brookmanholmes.drilltracker.domain.interactor

import com.brookmanholmes.drilltracker.presentation.model.DataCollectionModel
import java.util.*

data class DrillParams (
    val name: String,
    val id: String?,
    val description: String,
    val maxScore: Int,
    val dataToCollect: EnumSet<DataCollectionModel>,
    var defaultTargetScore: Int,
    val obPositions: Int,
    val cbPositions: Int,
    val targetPositions: Int,
    val type: Int,
    val image: ByteArray?,
    val purchased: Boolean,
    var imageUrl: String?
)