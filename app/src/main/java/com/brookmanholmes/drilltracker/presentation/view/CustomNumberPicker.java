package com.brookmanholmes.drilltracker.presentation.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brookmanholmes.drilltracker.R;

/**
 * Created by Brookman Holmes on 7/20/2017.
 */

public class CustomNumberPicker extends LinearLayout implements View.OnClickListener, View.OnLongClickListener {
    private static final long LONG_PRESS_UPDATE_INTERVAL = 300;
    private static final float ENABLED_ALPHA = 1f;
    private static final float DISABLED_ALPHA = .5f;

    private OnValueChangeListener onValueChangedListener = null;
    private ChangeCurrentByOneFromLongPressCommand changeCurrentByOneFromLongPressCommand;


    private int min = 0, max = 15;
    private int value = 7;
    private String stringTitle;

    private TextView title;
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
        title = findViewById(R.id.title);
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

    private void changeValue(int newValue) {
        TransitionManager.beginDelayedTransition(this);

        if (value > min && value < max) {
            notifyChange(value, newValue);
            setValue(newValue);

            if (value == max) {
                textNextValue.setVisibility(View.INVISIBLE);
                plus.setEnabled(false);
                plus.setAlpha(DISABLED_ALPHA);
            } else if (value == min) {
                textPrevValue.setVisibility(View.INVISIBLE);
                minus.setEnabled(false);
                minus.setAlpha(DISABLED_ALPHA);
            }
        }
    }

    private void increment() {
        TransitionManager.beginDelayedTransition(this);
        if (value < max) {
            value++;
            notifyChange(value - 1, value);
            setTextValues();

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
        }
    }


    private void decrement() {
        TransitionManager.beginDelayedTransition(this);
        if (value > min) {
            value--;
            notifyChange(value + 1, value);
            setTextValues();
            textPrevValue.setVisibility(View.VISIBLE);

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
    }

    private void setTextValues() {
        textValue.setText(Integer.toString(value));
        if (value > min)
            textPrevValue.setText(Integer.toString(value - 1));
        if (value < max)
        textNextValue.setText(Integer.toString(value + 1));
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

    private void postLongClick(boolean increment) {
        removeAllCallbacks();

        if (changeCurrentByOneFromLongPressCommand == null) {
            changeCurrentByOneFromLongPressCommand = new ChangeCurrentByOneFromLongPressCommand();
        }

        changeCurrentByOneFromLongPressCommand.setIncrement(increment);
        post(changeCurrentByOneFromLongPressCommand);
    }

    private void removeAllCallbacks() {
        if (changeCurrentByOneFromLongPressCommand != null)
            removeCallbacks(changeCurrentByOneFromLongPressCommand);
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

    public void setOnValueChangedListener(OnValueChangeListener onValueChangedListener) {
        this.onValueChangedListener = onValueChangedListener;
    }

    private void notifyChange(int previous, int current) {
        if (onValueChangedListener != null) {
            onValueChangedListener.onValueChange(previous, current);
        }
    }

    public interface OnValueChangeListener {
        void onValueChange(int oldVal, int newVal);
    }

    private class ChangeCurrentByOneFromLongPressCommand implements Runnable {
        private boolean increment;

        private void setIncrement(boolean increment) {
            this.increment = increment;
        }

        @Override
        public void run() {
            if (increment)
                increment();
            else
                decrement();

            postDelayed(this, LONG_PRESS_UPDATE_INTERVAL);
        }
    }
}
