package com.brookmanholmes.drilltracker.presentation.addattempt;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.brookmanholmes.drilltracker.presentation.model.PatternEntry;
import com.brookmanholmes.drilltracker.presentation.view.PatternInputPage;
import com.tech.freak.wizardpager.model.ModelCallbacks;
import com.tech.freak.wizardpager.model.Page;
import com.tech.freak.wizardpager.model.PageList;

import java.util.ArrayList;
import java.util.List;

public class PatternAttemptModel implements ModelCallbacks {
    private final List<Integer> pattern;

    private final List<ModelCallbacks> listeners = new ArrayList<>();
    private final PageList rootPageList;

    public PatternAttemptModel(List<Integer> pattern) {
        this.pattern = pattern;
        rootPageList = onNewRootPageList();
    }

    private PageList onNewRootPageList() {
        PageList list = new PageList();
        List<PatternInputPage> pages = new ArrayList<>();
        for (int i = 0; i < pattern.size(); i++) {
            boolean isLastBallInPattern = pattern.size() > i + 1;
            pages.add(
                    new PatternInputPage(
                            this,
                            i,
                            pattern.get(i),
                            isLastBallInPattern ? pattern.get(i + 1) : 0
                    ));
        }

        for (int i = 0; i < pages.size(); i++) {
            PatternInputPage currentPage = pages.get(i);
            if (pages.size() > i + 1) {
                PatternInputPage nextPage = pages.get(i + 1);
                currentPage.addBranch("true", nextPage);
            }
        }

        if (pages.size() > 1)
            list.add(pages.get(0));
        return list;
    }

    public List<PatternEntry> getPatternResult() {
        List<PatternEntry> result = new ArrayList<>();
        for (int i = 0; i < pattern.size(); i++) {
            result.add(new PatternEntry(i, pattern.get(i), false, 0));
        }
        for (int i = 0, k = 0; i < getCurrentPageSequence().size() && k < result.size(); i++, k++) {
            if (k < getCurrentPageSequence().size()) {
                if (getCurrentPageSequence().get(k) instanceof PatternInputPage) {
                    PatternInputPage page = (PatternInputPage) getCurrentPageSequence().get(k);
                    result.get(k).setMade(page.isBallMade());
                    result.get(k).setRating(page.getShapeRating());
                }
            }
        }

        return result;
    }

    @Override
    public void onPageDataChanged(Page page) {
        // can't use for each because of concurrent modification (review fragment
        // can get added or removed and will register itself as a listener)
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onPageDataChanged(page);
        }
    }

    @Override
    public void onPageTreeChanged() {
        // can't use for each because of concurrent modification (review fragment
        // can get added or removed and will register itself as a listener)
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onPageTreeChanged();
        }
    }

    public Page findByKey(String key) {
        return rootPageList.findByKey(key);
    }

    public void load(Bundle savedValues) {
        for (String key : savedValues.keySet()) {
            rootPageList.findByKey(key).resetData(savedValues.getBundle(key));
        }
    }

    public void registerListener(ModelCallbacks listener) {
        listeners.add(listener);
    }

    public Bundle save() {
        Bundle bundle = new Bundle();
        for (Page page : getCurrentPageSequence()) {
            bundle.putBundle(page.getKey(), page.getData());
        }
        return bundle;
    }

    /**
     * Gets the current list of wizard steps, flattening nested (dependent) pages based on the
     * user's choices.
     */
    public List<Page> getCurrentPageSequence() {
        ArrayList<Page> flattened = new ArrayList<>();
        rootPageList.flattenCurrentPageSequence(flattened);
        return flattened;
    }

    public void unregisterListener(ModelCallbacks listener) {
        listeners.remove(listener);
    }

    @NonNull
    @Override
    public String toString() {
        return "PatternAttemptModel{" +
                "selectedPattern=" + pattern +
                '}';
    }
}
