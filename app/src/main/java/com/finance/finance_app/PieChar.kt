package com.finance.finance_app

import android.graphics.Color
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter


class PieChar : Fragment() {

    private lateinit var db: FinanceDatabaseHelper
    private lateinit var pieChart: PieChart
    private lateinit var editMonthYear: EditText
    private val calendar = Calendar.getInstance()
    private val year = calendar.get(Calendar.YEAR)
    private val month = calendar.get(Calendar.MONTH) + 1

    private var selectedMonth = month  // selected month for default
    private var selectedYear = year  // selected year for default

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_pie_char, container, false)

        // init db
        db = FinanceDatabaseHelper(requireContext())
        listener(view)
        getDataByCategory(month, year)

        return view
    }

    // function listener where we init piechart and editMonthYear
    private fun listener(view: View) {
        pieChart = view.findViewById(R.id.pieChar)
        editMonthYear = view.findViewById(R.id.editTextMonthYear)
        val monthLetter = MonthOfYear.fromValue(month)
        val txt = String.format("$monthLetter $year")
        editMonthYear.setText(txt)

        // on Click to show Dialog
        editMonthYear.setOnClickListener {
            showDatapicker()
        }
    }


    // Show dialog picker
    private fun showDatapicker() {
        // get data and show the dialog
        val customPicker = MonthYearPickerDialog { month, year ->
            selectedMonth = month // pass month selected
            selectedYear = year // pass year selected
            onDateSelected(month, year)
        }
        customPicker.setInitialDate(selectedMonth, selectedYear)
        customPicker.show(childFragmentManager , "datePicker")
    }


    private fun onDateSelected(month:Int, year:Int) {
        val monthLetter = MonthOfYear.fromValue(month)
        val txt = String.format("$monthLetter $year")
        editMonthYear.setText(txt)
        getDataByCategory(month, year)
    }

    private fun getDataByCategory(month: Int, year: Int) {
        val month_zero = String.format("%02d", month)
        Log.i("month_year", "Este es el mes $month_zero y este es el año $year")
        val spendByCategory = db.getSpendByCategory(month_zero, year.toString()).toMutableList()

        //Create data example
        val entries = ArrayList<PieEntry>()

        for((category, spend) in spendByCategory) {
            entries.add(PieEntry(spend, category))
        }

        val dataSet = PieDataSet(entries, "")
        dataSet.valueTextSize = 12f
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueFormatter = PercentFormatter(pieChart) // show porcent



        dataSet.colors = ColorGraphs.colors

        val data = PieData(dataSet)

        pieChart.data = data
        pieChart.holeRadius = 5f
        pieChart.setHoleColor(Color.TRANSPARENT)
        pieChart.transparentCircleRadius = 5f
        pieChart.description.isEnabled = false
        pieChart.isDrawHoleEnabled = true
        pieChart.setDrawEntryLabels(true)

        val legend = pieChart.legend
        legend.textColor = Color.WHITE

        pieChart.invalidate()
    }

}