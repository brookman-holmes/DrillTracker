package com.brookmanholmes.drilltracker.presentation.exception;

/**
 * Created by Brookman Holmes on 7/7/2017.
 */

public class ErrorMessageFactory {
    private ErrorMessageFactory() {

    }

    public static String create(Exception exception) {
        return exception.getLocalizedMessage();
    }
}
