package com.brookmanholmes.drilltracker.presentation.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.brookmanholmes.drilltracker.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PatternSpinnerAdapter extends ArrayAdapter<List<Integer>> {

    public PatternSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<List<Integer>> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, parent);
    }

    public List<List<Integer>> getAllItems() {
        List<List<Integer>> result = new ArrayList<>();

        for (int i = 0; i < getCount(); i++) {
            result.add(getItem(i));
        }

        return result;
    }

    private View createItemView(int position, @NonNull ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.view_dropdown_pattern_layout, parent, false);
        ImageView position1 = view.findViewById(R.id.position1);
        ImageView position2 = view.findViewById(R.id.position2);
        ImageView position3 = view.findViewById(R.id.position3);
        ImageView position4 = view.findViewById(R.id.position4);
        ImageView position5 = view.findViewById(R.id.position5);
        ImageView position6 = view.findViewById(R.id.position6);
        ImageView position7 = view.findViewById(R.id.position7);
        ImageView position8 = view.findViewById(R.id.position8);
        ImageView position9 = view.findViewById(R.id.position9);
        ImageView position10 = view.findViewById(R.id.position10);
        ImageView position11 = view.findViewById(R.id.position11);
        ImageView position12 = view.findViewById(R.id.position12);
        ImageView position13 = view.findViewById(R.id.position13);
        ImageView position14 = view.findViewById(R.id.position14);
        ImageView position15 = view.findViewById(R.id.position15);

        List<Integer> pattern = getItem(position);

        setImageViewDrawable(position1, 0, Objects.requireNonNull(pattern));
        setImageViewDrawable(position2, 1, pattern);
        setImageViewDrawable(position3, 2, pattern);
        setImageViewDrawable(position4, 3, pattern);
        setImageViewDrawable(position5, 4, pattern);
        setImageViewDrawable(position6, 5, pattern);
        setImageViewDrawable(position7, 6, pattern);
        setImageViewDrawable(position8, 7, pattern);
        setImageViewDrawable(position9, 8, pattern);
        setImageViewDrawable(position10, 9, pattern);
        setImageViewDrawable(position11, 10, pattern);
        setImageViewDrawable(position12, 11, pattern);
        setImageViewDrawable(position13, 12, pattern);
        setImageViewDrawable(position14, 13, pattern);
        setImageViewDrawable(position15, 14, pattern);


        return view;
    }

    private void setImageViewDrawable(ImageView imageView, int positionInPattern, List<Integer> pattern) {
        if (positionInPattern < pattern.size()) {
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(getDrawableRes(pattern.get(positionInPattern)));
        } else {
            imageView.setVisibility(View.GONE);
        }
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
}
