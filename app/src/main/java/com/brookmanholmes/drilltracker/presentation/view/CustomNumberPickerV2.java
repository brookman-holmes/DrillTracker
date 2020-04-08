package com.brookmanholmes.drilltracker.presentation.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.brookmanholmes.drilltracker.R;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by brookman on 1/29/18.
 */

public class CustomNumberPickerV2 extends LinearLayout {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.picker)
    NumberPicker numberPicker;


    public CustomNumberPickerV2(Context context) {
        super(context);
        inflate(context);
    }

    public CustomNumberPickerV2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomNumberPickerV2, 0, 0);
        title.setText(a.getString(R.styleable.CustomNumberPickerV2_cnp2_title));
        numberPicker.setMin(a.getInt(R.styleable.CustomNumberPickerV2_cnp2_min, 0));
        numberPicker.setMax(a.getInt(R.styleable.CustomNumberPickerV2_cnp2_max, 15));
        numberPicker.setValue(a.getInt(R.styleable.CustomNumberPickerV2_cnp2_starting_value, 7));
        numberPicker.setUnit(a.getInt(R.styleable.CustomNumberPickerV2_cnp2_step_value, 1));
        a.recycle();
    }

    private void inflate(Context context) {
        LayoutInflater.from(context).inflate(R.layout.speed_picker_view, this);
        ButterKnife.bind(this);
    }

    public int getValue() {
        return numberPicker.getValue();
    }

    public void setValue(int value) {
        numberPicker.setValue(value);
    }

    public void setMaxValue(int value) {
        numberPicker.setMax(value);
        if (numberPicker.getValue() > value) {
            numberPicker.setValue(value);
        }
    }

    public void setMinValue(int value) {
        numberPicker.setMin(value);
    }

    public void setStepValue(int stepValue) {
        numberPicker.setUnit(stepValue);
    }

    public void setValueChangedListener(ValueChangedListener valueChangedListener) {
        numberPicker.setValueChangedListener(valueChangedListener);
    }

}
