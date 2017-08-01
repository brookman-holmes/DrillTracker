package com.brookmanholmes.drilltracker.domain.exception;

/**
 * Created by Brookman Holmes on 7/7/2017.
 */

public interface ErrorBundle {
    Exception getException();
    String getErrorMessage();
}
