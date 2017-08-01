package com.brookmanholmes.drilltracker.presentation.drilldetail;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.presentation.view.util.ImageHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Brookman Holmes on 7/28/2017.
 */

public class FullScreenImageDialog extends DialogFragment {
    private static final String PARAM_URL = "param_url";

    @BindView(R.id.image)
    ImageView imageView;

    Unbinder unbinder;

    static FullScreenImageDialog newInstance(String url) {
        FullScreenImageDialog fragment = new FullScreenImageDialog();
        Bundle args = new Bundle();
        args.putString(PARAM_URL, url);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.AlertDialogTheme_Fullscreen);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fullscreen_drill, container, false);
        unbinder = ButterKnife.bind(this, view);
        ImageHandler.loadImage(imageView, getUrl());
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return view;
    }

    private String getUrl() {
        return getArguments().getString(PARAM_URL);
    }

    @OnClick(R.id.image)
    void onImageClicked() {
        dismiss();
    }

}
