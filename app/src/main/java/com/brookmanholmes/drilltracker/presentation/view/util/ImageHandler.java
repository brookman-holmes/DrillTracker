package com.brookmanholmes.drilltracker.presentation.view.util;

import android.content.Context;
import android.graphics.Bitmap;
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

    public static void loadImage(ImageView imageView, String url) {
        Picasso.with(imageView.getContext())
                .load(url)
                .placeholder(R.drawable.table_horizontal)
                .error(R.drawable.table_horizontal)
                .into(imageView);
    }

    public static void loadImage(ImageView imageView, String url, Callback callback) {
        Picasso.with(imageView.getContext())
                .load(url)
                .placeholder(R.drawable.table_horizontal)
                .error(R.drawable.table_horizontal)
                .into(imageView, callback);
    }

    public static void loadImage(ImageView imageView, File file) {
        Picasso.with(imageView.getContext())
                .load(file)
                .placeholder(R.drawable.table_horizontal)
                .error(R.drawable.table_horizontal)
                .into(imageView);
    }

    public static void loadImage(ImageView imageView, Uri uri) {
        Picasso.with(imageView.getContext())
                .load(uri)
                .placeholder(R.drawable.table_horizontal)
                .error(R.drawable.table_horizontal)
                .into(imageView);
    }

    private ImageHandler() {}
}
