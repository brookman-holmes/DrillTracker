package com.brookmanholmes.drilltracker.presentation.view


import android.content.Context
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.brookmanholmes.drilltracker.R
import com.brookmanholmes.drilltracker.presentation.model.DistanceResult
import com.brookmanholmes.drilltracker.presentation.model.SpinResultModel
import com.brookmanholmes.drilltracker.presentation.model.TargetDataModel
import lecho.lib.hellocharts.model.*
import lecho.lib.hellocharts.util.ChartUtils
import lecho.lib.hellocharts.view.ColumnChartView
import lecho.lib.hellocharts.view.PieChartView
import java.util.*
import kotlin.collections.ArrayList

fun setDistanceChart(chart: ColumnChartView, model: TargetDataModel) {
    val columns = ArrayList<Column>()

    val distances = EnumSet.complementOf(EnumSet.of(DistanceResult.NO_DATA))

    for (distance in distances) {
        val subColumns = ArrayList<SubcolumnValue>()
        subColumns.add(SubcolumnValue(model.getDistanceValue(distance).toFloat(), getColor(chart.context, distance)))
        val column = Column(subColumns)
        column.setHasLabels(true)
        columns.add(column)
    }

    val axisValues = ArrayList<AxisValue>()
    for (i in 0..5) {
        val float = (i.toFloat()) / 2f
        axisValues.add(AxisValue(i.toFloat(), float.toString().toCharArray()))
    }
    val axis = Axis(axisValues)
        .setHasLines(false)
        .setHasSeparationLine(true)
        .setName("<---- Slow        Diamonds off target        Fast ---->")
    val data = ColumnChartData(columns)
    data.axisXBottom = axis

    chart.columnChartData = data
}

fun setDistanceChartWithSpeed(chart: ColumnChartView, today: TargetDataModel, history: TargetDataModel) {
    val columns = ArrayList<Column>()
    for (i in 0..8) {
        val subColumns = ArrayList<SubcolumnValue>()

        val todayTriple = today.getDistanceValues()[i]
        val historyTriple = history.getDistanceValues()[i]
        subColumns.add(SubcolumnValue(todayTriple.third.toFloat(), getColor(chart.context, todayTriple.first)))
        subColumns.add(SubcolumnValue(historyTriple.third.toFloat(), ChartUtils.darkenColor(ChartUtils.darkenColor(getColor(chart
            .context,
            historyTriple.first)))))
        val column = Column(subColumns)
        column.setHasLabels(true)
        columns.add(column)
    }

    val axisValues = ArrayList<AxisValue>()
    for (i in 0..8) {
        val float = (i.toFloat() - 4f) / 2f
        axisValues.add(AxisValue(i.toFloat(), float.toString().toCharArray()))
    }
    val axis = Axis(axisValues)
        .setHasLines(false)
        .setHasSeparationLine(true)
        .setName("<---- Slow        Diamonds off target        Fast ---->")
    val data = ColumnChartData(columns)
    data.isStacked = true
    data.axisXBottom = axis

    chart.columnChartData = data
}

fun setSpinResultPieChart(chart:PieChartView, model: SpinResultModel) {
    val correctValues = SliceValue(model.correct.toFloat(), ContextCompat.getColor(chart.context, R.color.chart_speed_correct))
    val lessValues = SliceValue(model.less.toFloat(), ContextCompat.getColor(chart.context, R.color.chart_speed_little))
    val moreValues = SliceValue(model.more.toFloat(), ContextCompat.getColor(chart.context, R.color.chart_speed_much))

    val data = PieChartData(listOf(correctValues, lessValues, moreValues))
    data.setHasLabels(true)
    chart.pieChartData = data
}

fun setSpinResultPieChart(chart:PieChartView, correct: Float, less: Float, more: Float) {
    val correctValues = SliceValue(correct, ContextCompat.getColor(chart.context, R.color.chart_speed_correct))
    val lessValues = SliceValue(less, ContextCompat.getColor(chart.context, R.color.chart_speed_little))
    val moreValues = SliceValue(more, ContextCompat.getColor(chart.context, R.color.chart_speed_much))

    val data = PieChartData(listOf(correctValues, lessValues, moreValues))
    chart.pieChartData = data
}

@ColorInt
fun getColor(context: Context, distanceResult: DistanceResult): Int {
    @ColorRes
    val colorRes = when (distanceResult) {
        DistanceResult.ZERO -> R.color.chart_excellent
        DistanceResult.ONE_HALF -> R.color.chart_good
        DistanceResult.ONE -> R.color.chart_fair
        DistanceResult.ONE_AND_HALF -> R.color.chart_poor
        DistanceResult.OVER_ONE_AND_HALF -> R.color.chart_really_poor
        DistanceResult.NO_DATA -> throw IllegalStateException("Looks like you're trying to deal with something with no data")
    }

    return ContextCompat.getColor(context, colorRes)
}