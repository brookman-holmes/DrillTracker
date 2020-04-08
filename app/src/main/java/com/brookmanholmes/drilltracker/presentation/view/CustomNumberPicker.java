package com.brookmanholmes.drilltracker.presentation.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.transition.TransitionManager;

import com.brookmanholmes.drilltracker.R;

/**
 * Created by Brookman Holmes on 7/20/2017.
 */

public class CustomNumberPicker extends LinearLayout implements View.OnClickListener, View.OnLongClickListener {
    private static final float ENABLED_ALPHA = 1f;
    private static final float DISABLED_ALPHA = .5f;
    private static final int DEFAULT_STEP = 1;

    private OnValueChangeListener onValueChangedListener = null;

    private int min = 0, max = 15;
    private int stepValue = 1;
    private int value = 7;
    private String stringTitle;

    private TextView textValue, textPrevValue, textNextValue;
    private ImageButton minus, plus;


    public CustomNumberPicker(Context context) {
        super(context);
        init();
    }

    public CustomNumberPicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomNumberPicker, 0, 0);
        stringTitle = a.getString(R.styleable.CustomNumberPicker_cnp_title);
        if (stringTitle == null)
            stringTitle = "Default title";

        min = a.getInt(R.styleable.CustomNumberPicker_cnp_min, 0);
        max = a.getInt(R.styleable.CustomNumberPicker_cnp_max, 15);
        value = a.getInt(R.styleable.CustomNumberPicker_cnp_starting_value, 7);
        stepValue = a.getInt(R.styleable.CustomNumberPicker_cnp_step_value, DEFAULT_STEP);
        a.recycle();
    }

    public CustomNumberPicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CustomNumberPicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        TextView title = findViewById(R.id.title);
        textValue = findViewById(R.id.value);
        textPrevValue = findViewById(R.id.prevValue);
        textNextValue = findViewById(R.id.nextValue);
        minus = findViewById(R.id.minus);
        plus = findViewById(R.id.plus);

        minus.setOnClickListener(this);
        plus.setOnClickListener(this);
        minus.setOnLongClickListener(this);
        plus.setOnLongClickListener(this);
        title.setText(stringTitle);
        setTextValues();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_number_picker, this);
    }

    private void increment() {
        TransitionManager.beginDelayedTransition(this);
        if (value < max) {
            value += stepValue;
            notifyChange(value - stepValue, value);
            setTextValues();
        }
    }


    private void decrement() {
        TransitionManager.beginDelayedTransition(this);
        if (value > min) {
            value -= stepValue;
            notifyChange(value + stepValue, value);
            setTextValues();
        }
    }

    private void setTextValues() {
        textValue.setText(Integer.toString(value));
        if (value > min)
            textPrevValue.setText(Integer.toString(value - stepValue));
        if (value < max)
            textNextValue.setText(Integer.toString(value + stepValue));

        setTextVisibility();
    }

    private void setTextVisibility() {
        if (value == max) {
            textNextValue.setVisibility(View.INVISIBLE);
            plus.setEnabled(false);
            plus.setAlpha(DISABLED_ALPHA);
        }

        if (value > min) {
            textPrevValue.setVisibility(View.VISIBLE);
            minus.setEnabled(true);
            minus.setAlpha(ENABLED_ALPHA);
        }

        if (value == min) {
            textPrevValue.setVisibility(View.INVISIBLE);
            minus.setEnabled(false);
            minus.setAlpha(DISABLED_ALPHA);
        }

        if (value < max) {
            textNextValue.setVisibility(View.VISIBLE);
            plus.setEnabled(true);
            plus.setAlpha(ENABLED_ALPHA);
        }
    }

    public int getValue() {
        return value;
    }

    public void setValue(int newValue) {
        notifyChange(this.value, newValue);
        this.value = newValue;
        setTextValues();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.minus:
                decrement();
                break;
            case R.id.plus:
                increment();
                break;
        }
    }

    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.minus:
                //postLongClick(false);
                break;
            case R.id.plus:
                //postLongClick(true);
                break;
        }
        return true;
    }

    public void setMax(int max) {
        this.max = max;
        if (value > max)
            value = max;
        setTextValues();
    }

    public void setDefaultValue(int value) {
        this.value = value;
        setTextValues();
    }

    public void setMin(int min) {
        this.min = min;
        if (value < min)
            value = min;
        setTextValues();
    }

    public void setStepValue(int stepValue) {
        this.stepValue = stepValue;
        setTextValues();
    }

    public void setOnValueChangedListener(OnValueChangeListener onValueChangedListener) {
        this.onValueChangedListener = onValueChangedListener;
    }

    private void notifyChange(int previous, int current) {
        if (onValueChangedListener != null) {
            onValueChangedListener.onValueChange(this, previous, current);
        }
    }

    interface OnValueChangeListener {
        void onValueChange(CustomNumberPicker picker, int oldVal, int newVal);
    }
}
