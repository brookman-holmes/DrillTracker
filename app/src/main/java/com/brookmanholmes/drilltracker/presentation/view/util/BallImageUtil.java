package com.brookmanholmes.drilltracker.presentation.view.util;

import androidx.annotation.DrawableRes;

import com.brookmanholmes.drilltracker.R;

public class BallImageUtil {

    private BallImageUtil() {
    }

    @DrawableRes
    public static int getBallResIdFromBallNumber(int ballNumber) {
        switch (ballNumber) {
            case 1:
                return R.drawable.ball_one;
            case 2:
                return R.drawable.ball_two;
            case 3:
                return R.drawable.ball_three;
            case 4:
                return R.drawable.ball_four;
            case 5:
                return R.drawable.ball_five;
            case 6:
                return R.drawable.ball_six;
            case 7:
                return R.drawable.ball_seven;
            case 8:
                return R.drawable.ball_eight;
            case 9:
                return R.drawable.ball_nine;
            case 10:
                return R.drawable.ball_ten;
            case 11:
                return R.drawable.ball_eleven;
            case 12:
                return R.drawable.ball_twelve;
            case 13:
                return R.drawable.ball_thirteen;
            case 14:
                return R.drawable.ball_fourteen;
            case 15:
                return R.drawable.ball_fifteen;
            default:
                throw new IllegalArgumentException("Ball number should be between 1 and 15, inclusive, you used: " + ballNumber);
        }
    }
}
