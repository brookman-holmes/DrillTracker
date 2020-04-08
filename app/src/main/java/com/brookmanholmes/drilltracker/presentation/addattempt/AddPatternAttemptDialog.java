package com.brookmanholmes.drilltracker.presentation.addattempt;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.presentation.view.PatternReviewFragmentV2;
import com.brookmanholmes.drilltracker.presentation.view.StepPagerStrip;
import com.tech.freak.wizardpager.model.ModelCallbacks;
import com.tech.freak.wizardpager.model.Page;
import com.tech.freak.wizardpager.ui.PageFragmentCallbacks;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.brookmanholmes.drilltracker.presentation.addattempt.AddAttemptDialog.PARAM_DRILL_ID;
import static com.brookmanholmes.drilltracker.presentation.addattempt.AddAttemptDialog.PARAM_PATTERN;
import static com.brookmanholmes.drilltracker.presentation.addattempt.AddAttemptDialog.PARAM_TARGET_SCORE;

/*
 * Copyright 2012 Roman Nurik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class AddPatternAttemptDialog extends DialogFragment implements
        PageFragmentCallbacks, PatternReviewFragmentV2.Callbacks, ModelCallbacks {
    private static final String TAG = AddPatternAttemptDialog.class.getCanonicalName();
    private final AddPatternPresenter presenter = new AddPatternPresenter();
    @BindView(R.id.pager)
    protected ViewPager pager;
    @BindView(R.id.nextButton)
    protected Button nextButton;
    @BindView(R.id.prevButton)
    protected Button previousButton;
    @BindView(R.id.strip)
    protected StepPagerStrip stepPagerStrip;
    private MyPagerAdapter pagerAdapter;
    private PatternAttemptModel wizardModel;
    private List<Page> currentPageSequence;
    private boolean editingAfterReview;
    private boolean consumePageSelectedEvent;

    static AddPatternAttemptDialog newInstance(String drillId, int targetScore, List<Integer> pattern) {
        AddPatternAttemptDialog dialog = new AddPatternAttemptDialog();
        Bundle args = new Bundle();
        args.putString(PARAM_DRILL_ID, drillId);
        args.putInt(PARAM_TARGET_SCORE, targetScore);
        args.putIntegerArrayList(PARAM_PATTERN, new ArrayList<>(pattern));
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
        if (wizardModel == null) {
            wizardModel = new PatternAttemptModel(Objects.requireNonNull(getArguments()).getIntegerArrayList(PARAM_PATTERN));
        }
        if (savedInstanceState != null)
            wizardModel.load(Objects.requireNonNull(savedInstanceState.getBundle("model")));

        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.AppTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_pattern_input, container, false);
        ButterKnife.bind(this, view);
        wizardModel.registerListener(this);
        pagerAdapter = new MyPagerAdapter(getChildFragmentManager());
        pager.setAdapter(pagerAdapter);

        stepPagerStrip.setOnPageSelectedListener(position -> {
            position = Math.min(pagerAdapter.getCount() - 1, position);
            if (pager.getCurrentItem() != position)
                pager.setCurrentItem(position);
        });

        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                stepPagerStrip.setCurrentPage(position);

                if (consumePageSelectedEvent) {
                    consumePageSelectedEvent = false;
                    return;
                }

                editingAfterReview = false;
                updateBottomBar();
            }
        });

        onPageTreeChanged();
        updateBottomBar();
        return view;
    }


    @OnClick(R.id.nextButton)
    void onNextButtonClicked() {
        if (pager.getCurrentItem() == currentPageSequence.size()) {
            presenter.addAttempt(
                    getDrillId(),
                    getTargetScore(),
                    wizardModel.getPatternResult()
            );
            dismiss();
        } else {
            if (editingAfterReview) {
                pager.setCurrentItem(pagerAdapter.getCount() - 1);
            } else
                pager.setCurrentItem(pager.getCurrentItem() + 1);
        }
    }

    @OnClick(R.id.prevButton)
    void onPreviousButtonClicked() {
        pager.setCurrentItem(pager.getCurrentItem() - 1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        wizardModel.unregisterListener(this);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle("model", wizardModel.save());
    }

    @Override
    public void onPageDataChanged(Page page) {
        if (page.isRequired()) {
            if (recalculateCutOffPage()) {
                pagerAdapter.notifyDataSetChanged();
                updateBottomBar();
            }
        }
    }

    @Override
    public void onPageTreeChanged() {
        currentPageSequence = wizardModel.getCurrentPageSequence();
        recalculateCutOffPage();
        stepPagerStrip.setPageCount(currentPageSequence.size() + 1);
        pagerAdapter.notifyDataSetChanged();
        updateBottomBar();
    }

    @Override
    public Page onGetPage(String key) {
        return wizardModel.findByKey(key);
    }

    @Override
    public PatternAttemptModel onGetModel() {
        return wizardModel;
    }

    @Override
    public void onEditScreenAfterReview(String pageKey) {
        for (int i = currentPageSequence.size() - 1; i >= 0; i--) {
            if (currentPageSequence.get(i).getKey().equals(pageKey)) {
                consumePageSelectedEvent = true;
                editingAfterReview = true;
                pager.setCurrentItem(i);
                updateBottomBar();
            }
        }
    }

    private boolean recalculateCutOffPage() {
        int cutOffPage = currentPageSequence.size() + 1;
        for (int i = 0; i < currentPageSequence.size(); i++) {
            Page page = currentPageSequence.get(i);
            if (page.isRequired()) {
                cutOffPage = 1;
                break;
            }
        }

        if (pagerAdapter.getCutOffPage() != cutOffPage) {
            pagerAdapter.setCutOffPage(cutOffPage);
            return true;
        }

        return false;
    }

    private void updateBottomBar() {
        int position = pager.getCurrentItem();
        if (position == currentPageSequence.size()) {
            nextButton.setText("Add Pattern Attempt");
        } else {
            nextButton.setText(editingAfterReview ? "Review" : "Next");
            nextButton.setEnabled(position != pagerAdapter.getCutOffPage());
        }

        previousButton.setVisibility(position <= 0 ? View.INVISIBLE : View.VISIBLE);
    }

    private String getDrillId() {
        return Objects.requireNonNull(getArguments()).getString(PARAM_DRILL_ID);
    }

    private int getTargetScore() {
        return Objects.requireNonNull(getArguments()).getInt(PARAM_TARGET_SCORE);
    }

    class MyPagerAdapter extends FragmentStatePagerAdapter {
        private int cutOffPage;
        private Fragment primaryItem;

        MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int i) {
            if (i >= currentPageSequence.size())
                return new PatternReviewFragmentV2();

            return currentPageSequence.get(i).createFragment();
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            // TODO: be smarter about this
            if (object == primaryItem) {
                // Re-use the current fragment (its position never changes)
                return POSITION_UNCHANGED;
            }

            return POSITION_NONE;
        }

        @Override
        public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            super.setPrimaryItem(container, position, object);
            primaryItem = (Fragment) object;
        }

        @Override
        public int getCount() {
            if (currentPageSequence == null) {
                return 0;
            }
            return Math.min(cutOffPage + 1, currentPageSequence.size() + 1);
        }

        int getCutOffPage() {
            return cutOffPage;
        }

        void setCutOffPage(int cutOffPage) {
            if (cutOffPage < 0) {
                cutOffPage = Integer.MAX_VALUE;
            }
            this.cutOffPage = cutOffPage;
        }
    }
}
