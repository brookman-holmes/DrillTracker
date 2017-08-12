package com.brookmanholmes.drilltracker.presentation.view.util;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import lecho.lib.hellocharts.formatter.SimpleAxisValueFormatter;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

import static android.support.v4.content.ContextCompat.getColor;


/**
 * Created by Brookman Holmes on 7/10/2017.
 */

public class ChartUtil {
    private static final String TAG = ChartUtil.class.getName();

    public static void setupChart(LineChartView chart, DrillModel model) {
        List<PointValue> points = getPointValues(getScoreArray(new ArrayList<>(model.attemptModels)));

        Line line = getLine(points, getColor(chart.getContext(), R.color.chart_blue));

        List<PointValue> dummyPoints = getPointValues(0, model.maxScore + 1);
        List<PointValue> targetPoints = model.attemptModels.size() > 1 ? getPointValues(getTargetArray(new ArrayList<>(model.attemptModels))) : getPointValues(model.defaultTargetScore, model.defaultTargetScore);

        LineChartData data = getData(getLine(targetPoints, getColor(chart.getContext(), R.color.chart_red)).setHasPoints(false), line, getLine(dummyPoints, Color.TRANSPARENT));

        setChartStyle(chart, data, model.maxScore, line.getValues().size());
    }

    public static void setupLifetimeChart(LineChartView chart, DrillModel model, boolean hasLabels) {
        List<PointValue> points = model.attemptModels.size() > 0 ? getPointValuesByBucket(new ArrayList<>(model.attemptModels)) : new ArrayList<PointValue>();
        Line line = getLine(points, getColor(chart.getContext(), R.color.chart_blue));

        List<PointValue> dummyPoints = getPointValues(0, model.maxScore + 1);
        List<PointValue> targetPoints = getPointValues(model.defaultTargetScore, model.defaultTargetScore, model.defaultTargetScore, model.defaultTargetScore, model.defaultTargetScore, model.defaultTargetScore, model.defaultTargetScore, model.defaultTargetScore);

        LineChartData data = getData(getLine(targetPoints, getColor(chart.getContext(), R.color.chart_red)).setHasPoints(false), line, getLine(dummyPoints, Color.TRANSPARENT));

        if (hasLabels) {
            List<AxisValue> axisValues = new ArrayList<>();
            axisValues.add(new AxisValue(0));
            List<String> labels = Arrays.asList("All time", "6 months", "3 months", "1 month", "Last week", "Today");
            for (int i = 0; i < labels.size(); i++) {
                AxisValue axisValue = new AxisValue(i + 1);
                axisValue.setLabel(labels.get(i));
                axisValues.add(axisValue);
            }
            axisValues.add(new AxisValue(7));
            Axis xAxis = new Axis(axisValues);
            xAxis.setHasTiltedLabels(true)
                    .setHasLines(true)
                    .setTextColor(ContextCompat.getColor(chart.getContext(), R.color.primary_text))
                    .setInside(true);

            Axis yAxis = Axis.generateAxisFromRange(0f, model.maxScore + 1, 1)
                    .setHasSeparationLine(true)
                    .setFormatter(new SimpleAxisValueFormatter(0))
                    .setHasLines(true);

            data.setAxisYRight(yAxis);
            data.setAxisXTop(xAxis);
            chart.setLineChartData(data);
            Viewport viewport = new Viewport(chart.getMaximumViewport());
            viewport.left = 0;
            viewport.right = 7;
            viewport.bottom = 0;
            viewport.top = model.maxScore + 1;
            chart.setCurrentViewport(viewport);
        } else {
            setChartStyle(chart, data, model.maxScore, 5);
        }
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

    private static List<PointValue> getPointValuesByBucket(Collection<DrillModel.AttemptModel> attempts) {
        // setup buckets
        Date allTime = new Date(0);
        Date today = getDateInPast(Calendar.HOUR_OF_DAY, 0);
        Date lastWeek = getDateInPast(Calendar.WEEK_OF_YEAR, -1);
        Date lastMonth = getDateInPast(Calendar.MONTH, -1);
        Date lastThreeMonths = getDateInPast(Calendar.MONTH, -3);
        Date lastSixMonths = getDateInPast(Calendar.MONTH, -6);

        Map<Date, Wrapper> attemptMap = new HashMap<>();
        for (DrillModel.AttemptModel attempt : attempts) {

            // put all attempts in all time bucket
            putAttemptInMap(attemptMap, allTime, attempt);

            if (attempt.date.after(today))
                putAttemptInMap(attemptMap, today, attempt);
            else if (attempt.date.before(today) && attempt.date.after(lastWeek))
                putAttemptInMap(attemptMap, lastWeek, attempt);
            else if (attempt.date.before(lastWeek) && attempt.date.after(lastMonth))
                putAttemptInMap(attemptMap, lastMonth, attempt);
            else if (attempt.date.before(lastMonth) && attempt.date.after(lastThreeMonths))
                putAttemptInMap(attemptMap, lastThreeMonths, attempt);
            else if (attempt.date.before(lastThreeMonths) && attempt.date.after(lastSixMonths))
                putAttemptInMap(attemptMap, lastSixMonths, attempt);
        }

        List<Date> dates = new ArrayList<>(attemptMap.keySet());
        Collections.sort(dates);

        List<PointValue> pointValues = new ArrayList<>();

        for (int i = 0; i < dates.size(); i++) {
            Wrapper wrapper = attemptMap.get(dates.get(i));
            float average = wrapper.sum / wrapper.count;
            pointValues.add(new PointValue(getIndex(dates.get(i).getTime()) + 1, average));
        }

        return pointValues;
    }

    private static void putAttemptInMap(Map<Date, Wrapper> map, Date bucket, DrillModel.AttemptModel model) {
        if (map.get(bucket) == null) {
            map.put(bucket, new Wrapper(model));
        } else {
            map.get(bucket).count += 1;
            map.get(bucket).sum += model.score;
        }
    }

    private static int getIndex(long time) {
        Date allTime = new Date(0);
        Date today = getDateInPast(Calendar.HOUR_OF_DAY, 0);
        Date lastWeek = getDateInPast(Calendar.WEEK_OF_YEAR, -1);
        Date lastMonth = getDateInPast(Calendar.MONTH, -1);
        Date lastThreeMonths = getDateInPast(Calendar.MONTH, -3);
        Date lastSixMonths = getDateInPast(Calendar.MONTH, -6);

        if (time == allTime.getTime()) {
            return 0;
        } else if (time == lastSixMonths.getTime()) {
            return 1;
        } else if (time == lastThreeMonths.getTime()) {
            return 2;
        } else if (time == lastMonth.getTime()) {
            return 3;
        } else if (time == lastWeek.getTime()) {
            return 4;
        } else if (time == today.getTime()) {
            return 5;
        } else {
            throw new IllegalArgumentException(String.format(Locale.getDefault(),
                    "Time must be either: %1$d, %2$d, %3$d, %4$d, %5$d, %6$d",
                    allTime.getTime(), lastSixMonths.getTime(),
                    lastThreeMonths.getTime(),
                    lastMonth.getTime(),
                    lastWeek.getTime(),
                    today.getTime()));
        }
    }

    private static Date getDateInPast(int field, int value) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.setTime(new Date());
        cal.add(field, value);
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
