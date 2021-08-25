package com.brookmanholmes.drilltracker.presentation.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.presentation.view.util.BallImageUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import timber.log.Timber;

public class PatternLayoutView extends ConstraintLayout {
    @BindViews({R.id.position1, R.id.position2, R.id.position3, R.id.position4, R.id.position5,
            R.id.position6, R.id.position7, R.id.position8, R.id.position9, R.id.position10,
            R.id.position11, R.id.position12, R.id.position13, R.id.position14, R.id.position15})
    List<ImageView> imageViews;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.remove_pattern)
    ImageButton removePattern;

    public PatternLayoutView(Context context) {
        super(context);
        LayoutInflater.from(getContext()).inflate(R.layout.view_pattern_layout, this);
        ButterKnife.bind(this);
    }

    public PatternLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(getContext()).inflate(R.layout.view_pattern_layout, this);
        ButterKnife.bind(this);
    }

    public PatternLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(getContext()).inflate(R.layout.view_pattern_layout, this);
        ButterKnife.bind(this);
    }

    public PatternLayoutView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        LayoutInflater.from(getContext()).inflate(R.layout.view_pattern_layout, this);
        ButterKnife.bind(this);
    }


    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        removePattern.setOnClickListener(onClickListener);
    }

    public void loadPattern(List<Integer> pattern) {
        Timber.i("loadPattern");
        for (int i = 0; i < 15; i++) {
            ImageView imageView = imageViews.get(i);
            if (pattern.size() > i) {
                Timber.i("Setting image resource for: %s", pattern.get(i));
                imageView.setImageResource(BallImageUtil.getBallResIdFromBallNumber(pattern.get(i)));
                imageView.setVisibility(VISIBLE);
            } else {
                imageView.setVisibility(INVISIBLE);
            }
        }
    }
}
