package com.finance.finance_app

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate

enum class MonthOfYear(val num: Int) {
    ENE(1),
    FEB(2),
    MAR(3),
    ABR(4),
    MAY(5),
    JUN(6),
    JUL(7),
    AGO(8),
    SEP(9),
    OCT(10),
    NOV(11),
    DIC(12);

    companion object {
        fun fromValue(value: Int): MonthOfYear? {
            return values().firstOrNull { it.num == value }
        }
    }
}

class BarChar : Fragment() {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var db: FinanceDatabaseHelper
    private lateinit var barChart: BarChart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_bar_char,container, false)


        db = FinanceDatabaseHelper(requireContext())
        getDataByMonth(view)


        return view
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_bar_char, container, false)
    }

    private fun getDataByMonth(view: View) {
        val spendByMonth = db.getSpendByMonth().toMutableList()

        barChart = view.findViewById(R.id.barChar)


        // Create Data example
        var i:Int = 0
        val months = ArrayList<String>()
        val entries = ArrayList<BarEntry>()

        for ((month, spend) in spendByMonth) {
            months.add(MonthOfYear.fromValue(month).toString())
            entries.add(BarEntry(i.toFloat(), spend))
            i++
        }

        val colors = listOf(
            Color.parseColor("#B29462"),
            Color.parseColor("#95A5A6"),
            Color.parseColor("#F2F2F2"),
            Color.parseColor("#92ba92"),
            Color.parseColor("#008585")
        )

        // DataSet
        val dataSet = BarDataSet(entries, "")
        dataSet.colors = colors
        dataSet.valueTextColor = Color.WHITE
        dataSet.valueTextSize = 16f

        val data = BarData(dataSet)

        val xAxis = barChart.xAxis
        val yAxis = barChart.axisLeft
        barChart.axisRight.textColor = Color.WHITE

        barChart.data = data
        barChart.description.isEnabled = false

        // put letters in white color
        xAxis.gridColor = Color.WHITE
        xAxis.textColor = Color.WHITE
        yAxis.textColor = Color.WHITE
        yAxis.gridColor = Color.WHITE


        xAxis.valueFormatter = IndexAxisValueFormatter(months)
        xAxis.granularity = 1f // a value for each bar
        barChart.invalidate()

        Log.i("ListaSpend", months.toString())

    }

}