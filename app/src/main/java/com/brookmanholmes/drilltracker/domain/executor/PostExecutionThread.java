package com.brookmanholmes.drilltracker.domain.executor;

import io.reactivex.Scheduler;

/**
 * Created by Brookman Holmes on 7/7/2017.
 */

public interface PostExecutionThread {
    Scheduler getScheduler();
}
