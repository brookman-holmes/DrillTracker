package com.brookmanholmes.drilltracker.presentation.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brookmanholmes.drilltracker.R;
import com.travijuu.numberpicker.library.NumberPicker;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by brookman on 1/29/18.
 */

public class SpeedPicker extends LinearLayout {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.picker)
    NumberPicker numberPicker;


    public SpeedPicker(Context context) {
        super(context);
        inflate(context);
    }

    public SpeedPicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SpeedPicker, 0, 0);
        title.setText(a.getString(R.styleable.SpeedPicker_sp_title));
    }

    private void inflate(Context context) {
        LayoutInflater.from(context).inflate(R.layout.speed_picker_view, this);
        ButterKnife.bind(this);
    }

    public int getPickerValue() {
        return numberPicker.getValue();
    }
}
