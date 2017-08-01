package com.brookmanholmes.drilltracker.presentation.view.util;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.util.Log;

import static android.support.v4.content.ContextCompat.getColor;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.formatter.SimpleAxisValueFormatter;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;


/**
 * Created by Brookman Holmes on 7/10/2017.
 */

public class ChartUtil {
    public static void setupChart(LineChartView chart, DrillModel model, boolean condenseDataByDate) {
        List<PointValue> points;
        if (condenseDataByDate) {
            points = ChartUtil.getPointValues(getScoreArrayByDate(model.attemptModels));
        } else {
            points = ChartUtil.getPointValues(getScoreArray(new ArrayList<>(model.attemptModels)));
        }

        List<PointValue> targetPoints = ChartUtil.getPointValues(getTargetArray(new ArrayList<>(model.attemptModels)));
        List<PointValue> dummyPoints = ChartUtil.getPointValues(0, model.maxScore + 1);

        Line line = ChartUtil.getLine(points, getColor(chart.getContext(), R.color.chart_blue));
        LineChartData data = ChartUtil.getData(ChartUtil.getLine(targetPoints, getColor(chart.getContext(), R.color.chart_red)).setHasPoints(false), line, ChartUtil.getLine(dummyPoints, Color.TRANSPARENT));
        ChartUtil.setChartStyle(chart, data, model.maxScore, points.size());
    }

    private static void setChartStyle(LineChartView chart, LineChartData data, int max, int points) {
        Axis xAxis = Axis.generateAxisFromRange(0f, points, 1)
                .setHasSeparationLine(true)
                .setHasLines(true)
                .setTextSize(0);
        Axis yAxis = Axis.generateAxisFromRange(0f, max + 1, 1)
                .setHasSeparationLine(true)
                .setFormatter(new SimpleAxisValueFormatter(0))
                .setHasLines(true);

        data.setAxisYRight(yAxis);
        data.setAxisXBottom(xAxis);
        chart.setLineChartData(data);
        Viewport viewport = new Viewport(chart.getMaximumViewport());
        viewport.left = 0;
        viewport.right = points;
        viewport.bottom = 0;
        viewport.top = max + 1;
        chart.setCurrentViewport(viewport);
    }

    private static LineChartData getData(Line... lines) {
        return new LineChartData(Arrays.asList(lines));
    }

    private static Line getLine(List<PointValue>values, @ColorInt int color) {
        return new Line(values)
                .setPointRadius(4)
                .setColor(color)
                .setPointColor(color)
                .setHasLabels(false)
                .setStrokeWidth(2);
    }

    private static List<PointValue> getPointValues(int... points) {
        List<PointValue> result = new ArrayList<>();

        for (int i = 0; i < points.length; i++) {
            result.add(new PointValue(i, points[i]));
        }

        return result;
    }

    private static List<PointValue> getPointValues(float... points) {
        List<PointValue> result = new ArrayList<>();

        for (int i = 0; i < points.length; i++) {
            result.add(new PointValue(i, points[i]));
        }

        return result;
    }

    private static int[] getScoreArray(List<DrillModel.AttemptModel> attempts) {
        Collections.sort(attempts);
        int[] result = new int[attempts.size()];

        int count = 0;
        for (DrillModel.AttemptModel attempt : attempts) {
            result[count] = attempt.score;
            count++;
        }

        return result;
    }

    private static int[] getTargetArray(List<DrillModel.AttemptModel> attempts) {
        Collections.sort(attempts);
        int[] result = new int[attempts.size()];

        int count = 0;
        for (DrillModel.AttemptModel attempt : attempts) {
            result[count] = attempt.target;
            count++;
        }

        return result;
    }

    private static float[] getScoreArrayByDate(Collection<DrillModel.AttemptModel> attempts) {
        Map<Date, Wrapper> attemptMap = new HashMap<>();

        for (DrillModel.AttemptModel attemptModel : attempts) {
            DrillModel.AttemptModel attempt = DrillModel.AttemptModel.copy(attemptModel);
            attempt.date = setDateToBeginningOfDay(attempt.date);
            if (attemptMap.containsKey(attempt.date)) {
                attemptMap.get(attempt.date).sum += attempt.score;
                attemptMap.get(attempt.date).count += 1;
            } else
                attemptMap.put(attempt.date, new Wrapper(attempt));
        }

        List<Date> dates = new ArrayList<>(attemptMap.keySet());
        Collections.sort(dates);

        float[] result = new float[dates.size()];
        int count = 0;
        for (Date date : dates) {
            Wrapper wrapper = attemptMap.get(date);
            float average = wrapper.sum / wrapper.count;
            result[count] = average;

            count += 1;
        }

        return result;
    }

    private static Date setDateToBeginningOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private static final class Wrapper {
        float count = 0;
        float sum = 0;

        private Wrapper(DrillModel.AttemptModel attemptModel) {
            this.sum = attemptModel.score;
            this.count = 1;
        }
    }
}
