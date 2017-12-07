package com.brookmanholmes.drilltracker.presentation.signin;

import com.brookmanholmes.drilltracker.presentation.base.Presenter;

/**
 * Created by brookman on 10/25/17.
 */

class SignInPresenter implements Presenter {
    private SignInView view = null;

    public void attachView(SignInView view) {
        this.view = view;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        this.view = null;
    }
}
