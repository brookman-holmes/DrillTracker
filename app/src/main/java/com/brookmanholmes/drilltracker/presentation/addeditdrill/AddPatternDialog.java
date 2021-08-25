package com.brookmanholmes.drilltracker.presentation.addeditdrill;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.presentation.base.BaseDialogFragment;
import com.brookmanholmes.drilltracker.presentation.view.util.BallImageUtil;

import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import timber.log.Timber;

/**
 ** TODO: move this into the add/edit drill fragment
 */
public class AddPatternDialog extends BaseDialogFragment<AddPatternDialogPresenter> implements AddPatternView {
    private static final String PARAM_DRILL_ID = "param_drill_id";

    private Unbinder unbinder;

    @BindViews({R.id.pattern_ball_one, R.id.pattern_ball_two, R.id.pattern_ball_three, R.id.pattern_ball_four,
            R.id.pattern_ball_five, R.id.pattern_ball_six, R.id.pattern_ball_seven, R.id.pattern_ball_eight,
            R.id.pattern_ball_nine, R.id.pattern_ball_ten, R.id.pattern_ball_eleven, R.id.pattern_ball_twelve,
            R.id.pattern_ball_thirteen, R.id.pattern_ball_fourteen, R.id.pattern_ball_fifteen})
    List<ImageView> patternImages;

    public static AddPatternDialog newInstance(String drillId) {
        AddPatternDialog dialog = new AddPatternDialog();
        Bundle args = new Bundle();
        args.putString(PARAM_DRILL_ID, drillId);
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.pause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    protected boolean hasTitle() {
        return true;
    }

    @Override
    protected void setDialogBuilderView(AlertDialog.Builder dialogBuilder) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_pattern, null, false);
        unbinder = ButterKnife.bind(this, view);
        dialogBuilder.setView(view);
        presenter.attach(this);
    }

    @Override
    protected String getTitle() {
        return "Create New Pattern";
    }

    @Override
    protected AddPatternDialogPresenter getPresenter() {
        return new AddPatternDialogPresenter(getDrillId());
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        presenter.addNewPattern();
    }

    @OnClick({R.id.one_ball, R.id.two_ball, R.id.three_ball, R.id.four_ball,
            R.id.five_ball, R.id.six_ball, R.id.seven_ball, R.id.eight_ball,
            R.id.nine_ball, R.id.ten_ball, R.id.eleven_ball, R.id.twelve_ball,
            R.id.thirteen_ball, R.id.fourteen_ball, R.id.fifteen_ball})
    void onBallPressed(ImageView ballImage) {
        int ball;
        switch (ballImage.getId()) {
            case R.id.one_ball:
                ball = 1;
                break;
            case R.id.two_ball:
                ball = 2;
                break;
            case R.id.three_ball:
                ball = 3;
                break;
            case R.id.four_ball:
                ball = 4;
                break;
            case R.id.five_ball:
                ball = 5;
                break;
            case R.id.six_ball:
                ball = 6;
                break;
            case R.id.seven_ball:
                ball = 7;
                break;
            case R.id.eight_ball:
                ball = 8;
                break;
            case R.id.nine_ball:
                ball = 9;
                break;
            case R.id.ten_ball:
                ball = 10;
                break;
            case R.id.eleven_ball:
                ball = 11;
                break;
            case R.id.twelve_ball:
                ball = 12;
                break;
            case R.id.thirteen_ball:
                ball = 13;
                break;
            case R.id.fourteen_ball:
                ball = 14;
                break;
            case R.id.fifteen_ball:
                ball = 15;
                break;
            default:
                throw new IllegalArgumentException("This should be all of the options...");
        }
        onBallPressed(ball);
    }

    @OnClick({R.id.pattern_ball_one, R.id.pattern_ball_two, R.id.pattern_ball_three, R.id.pattern_ball_four,
            R.id.pattern_ball_five, R.id.pattern_ball_six, R.id.pattern_ball_seven, R.id.pattern_ball_eight,
            R.id.pattern_ball_nine, R.id.pattern_ball_ten, R.id.pattern_ball_eleven, R.id.pattern_ball_twelve,
            R.id.pattern_ball_thirteen, R.id.pattern_ball_fourteen, R.id.pattern_ball_fifteen})
    void onBallInPatternClicked(ImageView ballImage) {
        int positionInPattern;
        switch (ballImage.getId()) {
            case R.id.pattern_ball_one:
                positionInPattern = 0;
                break;
            case R.id.pattern_ball_two:
                positionInPattern = 1;
                break;
            case R.id.pattern_ball_three:
                positionInPattern = 2;
                break;
            case R.id.pattern_ball_four:
                positionInPattern = 3;
                break;
            case R.id.pattern_ball_five:
                positionInPattern = 4;
                break;
            case R.id.pattern_ball_six:
                positionInPattern = 5;
                break;
            case R.id.pattern_ball_seven:
                positionInPattern = 6;
                break;
            case R.id.pattern_ball_eight:
                positionInPattern = 7;
                break;
            case R.id.pattern_ball_nine:
                positionInPattern = 8;
                break;
            case R.id.pattern_ball_ten:
                positionInPattern = 9;
                break;
            case R.id.pattern_ball_eleven:
                positionInPattern = 10;
                break;
            case R.id.pattern_ball_twelve:
                positionInPattern = 11;
                break;
            case R.id.pattern_ball_thirteen:
                positionInPattern = 12;
                break;
            case R.id.pattern_ball_fourteen:
                positionInPattern = 13;
                break;
            case R.id.pattern_ball_fifteen:
                positionInPattern = 14;
                break;
            default:
                throw new IllegalStateException("Invalid image selected: " + ballImage.getId());
        }
        Timber.i("ballImage Pressed: %s", ballImage);
        Timber.i("positionInPattern, %d", positionInPattern);
        presenter.ballInPatternClicked(positionInPattern);
    }

    @Override
    public void onBallPressed(int ballPressed) {
        presenter.onBallPressed(ballPressed);
    }

    @Override
    public void updatePattern(List<Integer> pattern) {
        for (int i = 0; i < patternImages.size(); i++) {
            if (i < pattern.size()) {
                int ballNumber = pattern.get(i);
                setImage(patternImages.get(i), ballNumber);
                patternImages.get(i).setClickable(true);
            } else {
                patternImages.get(i).setImageDrawable(null);
                patternImages.get(i).setClickable(false);
            }
        }
    }

    private void setImage(ImageView imageView, int ball) {
        imageView.setImageResource(BallImageUtil.getBallResIdFromBallNumber(ball));
    }

    private String getDrillId() {
        return requireArguments().getString(PARAM_DRILL_ID);
    }
}
