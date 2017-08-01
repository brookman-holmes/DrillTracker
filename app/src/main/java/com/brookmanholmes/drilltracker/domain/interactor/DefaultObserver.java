package com.brookmanholmes.drilltracker.domain.interactor;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by Brookman Holmes on 7/7/2017.
 */

public class DefaultObserver<T> extends DisposableObserver<T> {
    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
