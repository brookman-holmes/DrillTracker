package com.brookmanholmes.drilltracker.presentation.view.util;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.presentation.model.AimDrillModel;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;
import com.brookmanholmes.drilltracker.presentation.model.English;
import com.brookmanholmes.drilltracker.presentation.model.PositionalDrillModel;
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

    public static void setupChart(PieChartView vSpinChart, PieChartView hSpinChart, PieChartView speedChart, PieChartView distanceChart, PositionalDrillModel model) {
        if (model.getAttempts() == 0) {
            List<SliceValue> blankSlices = new ArrayList<>();
            blankSlices.add(new SliceValue(1));
            speedChart.setPieChartData(
                    new PieChartData(blankSlices)
                            .setHasCenterCircle(true)
                            .setCenterText1("Speed")
                            .setCenterText1FontSize(14)
            );
            hSpinChart.setPieChartData(
                    new PieChartData(blankSlices)
                            .setHasCenterCircle(true)
                            .setCenterText1("Horizontal")
                            .setCenterText2("spin")
                            .setCenterText2FontSize(14)
                            .setCenterText1FontSize(14)
            );
            vSpinChart.setPieChartData(
                    new PieChartData(blankSlices)
                            .setHasCenterCircle(true)
                            .setCenterText1("Vertical")
                            .setCenterText2("spin")
                            .setCenterText2FontSize(14)
                            .setCenterText1FontSize(14)
            );
            distanceChart.setPieChartData(
                    new PieChartData(blankSlices)
                            .setHasCenterCircle(true)
                            .setCenterText1("Distance")
                            .setCenterText2("from target")
                            .setCenterText2FontSize(14)
                            .setCenterText1FontSize(14)
            );
        } else {
            hSpinChart.setPieChartData(createPieChartData(
                    hSpinChart.getContext(),
                    "Horizontal",
                    "spin",
                    model.gethSpinCorrect(),
                    model.gethSpinMore(),
                    model.gethSpinLess()
            ));
            vSpinChart.setPieChartData(createPieChartData(
                    hSpinChart.getContext(),
                    "Vertical",
                    "spin",
                    model.getvSpinCorrect(),
                    model.getvSpinMore(),
                    model.getvSpinLess()
            ));
            speedChart.setPieChartData(createPieChartData(
                    hSpinChart.getContext(),
                    "Speed",
                    model.getSpeedCorrect(),
                    model.getSpeedHard(),
                    model.getSpeedSoft()
            ));
            distanceChart.setPieChartData(createPieChartData(
                    distanceChart.getContext(),
                    model.getDistanceZero(),
                    model.getDistanceSix(),
                    model.getDistanceTwelve(),
                    model.getDistanceEighteen(),
                    model.getDistanceTwentyFour()
            ));
        }
    }

    private static PieChartData createPieChartData(Context context, float value1, float value2, float value3, float value4, float value5) {
        List<SliceValue> sliceValues = new ArrayList<>();
        sliceValues.add(new SliceValue(value1, getColor(context, R.color.chart_green)));
        sliceValues.add(new SliceValue(value2, getColor(context, R.color.chart_yellow)));
        sliceValues.add(new SliceValue(value3, getColor(context, R.color.chart_orange)));
        sliceValues.add(new SliceValue(value4, getColor(context, R.color.chart_red)));
        sliceValues.add(new SliceValue(value5, getColor(context, R.color.chart_redder)));
        PieChartData data = new PieChartData(sliceValues);
        data.setHasLabels(true)
                .setHasCenterCircle(true)
                .setCenterText1("Distance")
                .setCenterText2("from target")
                .setCenterText2FontSize(14)
                .setCenterText1FontSize(14);
        return data;
    }

    public static void setupChart(PieChartView speedChart, PieChartView thicknessChart, PieChartView spinChart, SafetyDrillModel model) {
        if (model.getAttempts() == 0) {
            List<SliceValue> blankSlices = new ArrayList<>();
            blankSlices.add(new SliceValue(1));
            speedChart.setPieChartData(new PieChartData(blankSlices).setHasCenterCircle(true).setCenterText1("Speed").setCenterText1FontSize(14));
            spinChart.setPieChartData(new PieChartData(blankSlices).setHasCenterCircle(true).setCenterText1("Spin").setCenterText1FontSize(14));
            thicknessChart.setPieChartData(new PieChartData(blankSlices).setHasCenterCircle(true).setCenterText1("Thickness").setCenterText1FontSize(14));
        } else {
            speedChart.setPieChartData(createPieChartData(speedChart.getContext(), "Speed", model.getSpeedCorrect(), model.getSpeedHard(), model.getSpeedSoft()));
            thicknessChart.setPieChartData(createPieChartData(spinChart.getContext(), "Thickness", model.getThicknessCorrect(), model.getThicknessThick(), model.getThicknessThin()));
            spinChart.setPieChartData(createPieChartData(spinChart.getContext(), "Spin", model.getSpinCorrect(), model.getSpinMore(), model.getSpinLess()));
        }
    }

    private static PieChartData createPieChartData(Context context, String label, float value1, float value2, float value3) {
        List<SliceValue> sliceValues = new ArrayList<>();
        sliceValues.add(new SliceValue(value1, getColor(context, R.color.chart_green)));
        sliceValues.add(new SliceValue(value2, getColor(context, R.color.chart_red)));
        sliceValues.add(new SliceValue(value3, getColor(context, R.color.chart_blue)));
        PieChartData data = new PieChartData(sliceValues);
        data.setHasLabels(true)
                .setCenterText1(label)
                .setCenterText1FontSize(14)
                .setHasCenterCircle(true);
        return data;
    }

    private static PieChartData createPieChartData(Context context, String label1, String label2, float value1, float value2, float value3) {
        List<SliceValue> sliceValues = new ArrayList<>();
        sliceValues.add(new SliceValue(value1, getColor(context, R.color.chart_green)));
        sliceValues.add(new SliceValue(value2, getColor(context, R.color.chart_red)));
        sliceValues.add(new SliceValue(value3, getColor(context, R.color.chart_blue)));
        PieChartData data = new PieChartData(sliceValues);
        data.setHasLabels(true)
                .setCenterText1(label1)
                .setCenterText2(label2)
                .setCenterText1FontSize(14)
                .setCenterText2FontSize(14)
                .setHasCenterCircle(true);
        return data;
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
