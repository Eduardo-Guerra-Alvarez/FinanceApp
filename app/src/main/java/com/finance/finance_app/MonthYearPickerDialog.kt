package com.finance.finance_app

import android.app.AlertDialog
import android.app.Dialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment

class MonthYearPickerDialog(val listener: (month: Int, year: Int) -> Unit) : DialogFragment() {

    private lateinit var monthPicker: NumberPicker
    private lateinit var yearPicker: NumberPicker

    private var selectedMonth: Int = -1  // store selected month
    private var selectedYear: Int = -1   // store selected year

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater
            = LayoutInflater.from(it)
            val view = inflater.inflate(R.layout.dialog_month_year_picker,
            null)

            monthPicker = view.findViewById(R.id.month_picker)
            yearPicker = view.findViewById(R.id.year_picker)

            // Configura los valores de los NumberPicker
            val months = resources.getStringArray(R.array.months) // Array de nombres de meses
            monthPicker.minValue = 0
            monthPicker.maxValue = months.size - 1
            monthPicker.displayedValues = months
            yearPicker.minValue = 2015
            yearPicker.maxValue = Calendar.getInstance().get(Calendar.YEAR)

            // if there is a save state then restore the selected month and year
            if (selectedMonth != -1) {
                monthPicker.value = selectedMonth
            }
            if (selectedYear != -1) {
                yearPicker.value = selectedYear
            }

            builder.setView(view)
                .setPositiveButton("Aceptar") { _, _ ->
                    selectedMonth = monthPicker.value // save month selected
                    selectedYear = yearPicker.value // save year selected
                    listener(monthPicker.value + 1, yearPicker.value) // Sumamos 1 al mes porque los índices empiezan en 0
                }
                .setNegativeButton("Cancelar") { dialog, _ ->
                    dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun setInitialDate(month: Int, year: Int) {
        selectedMonth = month - 1 //restore 1 because index start in 0
        selectedYear = year
    }
}