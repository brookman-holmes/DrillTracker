package com.brookmanholmes.drilltracker.presentation.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.presentation.addattempt.PatternAttemptModel;
import com.brookmanholmes.drilltracker.presentation.view.util.BallImageUtil;
import com.tech.freak.wizardpager.model.ModelCallbacks;
import com.tech.freak.wizardpager.model.Page;
import com.tech.freak.wizardpager.model.ReviewItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PatternReviewFragmentV2 extends Fragment implements ModelCallbacks {
    @BindView(R.id.scrollView)
    RecyclerView recyclerView;
    private Callbacks callbacks;
    private PatternAttemptModel model;
    private ReviewAdapter adapter = new ReviewAdapter();
    private Unbinder unbinder;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (!(getParentFragment() instanceof Callbacks)) {
            throw new ClassCastException("Activity must implement fragment's callbacks");
        }

        callbacks = (Callbacks) getParentFragment();
        adapter.setCallbacks(callbacks);
        model = callbacks.onGetModel();
        model.registerListener(this);
        onPageTreeChanged();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), layoutManager.getOrientation()));
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
        adapter.setCallbacks(null);
        model.unregisterListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recyclerView.setAdapter(null);
        unbinder.unbind();
    }

    @Override
    public void onPageDataChanged(Page changedPage) {
        ArrayList<ReviewItem> reviewItems = new ArrayList<>();
        for (Page page : model.getCurrentPageSequence()) {
            page.getReviewItems(reviewItems);
        }
        Collections.sort(reviewItems, (a, b) -> Integer.compare(a.getWeight(), b.getWeight()));

        adapter.update(reviewItems);
    }

    @Override
    public void onPageTreeChanged() {
        onPageDataChanged(null);
    }

    public interface Callbacks {
        PatternAttemptModel onGetModel();

        void onEditScreenAfterReview(String pageKey);
    }

    private static class ReviewAdapter extends RecyclerView.Adapter<ReviewItemViewHolder> {
        private List<PatternInputPage.PatternReviewItem> data = new ArrayList<>();
        private Callbacks callbacks;

        ReviewAdapter() {
        }

        @NonNull
        @Override
        public ReviewItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ReviewItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_pattern_review_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ReviewItemViewHolder holder, int position) {
            holder.bind(data.get(position));
            holder.itemView.setOnClickListener(v -> {
                if (callbacks != null)
                    callbacks.onEditScreenAfterReview(data.get(position).getPageKey());
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        void setCallbacks(Callbacks callbacks) {
            this.callbacks = callbacks;
        }

        void update(List<ReviewItem> newData) {
            data.clear();
            for (ReviewItem reviewItem : newData) {
                if (reviewItem instanceof PatternInputPage.PatternReviewItem) {
                    data.add((PatternInputPage.PatternReviewItem) reviewItem);
                }
            }

            notifyDataSetChanged();
        }
    }

    static class ReviewItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ball)
        ImageView ball;
        @BindView(R.id.nextBall)
        ImageView nextBall;
        @BindView(R.id.textView5)
        TextView ballMadeStatus;
        @BindView(R.id.shapeStatus)
        TextView shapeStatus;
        @BindView(R.id.text14)
        TextView fillerText1;
        @BindView(R.id.textView9)
        TextView fillerText2;

        ReviewItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(PatternInputPage.PatternReviewItem reviewItem) {
            setBall(reviewItem.getBall());
            setBallMadeStatus(reviewItem.isBallMade());
            setNextBall(reviewItem.getNextBall());
            setShapeStatus(reviewItem.getShapeRating());
        }

        private void setBallMadeStatus(boolean isBallMade) {
            if (isBallMade) {
                ballMadeStatus.setText("Made");
                ballMadeStatus.setTextColor(ContextCompat.getColor(Objects.requireNonNull(itemView.getContext()), R.color.text_ball_made));
            } else {
                ballMadeStatus.setTextColor(ContextCompat.getColor(Objects.requireNonNull(itemView.getContext()), R.color.text_ball_missed));
                ballMadeStatus.setText("Missed");
            }
        }

        private void setBall(int ballNumber) {
            ball.setImageResource(BallImageUtil.getBallResIdFromBallNumber(ballNumber));
        }

        private void setNextBall(int ballNumber) {
            if (ballNumber == 0) {
                nextBall.setVisibility(View.GONE);
                fillerText1.setVisibility(View.GONE);
                fillerText2.setVisibility(View.GONE);
                shapeStatus.setVisibility(View.GONE);
            } else {
                nextBall.setImageResource(BallImageUtil.getBallResIdFromBallNumber(ballNumber));
                nextBall.setVisibility(View.VISIBLE);
                fillerText1.setVisibility(View.VISIBLE);
                fillerText2.setVisibility(View.VISIBLE);
                shapeStatus.setVisibility(View.VISIBLE);
            }
        }

        private void setShapeStatus(int shapeRating) {
            shapeStatus.setText(shapeDescription(shapeRating));
            shapeStatus.setTextColor(ContextCompat.getColor(this.itemView.getContext(), getColor(shapeRating)));
        }

        private String shapeDescription(int shapeRating) {
            switch (shapeRating) {
                case 1:
                    return "Poor";
                case 2:
                    return "Fair";
                case 3:
                    return "Good";
                case 4:
                    return "Excellent";
                default:
                    return "Error shape rating does not exist";
            }
        }

        private @ColorRes
        int getColor(int shapeRating) {
            switch (shapeRating) {
                case 1:
                    return R.color.shape_rating_zero;
                case 2:
                    return R.color.shape_rating_one;
                case 3:
                    return R.color.shape_rating_two;
                case 4:
                    return R.color.shape_rating_three;
                default:
                    return R.color.colorAnalogousPurple;
            }
        }
    }
}
