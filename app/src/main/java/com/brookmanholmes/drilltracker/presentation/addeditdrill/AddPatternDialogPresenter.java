package com.brookmanholmes.drilltracker.presentation.addeditdrill;

import androidx.annotation.Nullable;

import com.brookmanholmes.drilltracker.data.repository.datasource.DataStoreFactory;
import com.brookmanholmes.drilltracker.domain.interactor.AddPattern;
import com.brookmanholmes.drilltracker.domain.interactor.DefaultObserver;
import com.brookmanholmes.drilltracker.presentation.base.Presenter;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class AddPatternDialogPresenter implements Presenter {
    @Nullable
    private AddPatternView view;
    private final List<Integer> pattern = new ArrayList<>(15);
    private final AddPattern addPattern;
    private final String drillId;

    AddPatternDialogPresenter(String drillId) {
        this.drillId = drillId;
        addPattern = new AddPattern(DataStoreFactory.getDrillRepo());
    }

    void addNewPattern() {
        Timber.i("addPattern.execute()");
        addPattern.execute(new DefaultObserver<>(), Pair.of(drillId, pattern));
    }

    public void onBallPressed(int ball) {
        addBallToPattern(ball);
        updatePatternList();
    }

    private void updatePatternList() {
        if (view != null) {
            view.updatePattern(pattern);
        }
    }

    private void addBallToPattern(int ball) {
        if (pattern.size() < 15) {
            pattern.add(ball);
        } else {
            Timber.i("addBallToPattern: Too many balls are trying to be added to the pattern");
        }
    }

    public void attach(AddPatternView view) {
        this.view = view;
    }

    public void ballInPatternClicked(int positionInPattern) {
        if (pattern.size() > positionInPattern) {
            pattern.remove(positionInPattern);
        }
        updatePatternList();
    }

    @Override
    public void resume() {
        updatePatternList();
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        Timber.i("addPattern.dispose()");
        addPattern.dispose();
        view = null;
    }
}
