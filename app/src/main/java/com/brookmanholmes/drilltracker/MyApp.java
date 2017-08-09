package com.brookmanholmes.drilltracker;

import android.app.Application;

import com.squareup.picasso.Picasso;

/**
 * Created by Brookman Holmes on 7/16/2017.
 */

public class MyApp extends Application {
    Picasso picasso;

    @Override
    public void onCreate() {
        super.onCreate();
        picasso = new Picasso.Builder(this).build();
        Picasso.setSingletonInstance(picasso);
    }
}
