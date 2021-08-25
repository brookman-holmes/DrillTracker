package com.brookmanholmes.drilltracker.domain.interactor

import com.brookmanholmes.drilltracker.domain.executor.PostExecutionThread
import com.brookmanholmes.drilltracker.domain.executor.ThreadExecutor
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.schedulers.Schedulers

/**
 * Abstract class for a Use Case (Interactor in terms of Clean Architecture).
 * This interface represents a execution unit for different use cases (this means any use case
 * in the application should implement this contract).
 *
 * By convention each UseCase implementation will return the result using a [DisposableCompletableObserver]
 * that will execute its job in a background thread and will post the result in the UI thread.
 *
 * This use case is to be used when we expect no value to be emitted but rather for an action to be completed.
 *
 * @see Completable
 */
abstract class CompletableUseCase<in Params>(
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) {
    private val disposables = CompositeDisposable()
    private val threadExecutorScheduler = Schedulers.from(threadExecutor)
    private val postExecutionThreadScheduler = postExecutionThread.scheduler

    /**
     * Builds a [Completable] which will be used when executing the current [CompletableUseCase].
     */
    abstract fun buildUseCaseCompletable(params: Params? = null): Completable

    /**
     * Executes the current use case.
     *
     * @param observer [DisposableCompletableObserver] which will be listening to the observer build
     * by [buildUseCaseCompletable] method.
     * @param params Parameters (Optional) used to build/execute this use case.
     */
    fun execute(observer: DisposableCompletableObserver, params: Params? = null) {
        val completable = buildUseCaseCompletableWithSchedulers(params)
        addDisposable(completable.subscribeWith(observer))
    }

    /**
     * Builds a [Completable] which will be used when executing the current [CompletableUseCase].
     * With provided Schedulers
     */
    private fun buildUseCaseCompletableWithSchedulers(params: Params?): Completable {
        return buildUseCaseCompletable(params)
            .subscribeOn(threadExecutorScheduler)
            .observeOn(postExecutionThreadScheduler)
    }

    open fun dispose() {
        if (!disposables.isDisposed) {
            disposables.dispose()
        }
    }

    protected fun addDisposable(disposable: Disposable) {
        disposables.add(checkNotNull(disposable, { "disposable cannot be null!" }))
    }
}