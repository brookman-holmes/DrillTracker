package com.brookmanholmes.drilltracker.presentation.view;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.ListFragment;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.presentation.addattempt.PatternAttemptModel;
import com.tech.freak.wizardpager.model.ModelCallbacks;
import com.tech.freak.wizardpager.model.Page;
import com.tech.freak.wizardpager.model.ReviewItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PatternReviewFragment extends ListFragment implements ModelCallbacks {
    private static final String TAG = PatternReviewFragment.class.getSimpleName();
    private Callbacks callbacks;
    private PatternAttemptModel wizardModel;
    private List<ReviewItem> currentReviewItems;

    private ReviewAdapter reviewAdapter;

    public PatternReviewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reviewAdapter = new ReviewAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_review_list, container, false);
        ListView listView = rootView.findViewById(android.R.id.list);
        setListAdapter(reviewAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        return rootView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (!(getParentFragment() instanceof Callbacks)) {
            throw new ClassCastException("Activity must implement fragment's callbacks");
        }

        callbacks = (Callbacks) getParentFragment();

        wizardModel = callbacks.onGetModel();
        wizardModel.registerListener(this);
        onPageTreeChanged();
    }

    @Override
    public void onPageTreeChanged() {
        onPageDataChanged(null);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;

        wizardModel.unregisterListener(this);
    }

    @Override
    public void onPageDataChanged(Page changedPage) {
        ArrayList<ReviewItem> reviewItems = new ArrayList<>();
        for (Page page : wizardModel.getCurrentPageSequence()) {
            page.getReviewItems(reviewItems);
        }
        Collections.sort(reviewItems, (a, b) -> Integer.compare(a.getWeight(), b.getWeight()));
        currentReviewItems = reviewItems;

        if (reviewAdapter != null) {
            reviewAdapter.notifyDataSetInvalidated();
        }
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        callbacks.onEditScreenAfterReview(currentReviewItems.get(position).getPageKey());
    }

    public interface Callbacks {
        PatternAttemptModel onGetModel();

        void onEditScreenAfterReview(String pageKey);
    }

    private class ReviewAdapter extends BaseAdapter {
        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return true;
        }

        @Override
        public Object getItem(int position) {
            return currentReviewItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return currentReviewItems.get(position).hashCode();
        }

        @Override
        public View getView(int position, View view, ViewGroup container) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View rootView = inflater.inflate(R.layout.row_pattern_review_item, container, false);

            ReviewItem reviewItem = currentReviewItems.get(position);
            Log.i(TAG, "getView: " + reviewItem);
            if (reviewItem instanceof PatternInputPage.PatternReviewItem) {
                Log.i(TAG, "getView: reviewItem is instanceof PatterInputPage.PatternReviewItem");
                Log.i(TAG, "getView: " + reviewItem.toString());
                int currentBall = ((PatternInputPage.PatternReviewItem) reviewItem).getBall();
                int nextBall = ((PatternInputPage.PatternReviewItem) reviewItem).getNextBall();
                int shapeRating = ((PatternInputPage.PatternReviewItem) reviewItem).getShapeRating();
                boolean isBallMade = ((PatternInputPage.PatternReviewItem) reviewItem).isBallMade();

                ((ImageView) rootView.findViewById(R.id.ball)).setImageResource(getDrawableRes(currentBall));
                ((ImageView) rootView.findViewById(R.id.nextBall)).setImageResource(getDrawableRes(nextBall));
                setBallStatus(rootView.findViewById(R.id.textView5), isBallMade);
                setTextVisibility(rootView, nextBall != 0 ? View.VISIBLE : View.GONE);
                setShapeStatus(rootView.findViewById(R.id.shapeStatus), shapeRating);
            }

            return rootView;
        }

        @Override
        public int getCount() {
            return currentReviewItems.size();
        }

        private @DrawableRes
        int getDrawableRes(int ball) {
            switch (ball) {
                case 1:
                    return R.drawable.ic_one_ball;
                case 2:
                    return R.drawable.ic_two_ball;
                case 3:
                    return R.drawable.ic_three_ball;
                case 4:
                    return R.drawable.ic_four_ball;
                case 5:
                    return R.drawable.ic_five_ball;
                case 6:
                    return R.drawable.ic_six_ball;
                case 7:
                    return R.drawable.ic_seven_ball;
                case 8:
                    return R.drawable.ic_eight_ball;
                case 9:
                    return R.drawable.ic_nine_ball;
                case 10:
                    return R.drawable.ic_ten_ball;
                case 11:
                    return R.drawable.ic_eleven_ball;
                case 12:
                    return R.drawable.ic_twelve_ball;
                case 13:
                    return R.drawable.ic_thirteen_ball;
                case 14:
                    return R.drawable.ic_fourteen_ball;
                case 15:
                    return R.drawable.ic_fifteen_ball;
                default:
                    return R.drawable.ic_one_ball;
            }
        }

        private String shapeDescription(int shapeRating) {
            switch (shapeRating) {
                case 0:
                    return "Poor";
                case 1:
                    return "Fair";
                case 2:
                    return "Good";
                case 3:
                    return "Excellent";
                default:
                    return "Error shape rating does not exist";
            }
        }

        private void setTextVisibility(View view, int visibility) {
            view.findViewById(R.id.nextBall).setVisibility(visibility);
            view.findViewById(R.id.text14).setVisibility(visibility);
            view.findViewById(R.id.textView9).setVisibility(visibility);
            view.findViewById(R.id.shapeStatus).setVisibility(visibility);
        }

        private void setBallStatus(TextView textView, boolean isBallMade) {
            if (isBallMade) {
                textView.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.text_ball_made));
                textView.setText("Made");
            } else {
                textView.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.text_ball_missed));
                textView.setText("Missed");
            }
        }

        private void setShapeStatus(TextView textView, int shapeRating) {
            textView.setText(shapeDescription(shapeRating));
            textView.setTextColor(getColor(shapeRating));
        }

        private @ColorInt
        int getColor(int shapeRating) {
            switch (shapeRating) {
                case 0:
                    return ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.shape_rating_zero);
                case 1:
                    return ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.shape_rating_one);
                case 2:
                    return ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.shape_rating_two);
                case 3:
                    return ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.shape_rating_three);
                default:
                    return ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorAnalogousPurple);
            }
        }
    }
}
