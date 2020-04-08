package com.brookmanholmes.drilltracker.presentation.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.ArrayRes;

import com.brookmanholmes.drilltracker.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brookman on 1/3/18.
 */

public class SpinnerAdapterHelper {
    private SpinnerAdapterHelper() {
    }

    public static ArrayAdapter<CharSequence> createAdapterFromResource(
            Context context,
            @ArrayRes int arrayRes
    ) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, arrayRes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_centered);
        return adapter;
    }

    public static ArrayAdapter<CharSequence> createNumberedListAdapter(
            Context context,
            int from,
            int to
    ) {
        List<CharSequence> list = new ArrayList<>();
        for (int i = from; i < to; i++) {
            list.add(Integer.toString(i));
        }
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, list);

        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_centered);
        return adapter;
    }

    public static ArrayAdapter<CharSequence> createNumberedListAdapter(
            Context context,
            int from,
            int to,
            String prepend
    ) {
        List<CharSequence> list = new ArrayList<>();
        for (int i = from; i < to; i++) {
            if (i == 0) {
                list.add(prepend + "Any");
            } else {
                list.add(prepend + i);
            }
        }
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, list);

        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    public static List<String> createMaterialSpinnerItems(int from, int to) {
        List<String> result = new ArrayList<>();

        result.add("Any");

        for (int i = from + 1; i < to; i++) {
            result.add(String.valueOf(i));
        }

        return result;
    }
}
