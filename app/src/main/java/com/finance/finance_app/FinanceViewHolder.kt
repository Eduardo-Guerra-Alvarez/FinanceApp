package com.finance.finance_app

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView

class FinanceViewHolder(view: View) : RecyclerView.ViewHolder(view){

    val category = view.findViewById<TextView>(R.id.category)
    val description = view.findViewById<TextView>(R.id.description)
    val spend = view.findViewById<TextView>(R.id.spend)
    val date = view.findViewById<TextView>(R.id.date)


    fun render(finance:Finance) {
        category.text = finance.category.toString()
        description.text = finance.description.toString()
        spend.text = finance.spend.toString()
        date.text = finance.date
    }
}