package com.brookmanholmes.drilltracker.domain.interactor

import com.brookmanholmes.drilltracker.domain.Drill
import com.brookmanholmes.drilltracker.domain.Type
import com.brookmanholmes.drilltracker.domain.repository.DrillRepository
import com.brookmanholmes.drilltracker.presentation.mapper.DrillModelDataMapper
import io.reactivex.Observable

/**
 * Created by Brookman Holmes on 7/14/2017.
 * This class is an implementation of [UseCase] that represents a use case for adding a new
 * [Drill] to the repository
 */
class AddDrill(private val drillRepository: DrillRepository) : UseCase<Drill, DrillParams>() {
    private val TEMP_URL = "temp_url"
    override fun buildUseCaseObservable(params: DrillParams): Observable<Drill> {
        val drill = Drill(
            params.id,
            params.name,
            params.description,
            if (params.imageUrl == null) TEMP_URL else params.imageUrl!!,
            Type.values()[params.type],
            params.maxScore,
            params.defaultTargetScore,
            params.obPositions,
            params.cbPositions,
            params.targetPositions,
            params.purchased,
            DrillModelDataMapper.encode(params.dataToCollect)
        )
        return if (params.imageUrl == null) {
            drillRepository.addDrill(drill, params.image)
        } else {
            drillRepository.addDrill(drill)
        }
    }
}