package com.brookmanholmes.drilltracker.presentation.view;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;

import com.brookmanholmes.drilltracker.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by brookman on 12/11/17.
 */

public class PatternPlayInputView extends RecyclerView {
    PatternAdapter adapter;

    public PatternPlayInputView(Context context) {
        super(context);
        init();
    }

    public PatternPlayInputView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PatternPlayInputView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setPattern(1, 3, 5, 7, 2, 4, 6, 8);
    }

    public List<PatternEntry> getPattern() {
        return adapter.patternEntries;
    }

    public void setPattern(int... balls) {
        adapter = new PatternAdapter(balls);
        setAdapter(adapter);
        setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private interface OnEntryChanged {
        void setBallMadeStatus(int ball, boolean isBallMade);

        void setPositionRating(int ball, int rating);
    }

    private static class PatternAdapter extends RecyclerView.Adapter<PatternRowViewHolder> implements PatternPlayInputView.OnEntryChanged {
        private static String TAG = PatternAdapter.class.getName();
        List<PatternEntry> patternEntries = new ArrayList<>();

        private PatternAdapter(int... balls) {
            for (int ball : balls) {
                patternEntries.add(new PatternEntry(ball));
            }
        }

        @Override
        public PatternRowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_pattern_input, parent, false);
            return new PatternRowViewHolder(view, this);
        }

        @Override
        public void onBindViewHolder(PatternRowViewHolder holder, int position) {
            holder.bind(patternEntries.get(position), position + 1 == patternEntries.size());
        }

        @Override
        public int getItemCount() {
            return patternEntries.size();
        }

        @Override
        public void setBallMadeStatus(int ball, boolean isBallMade) {
            Log.i(TAG, "setBallMadeStatus: " + ball + " " + isBallMade);
            getPatternEntry(ball).made = isBallMade;
        }

        @Override
        public void setPositionRating(int ball, int rating) {
            Log.i(TAG, "setPositionRating: " + ball + " " + rating);
            getPatternEntry(ball).rating = rating;
        }

        private PatternEntry getPatternEntry(int ball) {
            for (PatternEntry entry : patternEntries) {
                if (entry.ball == ball)
                    return entry;
            }

            throw new IllegalArgumentException("ball " + ball + " is not in this pattern");
        }
    }

    static class PatternRowViewHolder extends ViewHolder {
        OnEntryChanged onEntryChanged;
        ArrayAdapter<CharSequence> adapter;
        boolean isLastBall = false;
        @BindView(R.id.imageView)
        ImageView image;
        @BindView(R.id.switch2)
        Switch aSwitch;
        @BindView(R.id.spinner)
        Spinner spinner;

        PatternRowViewHolder(View itemView, final OnEntryChanged onEntryChanged) {
            super(itemView);
            ButterKnife.bind(this, this.itemView);
            this.onEntryChanged = onEntryChanged;

            adapter = ArrayAdapter.createFromResource(itemView.getContext(), R.array.position_ratings, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }

        private void bind(final PatternEntry entry, boolean isLastBall) {
            setImage(entry.ball);
            setMade(entry.made);
            setRating(entry.rating);
            this.isLastBall = isLastBall;

            aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    onEntryChanged.setBallMadeStatus(entry.ball, b);
                    setSpinnerVisibility(b);
                }
            });

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    onEntryChanged.setPositionRating(entry.ball, adapterView.getSelectedItemPosition() + 1);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }

        private void setImage(int ball) {
            @DrawableRes int id;
            switch (ball) {
                case 1:
                    id = R.drawable.ic_one_ball;
                    break;
                case 2:
                    id = R.drawable.ic_two_ball;
                    break;
                case 3:
                    id = R.drawable.ic_three_ball;
                    break;
                case 4:
                    id = R.drawable.ic_four_ball;
                    break;
                case 5:
                    id = R.drawable.ic_five_ball;
                    break;
                case 6:
                    id = R.drawable.ic_six_ball;
                    break;
                case 7:
                    id = R.drawable.ic_seven_ball;
                    break;
                case 8:
                    id = R.drawable.ic_eight_ball;
                    break;
                case 9:
                    id = R.drawable.ic_nine_ball;
                    break;
                case 10:
                    id = R.drawable.ic_ten_ball;
                    break;
                case 11:
                    id = R.drawable.ic_eleven_ball;
                    break;
                case 12:
                    id = R.drawable.ic_twelve_ball;
                    break;
                case 13:
                    id = R.drawable.ic_thirteen_ball;
                    break;
                case 14:
                    id = R.drawable.ic_fourteen_ball;
                    break;
                case 15:
                    id = R.drawable.ic_fifteen_ball;
                    break;
                default:
                    throw new IllegalStateException("Ball must be between 1 and 15, was: " + ball);
            }
            image.setImageResource(id);
        }

        private void setMade(boolean isBallMade) {
            aSwitch.setChecked(isBallMade);
            setSpinnerVisibility(isBallMade);
        }

        private void setSpinnerVisibility(boolean isBallMade) {
            if (isBallMade && !isLastBall) {
                spinner.setVisibility(View.VISIBLE);
            } else {
                spinner.setVisibility(View.INVISIBLE);
            }
        }

        private void setRating(int rating) {
            spinner.setSelection(rating - 1);
        }
    }

    private static class PatternEntry {
        private int ball = 0;
        private boolean made = false;
        private int rating = 0;

        PatternEntry(int ball) {
            this.ball = ball;
        }

        PatternEntry(int ball, boolean made, int rating) {
            this.ball = ball;
            this.made = made;
            this.rating = rating;
        }

        @Override
        public String toString() {
            return "PatternEntry{" +
                    "ball=" + ball +
                    ", made=" + made +
                    ", rating=" + rating +
                    '}';
        }
    }

}
