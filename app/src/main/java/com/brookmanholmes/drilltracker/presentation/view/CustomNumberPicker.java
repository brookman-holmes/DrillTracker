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

public class CustomNumberPicker extends LinearLayout implements View.OnClickListener {
    private int min = 0, max = 15;
    private int value = 7;
    private String stringTitle;

    private OnValueChangeListener onValueChangedListener = null;

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
        title.setText(stringTitle);
        setTextValues();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_number_picker, this);
    }

    private final float enabled = 1f;
    private final float disabled = .5f;

    private void increment() {
        TransitionManager.beginDelayedTransition(this);
        if (value < max) {
            value++;
            notifyChange(value - 1, value);
            setTextValues();

            if (value == max) {
                textNextValue.setVisibility(View.INVISIBLE);
                plus.setEnabled(false);
                plus.setAlpha(disabled);
            }

            if (value > min) {
                textPrevValue.setVisibility(View.VISIBLE);
                minus.setEnabled(true);
                minus.setAlpha(enabled);
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
                minus.setAlpha(disabled);
            }

            if (value < max) {
                textNextValue.setVisibility(View.VISIBLE);
                plus.setEnabled(true);
                plus.setAlpha(enabled);
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

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch(id) {
            case R.id.minus:
                decrement();
                break;
            case R.id.plus:
                increment();
                break;
        }
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

    public void setValue(int value) {
        notifyChange(this.value, value);
        this.value = value;
        setTextValues();
    }

    public interface OnValueChangeListener {
        void onValueChange(int oldVal, int newVal);
    }
}
