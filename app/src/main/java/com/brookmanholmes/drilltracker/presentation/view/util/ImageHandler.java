package com.brookmanholmes.drilltracker.presentation.view.util;

import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.brookmanholmes.drilltracker.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by Brookman Holmes on 7/16/2017.
 * Wrapper around a picasso singleton that has a convenience method to use a default method for loading
 * a drill into an ImageView
 */

public class ImageHandler {
    private static final String TAG = ImageHandler.class.getName();

    private ImageHandler() {
    }

    public static void loadImage(ImageView imageView, String url) {
        Log.i(TAG, "loadImage: " + imageView.getHeight());
        Log.i(TAG, "loadImage: " + imageView.getWidth());
        Picasso.with(imageView.getContext())
                .load(url)
                .fit()
                .placeholder(R.drawable.pool_table)
                .error(R.drawable.pool_table_error)
                .into(imageView);
    }

    public static void loadImage(ImageView imageView, String url, Callback callback) {
        Picasso.with(imageView.getContext())
                .load(url)
                .fit()
                .placeholder(R.drawable.pool_table)
                .error(R.drawable.pool_table_error)
                .into(imageView, callback);
    }

    public static void loadImage(ImageView imageView, File file) {
        Picasso.with(imageView.getContext())
                .load(file)
                .fit()
                .placeholder(R.drawable.pool_table)
                .error(R.drawable.pool_table_error)
                .into(imageView);
    }

    public static void loadImage(ImageView imageView, Uri uri) {
        Picasso.with(imageView.getContext())
                .load(uri)
                .fit()
                .placeholder(R.drawable.pool_table)
                .error(R.drawable.pool_table_error)
                .into(imageView);
    }
}
