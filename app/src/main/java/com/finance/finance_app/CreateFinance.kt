package com.finance.finance_app

import android.icu.util.Calendar
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CreateFinance : AppCompatActivity() {

    private lateinit var spinnerCategory:Spinner
    private lateinit var editTxtSubcategory: EditText
    private lateinit var editTxtDescription: EditText
    private lateinit var editTxtSpend:EditText
    private lateinit var editTxtDate:EditText
    private lateinit var btnAdd:Button
    val calendar = Calendar.getInstance()
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val month = calendar.get(Calendar.MONTH)+1 // add 1
    val year = calendar.get(Calendar.YEAR)

    private lateinit var db:FinanceDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_finance)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        listeners()
        db = FinanceDatabaseHelper(this)
    }

    private fun listeners() {
        editTxtDate = findViewById(R.id.editTextDate)
        val txt = String.format("%d-%02d-%02d", year, month, day)
        editTxtDate.setText(txt)
        editTxtDate.setOnClickListener{
            showDataPicker()
        }

        spinnerCategory = findViewById(R.id.spinnerCategory)
        ArrayAdapter.createFromResource(
            this,
            R.array.category_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner.
            spinnerCategory.adapter = adapter
        }

        editTxtSpend = findViewById(R.id.editTextSpend)
        editTxtDescription = findViewById((R.id.editTextDescription))

        btnAdd = findViewById(R.id.btnAddFinance)
        btnAdd.setOnClickListener {

            val category = selectCategory().toString()
            val subcategory = ""
            val description = editTxtDescription.text.toString()
            val spend = editTxtSpend.text.toString()
            val date = editTxtDate.text.toString()

            if((spend == "") || (date == "")) {
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
            } else  {
                saveFinance(category, subcategory, description, spend, date)
            }
        }
    }

    private fun showDataPicker() {
        val datePicker = DatePickerFragment{day,month,year -> onDateSelected(day, month, year)}
        datePicker.show(supportFragmentManager, "datePicker")
    }

    private fun onDateSelected(day:Int, month:Int, year:Int) {
        val txt = String.format("%d-%02d-%02d", year, month + 1, day)
        editTxtDate.setText(txt)
    }

    private fun saveFinance(
        category:String,
        subcategory:String,
        description:String,
        spend:String,
        date:String
    ) {
        val finance = Finance(null,category, subcategory, description, spend.toFloat(), date)
        db.insertFinance(finance)
        finish()
    }

    private fun selectCategory(): String? {
        val selectedPosition = spinnerCategory.selectedItemPosition

        if (selectedPosition != -1) {
            var selectedString: String? = null
            if (spinnerCategory.adapter is ArrayAdapter<*>) {
                val adapter = spinnerCategory.adapter as ArrayAdapter<*>
                selectedString = adapter.getItem(selectedPosition).toString()
            } else {
                selectedString = spinnerCategory.selectedItem as String
            }

            // Do something with the selectedString
            return selectedString
        } else {
            return null
        }
    }
}










