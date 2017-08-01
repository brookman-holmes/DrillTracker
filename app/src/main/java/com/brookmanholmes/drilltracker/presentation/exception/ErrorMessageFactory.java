package com.brookmanholmes.drilltracker.presentation.exception;

import android.content.Context;

/**
 * Created by Brookman Holmes on 7/7/2017.
 */

public class ErrorMessageFactory {
    private ErrorMessageFactory() {

    }

    public static String create(Context context, Exception exception) {
        return exception.getLocalizedMessage();
    }
}
