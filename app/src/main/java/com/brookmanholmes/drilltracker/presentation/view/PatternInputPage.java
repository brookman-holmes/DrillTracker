package com.brookmanholmes.drilltracker.presentation.view;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.tech.freak.wizardpager.model.BranchPage;
import com.tech.freak.wizardpager.model.ModelCallbacks;
import com.tech.freak.wizardpager.model.Page;
import com.tech.freak.wizardpager.model.ReviewItem;

import java.util.ArrayList;

public class PatternInputPage extends BranchPage {
    public static final String SHAPE_KEY = "SHAPE_KEY";

    private final int positionInPattern, currentBall, nextBall;

    public PatternInputPage(ModelCallbacks callbacks, int positionInPattern, int currentBall, int nextBall) {
        super(callbacks, Integer.toString(positionInPattern));
        this.positionInPattern = positionInPattern;
        this.currentBall = currentBall;
        this.nextBall = nextBall;
        getData().putInt(SHAPE_KEY, 1);
    }

    @Override
    public Fragment createFragment() {
        return PatternInputFragment.create(getKey(), positionInPattern, currentBall, nextBall);
    }

    public int getBallNumber() {
        return currentBall;
    }

    public int getPositionInPattern() {
        return positionInPattern;
    }

    public boolean isBallMade() {
        return "true".equals(getData().getString(Page.SIMPLE_DATA_KEY));
    }

    public boolean isLastBallInPattern() {
        return nextBall == 0;
    }

    public int getShapeRating() {
        return getData().getInt(SHAPE_KEY);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        dest.add(new PatternReviewItem(getKey(), currentBall, nextBall, isBallMade(), getShapeRating()));
    }

    @NonNull
    @Override
    public String toString() {
        return "PatternInputPage{" +
                "positionInPattern=" + positionInPattern +
                ", currentBall=" + currentBall +
                ", nextBall=" + nextBall +
                ", mChoices=" + mChoices +
                '}';
    }

    public static class PatternReviewItem extends ReviewItem {
        private final int ball;
        private final int nextBall;
        private final boolean isBallMade;
        private final int shapeRating;

        PatternReviewItem(String pageKey, int ball, int nextBall, boolean isBallMade, int shapeRating) {
            super("", "", pageKey);
            this.ball = ball;
            this.nextBall = nextBall;
            this.isBallMade = isBallMade;
            this.shapeRating = shapeRating;
        }

        public int getBall() {
            return ball;
        }

        public int getNextBall() {
            return nextBall;
        }

        public boolean isBallMade() {
            return isBallMade;
        }

        public int getShapeRating() {
            return shapeRating;
        }

        @Override
        public String toString() {
            return "PatternReviewItem{" +
                    "ball=" + ball +
                    ", nextBall=" + nextBall +
                    ", isBallMade=" + isBallMade +
                    ", shapeRating=" + shapeRating +
                    '}';
        }
    }
}
