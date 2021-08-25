package com.brookmanholmes.drilltracker.presentation.view;

import static com.brookmanholmes.drilltracker.presentation.view.PatternInputPage.SHAPE_KEY;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.presentation.view.util.BallImageUtil;
import com.tech.freak.wizardpager.model.Page;
import com.tech.freak.wizardpager.ui.PageFragmentCallbacks;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class PatternInputFragment extends Fragment {
    private final static String ARG_POSITION_IN_PATTERN = "position_in_pattern";
    private final static String ARG_CURRENT_BALL = "current_ball";
    private final static String ARG_NEXT_BALL = "next_ball";

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.currentBall)
    ImageView currentBallImage;
    @BindView(R.id.nextBall)
    ImageView nextBallImage;


    private PageFragmentCallbacks callbacks;
    private Page page;

    private int currentBall;
    private int nextBall;

    public PatternInputFragment() {
    }

    public static PatternInputFragment create(String key, int positionInPattern, int currentBall, int nextBall) {
        Bundle args = new Bundle();
        args.putString("key", key);
        args.putInt(ARG_POSITION_IN_PATTERN, positionInPattern);
        args.putInt(ARG_CURRENT_BALL, currentBall);
        args.putInt(ARG_NEXT_BALL, nextBall);
        PatternInputFragment fragment = new PatternInputFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (!(getParentFragment() instanceof PageFragmentCallbacks)) {
            throw new ClassCastException("Fragment must implement PageFragmentCallbacks");
        }

        callbacks = (PageFragmentCallbacks) getParentFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        String key = Objects.requireNonNull(args).getString("key");
        page = callbacks.onGetPage(key);
        currentBall = args.getInt(ARG_CURRENT_BALL);
        nextBall = args.getInt(ARG_NEXT_BALL);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pattern_input, container, false);
        ButterKnife.bind(this, view);
        setBallImage(currentBall, currentBallImage);

        if (nextBall == 0) {
            view.findViewById(R.id.radioGroup).setVisibility(View.GONE);
            view.findViewById(R.id.textView8).setVisibility(View.GONE);
            nextBallImage.setVisibility(View.GONE);
        } else {
            setBallImage(nextBall, nextBallImage);
        }
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    @OnCheckedChanged(R.id.switch1)
    void onCheckChanged(boolean checked) {
        boolean isAlreadyChecked = "true".equals(page.getData().getString(Page.SIMPLE_DATA_KEY));

        if (checked) {
            page.getData().putString(Page.SIMPLE_DATA_KEY, "true");
        } else {
            page.getData().putString(Page.SIMPLE_DATA_KEY, "false");
        }
        if (checked != isAlreadyChecked)
            page.notifyDataChanged();
    }

    @OnClick({R.id.radioButtonExcellent, R.id.radioButtonGood, R.id.radioButtonPoor, R.id.radioButtonFair})
    void onRadioButtonClicked(RadioButton radioButton) {
        boolean checked = radioButton.isChecked();
        int selected = 1;
        switch (radioButton.getId()) {
            case R.id.radioButtonExcellent:
                if (checked)
                    selected = 4;
                break;
            case R.id.radioButtonGood:
                if (checked)
                    selected = 3;
                break;
            case R.id.radioButtonFair:
                if (checked)
                    selected = 2;
                break;
            case R.id.radioButtonPoor:
                if (checked)
                    selected = 1;
                break;
        }

        boolean isAlreadySelected = selected == page.getData().getInt(SHAPE_KEY, 0);

        if (!isAlreadySelected) {
            page.getData().putInt(SHAPE_KEY, selected);
            page.notifyDataChanged();
        }
    }

    private void setBallImage(int ball, ImageView image) {
        image.setImageResource(BallImageUtil.getBallResIdFromBallNumber(ball));
    }
}
