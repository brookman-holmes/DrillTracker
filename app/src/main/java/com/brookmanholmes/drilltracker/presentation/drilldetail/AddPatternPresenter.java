package com.brookmanholmes.drilltracker.presentation.drilldetail;

public class AddPatternPresenter implements AddPatternContract {
    private AddPatternView view;

    public AddPatternPresenter(AddPatternView view) {
        this.view = view;
    }

    @Override
    public void onNewPatternClicked(String drillId) {
        view.showNewPatternDialog(drillId);
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        view = null;
    }
}
