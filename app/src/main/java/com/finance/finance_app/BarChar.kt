package com.finance.finance_app

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.io.IOError
import kotlin.jvm.Throws

class BarChar : Fragment() {

    private lateinit var db: FinanceDatabaseHelper
    private lateinit var barChart: BarChart
    private lateinit var editYear: EditText

    private val calendar = android.icu.util.Calendar.getInstance()
    private val year = calendar.get(android.icu.util.Calendar.YEAR)

    private var selectedYear = year

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_bar_char,container, false)
        db = FinanceDatabaseHelper(requireContext())
        listening(view)
        getDataByMonth(year)

        return view
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_bar_char, container, false)
    }

    private fun listening(view: View) {
        barChart = view.findViewById(R.id.barChar)
        editYear = view.findViewById(R.id.editTxtYear)
        editYear.setText(year.toString())

        editYear.setOnClickListener {
            showDataPicker()
        }
    }

    private fun showDataPicker() {
        val customPicker = YearPickerDialog { year ->
            selectedYear = year
            onDateSelected(year)
        }

        customPicker.setInitialDate(selectedYear)
        customPicker.show(childFragmentManager, "datePicker")
    }

    private fun onDateSelected(year:Int) {
        editYear.setText(year.toString())
        getDataByMonth(year)
    }

    private fun getDataByMonth(year: Int) {
        val spendByMonth = db.getSpendByMonth(year.toString()).toMutableList()

        // Create Data example
        var i:Int = 0
        val months = ArrayList<String>()
        val entries = ArrayList<BarEntry>()

        for ((month, spend) in spendByMonth) {
            months.add(MonthOfYear.fromValue(month).toString())
            entries.add(BarEntry(i.toFloat(), spend))
            i++
        }

        // DataSet
        val dataSet = BarDataSet(entries, "")
        dataSet.colors = ColorGraphs.colors
        dataSet.valueTextColor = Color.WHITE
        dataSet.valueTextSize = 16f

        val data = BarData(dataSet)

        val xAxis = barChart.xAxis
        val yAxis = barChart.axisLeft
        barChart.axisRight.textColor = Color.WHITE

        barChart.data = data
        barChart.description.isEnabled = false
        val legend = barChart.legend
        legend.textColor = Color.WHITE

        // put letters in white color
        xAxis.gridColor = Color.WHITE
        xAxis.textColor = Color.WHITE
        yAxis.textColor = Color.WHITE
        yAxis.gridColor = Color.WHITE


        xAxis.valueFormatter = IndexAxisValueFormatter(months)
        xAxis.granularity = 1f // a value for each bar
        barChart.invalidate()
    }

}