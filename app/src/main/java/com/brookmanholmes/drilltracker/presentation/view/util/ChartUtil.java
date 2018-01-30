package com.brookmanholmes.drilltracker.presentation.view.util;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.util.Log;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.presentation.model.AimDrillModel;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;
import com.brookmanholmes.drilltracker.presentation.model.English;
import com.brookmanholmes.drilltracker.presentation.model.SafetyDrillModel;
import com.brookmanholmes.drilltracker.presentation.model.Speed;
import com.brookmanholmes.drilltracker.presentation.model.SpeedDrillModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import lecho.lib.hellocharts.formatter.SimpleAxisValueFormatter;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.Chart;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PieChartView;

import static android.support.v4.content.ContextCompat.getColor;


/**
 * Created by Brookman Holmes on 7/10/2017.
 */

public class ChartUtil {
    private static final String TAG = ChartUtil.class.getName();

    public static void setupChart(ColumnChartView chart, SpeedDrillModel model) {
        Log.i(TAG, "setupChart: " + model.speeds);
        int maxValue = 0;
        List<Column> columns = new ArrayList<>();
        for (Speed speed : Speed.values()) {
            List<SubcolumnValue> subcolumnValues = new ArrayList<>();
            if (maxValue < model.speeds.get(speed)) {
                maxValue = model.speeds.get(speed);
            }
            subcolumnValues.add(new SubcolumnValue(model.speeds.get(speed), getColor(chart.getContext(), R.color.chart_blue)));
            Column column = new Column(subcolumnValues);
            column.setHasLabels(true);
            columns.add(column);
        }
        ColumnChartData data = new ColumnChartData(columns);
        List<AxisValue> axisValues = new ArrayList<>();
        int count = 0;
        for (Speed speed : Speed.values()) {
            axisValues.add(new AxisValue(count++, new DecimalFormat(".##").format(speed.getDiamondOffset()).toCharArray()));
        }
        Axis axis = createXAxis(chart.getContext(), axisValues);
        axis.setHasTiltedLabels(false)
                .setInside(false)
                .setHasLines(false)
                .setHasSeparationLine(false)
                .setName("Diamonds away from correct speed");
        Axis yAxis = createYAxis(0, maxValue + 4, 1, 0);
        data.setAxisXBottom(axis);
        data.setAxisYLeft(yAxis);

        chart.setColumnChartData(data);
    }

    public static void setupChart(PieChartView speedChart, PieChartView thicknessChart, PieChartView spinChart, SafetyDrillModel model) {
        if (model.getAttempts() == 0) {
            List<SliceValue> blankSlices = new ArrayList<>();
            blankSlices.add(new SliceValue(1));
            speedChart.setPieChartData(new PieChartData(blankSlices));
            spinChart.setPieChartData(new PieChartData(blankSlices));
            thicknessChart.setPieChartData(new PieChartData(blankSlices));
        } else {
            List<SliceValue> speedValues = new ArrayList<>();
            speedValues.add(new SliceValue(model.getSpeedCorrect(), getColor(speedChart.getContext(), R.color.chart_green)));
            speedValues.add(new SliceValue(model.getSpeedHard(), getColor(speedChart.getContext(), R.color.chart_red)));
            speedValues.add(new SliceValue(model.getSpeedSoft(), getColor(speedChart.getContext(), R.color.chart_blue)));
            PieChartData speedData = new PieChartData(speedValues);
            speedData.setHasLabels(true);
            speedChart.setPieChartData(speedData);

            List<SliceValue> thicknessValues = new ArrayList<>();
            thicknessValues.add(new SliceValue(model.getThicknessCorrect(), getColor(speedChart.getContext(), R.color.chart_green)));
            thicknessValues.add(new SliceValue(model.getThicknessThick(), getColor(speedChart.getContext(), R.color.chart_red)));
            thicknessValues.add(new SliceValue(model.getThicknessThin(), getColor(speedChart.getContext(), R.color.chart_blue)));
            PieChartData thicknessData = new PieChartData(thicknessValues);
            thicknessData.setHasLabels(true);
            thicknessChart.setPieChartData(thicknessData);

            List<SliceValue> spinValues = new ArrayList<>();
            spinValues.add(new SliceValue(model.getSpinCorrect(), getColor(speedChart.getContext(), R.color.chart_green)));
            spinValues.add(new SliceValue(model.getSpinMore(), getColor(speedChart.getContext(), R.color.chart_red)));
            spinValues.add(new SliceValue(model.getSpinLess(), getColor(speedChart.getContext(), R.color.chart_blue)));
            PieChartData spinData = new PieChartData(spinValues);
            spinData.setHasLabels(true);
            spinChart.setPieChartData(spinData);
        }
    }

    public static void setupChart(LineChartView chart, DrillModel model) {
        List<PointValue> points = getPointValues(getScoreArray(new ArrayList<>(DrillModel.getSessionAttempts(model.attemptModels))));

        Line line = getLine(points, getColor(chart.getContext(), R.color.chart_blue));

        List<PointValue> targetPoints = model.attemptModels.size() > 1 ? getPointValues(getTargetArray(new ArrayList<>(model.attemptModels))) : getPointValues(model.defaultTargetScore, model.defaultTargetScore);

        LineChartData data = getData(
                getLine(targetPoints, getColor(chart.getContext(), R.color.chart_red)).setHasPoints(false),
                line,
                getDummyLine(0f, model.maxScore + 1));

        setChartStyle(chart, data, model.maxScore, line.getValues().size());
    }

    public static void setupLifetimeChart(LineChartView chart, DrillModel model) {
        List<PointValue> points = getPointValues(new AimDrillModel(model, EnumSet.of(English.ANY)));
        Line line = getLine(points, getColor(chart.getContext(), R.color.chart_blue));

        LineChartData data = getData(
                getTargetLine(model.defaultTargetScore, getColor(chart.getContext(), R.color.chart_red)),
                line,
                getDummyLine(0, model.maxScore + 1));

        data.setAxisYRight(createYAxis(0, model.maxScore + 1, 1, 0));
        data.setAxisXTop(createXAxis(chart.getContext(), createHistoryAxisLabels()));

        chart.setLineChartData(data);

        setViewPort(chart, 0, 7, 0, model.maxScore + 1);
    }

    public static void setupAimChart(LineChartView chart, AimDrillModel model) {
        Line line = getLine(getPointValues(model), getColor(chart.getContext(), R.color.chart_blue));

        Line targetLine = getTargetLine(model.targetScore, getColor(chart.getContext(), R.color.chart_red));
        Line dummyLine = getDummyLine(0f, 1.2f);

        LineChartData data = getData(targetLine, line, dummyLine);

        data.setAxisYRight(createYAxis(0, 1.2f, .1f, 1));
        data.setAxisXTop(createXAxis(chart.getContext(), createHistoryAxisLabels()));
        chart.setLineChartData(data);

        setViewPort(chart, 0, 7, 0, 1.2f);
    }

    private static void setChartStyle(LineChartView chart, LineChartData data, int max, int points) {
        Axis xAxis = Axis.generateAxisFromRange(0f, points, 1)
                .setHasSeparationLine(true)
                .setHasLines(true)
                .setTextSize(0);

        data.setAxisYRight(createYAxis(0, max + 1, 1, 0));
        data.setAxisXBottom(xAxis);
        chart.setLineChartData(data);

        setViewPort(chart, 0, 7, 0, max + 1);
    }

    private static Axis createXAxis(Context context, List<AxisValue> axisValues) {
        Axis axis = new Axis(axisValues);
        axis.setHasTiltedLabels(true)
                .setTextColor(getColor(context, R.color.primary_text))
                .setHasLines(true).setInside(true);
        return axis;
    }

    private static Axis createYAxis(float start, float stop, float step, int decimalDigitsNumber) {
        return Axis.generateAxisFromRange(start, stop, step)
                .setHasSeparationLine(true)
                .setFormatter(new SimpleAxisValueFormatter(decimalDigitsNumber))
                .setHasLines(true);
    }

    private static List<AxisValue> createHistoryAxisLabels() {
        List<AxisValue> axisValues = new ArrayList<>();
        axisValues.add(new AxisValue(0));
        List<String> labels = Arrays.asList("All time", "6 months", "3 months", "1 month", "Last week", "Today");
        for (int i = 0; i < labels.size(); i++) {
            AxisValue axisValue = new AxisValue(i + 1);
            axisValue.setLabel(labels.get(i));
            axisValues.add(axisValue);
        }
        axisValues.add(new AxisValue(7));
        return axisValues;
    }

    private static void setViewPort(Chart chart, float left, float right, float bottom, float top) {
        Viewport viewport = new Viewport(chart.getMaximumViewport());

        viewport.left = left;
        viewport.right = right;
        viewport.bottom = bottom;
        viewport.top = top;
        chart.setCurrentViewport(viewport);
    }
    private static LineChartData getData(Line... lines) {
        return new LineChartData(Arrays.asList(lines));
    }

    private static Line getTargetLine(float target, @ColorInt int color) {
        return getLine(getPointValues(target, target, target, target, target, target, target, target), color)
                .setHasPoints(false);
    }

    private static Line getTargetLine(int target, @ColorInt int color) {
        return getLine(getPointValues(target, target, target, target, target, target, target, target), color)
                .setHasPoints(false);
    }

    private static Line getDummyLine(float min, float max) {
        return getLine(getPointValues(min, max), Color.TRANSPARENT);
    }

    private static Line getLine(List<PointValue>values, @ColorInt int color) {
        return new Line(values)
                .setPointRadius(4)
                .setColor(color)
                .setPointColor(color)
                .setHasLabels(false)
                .setStrokeWidth(2);
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

    private static List<PointValue> getPointValues(AimDrillModel model) {
        List<PointValue> pointValues = new ArrayList<>();

        int points = 1;
        if (model.allTimeAverageAttempts > 0) {
            pointValues.add(new PointValue(points, model.allTimeAverage));
        }
        points++;
        if (model.sixMonthAverageAttempts > 0) {
            pointValues.add(new PointValue(points, model.sixMonthAverage));
        }
        points++;
        if (model.threeMonthAverageAttempts > 0) {
            pointValues.add(new PointValue(points, model.threeMonthAverage));
        }
        points++;
        if (model.oneMonthAverageAttempts > 0) {
            pointValues.add(new PointValue(points, model.oneMonthAverage));
        }
        points++;
        if (model.weekAverageAttempts > 0) {
            pointValues.add(new PointValue(points, model.weekAverage));
        }
        points++;
        if (model.sessionAverageAttempts > 0) {
            pointValues.add(new PointValue(points, model.sessionAverage));
        }

        return pointValues;
    }
}
