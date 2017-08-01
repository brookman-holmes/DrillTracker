package com.brookmanholmes.drilltracker.domain.exception;

/**
 * Created by Brookman Holmes on 7/7/2017.
 */

public class DefaultErrorBundle implements ErrorBundle {
    private static final String DEFAULT_ERROR_MESSAGE = "Unknown Error";
    private final Exception exception;

    public DefaultErrorBundle(Exception exception) {
        this.exception = exception;
    }

    @Override
    public Exception getException() {
        return exception;
    }

    @Override
    public String getErrorMessage() {
        return (exception != null) ? this.exception.getMessage() : DEFAULT_ERROR_MESSAGE;
    }
}
