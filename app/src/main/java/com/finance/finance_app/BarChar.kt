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
    private lateinit var selectYear: Spinner

    private val calendar = android.icu.util.Calendar.getInstance()
    private val year = calendar.get(android.icu.util.Calendar.YEAR)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_bar_char,container, false)
        db = FinanceDatabaseHelper(requireContext())
        listening(view)
        getSelectYear(view)
        getDataByMonth(view, year.toString())

        return view
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_bar_char, container, false)
    }

    private fun listening(view: View) {
        val years = db.getYears()
        selectYear = view.findViewById(R.id.selectYear)
        val adapter = ArrayAdapter(
            view.context,
            android.R.layout.simple_spinner_item,
            years)
        selectYear.adapter = adapter

        val currentPosition = years.indexOf(year.toString())
        selectYear.setSelection(currentPosition)
    }

    private fun getSelectYear(newView: View) {
        selectYear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {
                val yearSelected = parent?.getItemAtPosition(position).toString()
                // Aquí puedes realizar alguna acción con el año seleccionado, por ejemplo, hacer una consulta a la base de datos
                getDataByMonth(newView, yearSelected)
                Log.d("Año seleccionado", yearSelected)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Este método se llama cuando no se selecciona ningún elemento
            }
        }
    }


    private fun getDataByMonth(view: View, year: String) {
        val spendByMonth = db.getSpendByMonth(year).toMutableList()

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