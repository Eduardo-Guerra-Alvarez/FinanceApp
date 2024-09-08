package com.finance.finance_app

import android.app.AlertDialog
import android.app.Dialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment

class YearPickerDialog (val listener: (year: Int) -> Unit) : DialogFragment(){

    private lateinit var yearPicker: NumberPicker

    private var selectedYear: Int = -1

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = LayoutInflater.from(it)
            val view = inflater.inflate(R.layout.dialog_year_picker, null)

            yearPicker = view.findViewById(R.id.only_year_picker)

            yearPicker.minValue = 2015
            yearPicker.maxValue = Calendar.getInstance().get(Calendar.YEAR)

            if (selectedYear != -1) {
                yearPicker.value = selectedYear
            }

            builder.setView(view)
                .setPositiveButton("Aceptar") {_, _ ->
                    selectedYear = yearPicker.value
                    listener(yearPicker.value)
                }
                .setNegativeButton("Cancelar") { dialog, _ ->
                    dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun setInitialDate(year: Int) {
        selectedYear = year
    }
}