package com.brookmanholmes.drilltracker.domain.interactor

import com.brookmanholmes.drilltracker.domain.Attempt
import com.brookmanholmes.drilltracker.domain.repository.DrillRepository
import io.reactivex.Observable

class GetAttemptsUseCase(private val drillRepository: DrillRepository) : UseCase<Collection<Attempt>, AttemptFilter>() {
    override fun buildUseCaseObservable(params: AttemptFilter): Observable<Collection<Attempt>> {
        return drillRepository.getAttempts(params.id)
    }
}