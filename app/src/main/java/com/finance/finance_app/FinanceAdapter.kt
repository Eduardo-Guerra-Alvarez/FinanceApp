package com.finance.finance_app

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class FinanceAdapter (var financeList:MutableList<Finance>, context: Context) : RecyclerView.Adapter<FinanceViewHolder>() {

    private var db:FinanceDatabaseHelper = FinanceDatabaseHelper(context)
    private var filteredFinanceList: MutableList<Finance> = financeList.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinanceViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return FinanceViewHolder(layoutInflater.inflate(R.layout.list_element, parent, false))
    }

    override fun getItemCount(): Int {
        return filteredFinanceList.size
    }

    override fun onBindViewHolder(holder: FinanceViewHolder, position: Int) {
        val item = filteredFinanceList[position]
        holder.render(item)
    }

    fun onSwiped(holder: RecyclerView.ViewHolder){
        val position = holder.adapterPosition
        val finance = filteredFinanceList[position]
        finance.id?.let { db.deleteFinance(it) }
        refreshData(db.getFinances())
        //Toast.makeText(holder.itemView.context, "Se elimino exitosamente", Toast.LENGTH_SHORT).show()
        Snackbar.make(holder.itemView, "Elemento eliminado", Snackbar.LENGTH_LONG)
            .setAction("Deshacer") {
                // Llama a la función de deshacer del adaptador
                undoSwipe(finance)
            }
            .show()
    }

    fun undoSwipe(finance: Finance) {
        db.reInsertFinance(finance)
        refreshData(db.getFinances())
    }

    fun refreshData(newFinance: List<Finance>) {
        financeList = newFinance.toMutableList()
        filteredFinanceList = financeList
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        filteredFinanceList = if (query.isEmpty()) {
            financeList.toMutableList()
        } else {
            val result = financeList.filter { it.description.contains(query, true) }.toMutableList()
            println("Filtered list size: ${result.size}")
            result
        }
        notifyDataSetChanged()
    }
}