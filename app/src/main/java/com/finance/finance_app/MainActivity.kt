package com.finance.finance_app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var btnAdd:FloatingActionButton
    private lateinit var db: FinanceDatabaseHelper
    private lateinit var financeAdapter: FinanceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = FinanceDatabaseHelper(this)
        financeAdapter = FinanceAdapter(db.getFinances().toMutableList(), this)
        initRecycleView()
        initListeners()
    }

    private fun initListeners() {
        btnAdd = findViewById(R.id.fabAdd)
        btnAdd.setOnClickListener{
            val intent = Intent(this, CreateFinance::class.java)
            startActivity(intent)
        }
    }

    private fun initRecycleView(){
        val recyclerView = findViewById<RecyclerView>(R.id.recycleFinance)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = financeAdapter

        // En tu actividad o fragmento:
        val itemTouchHelper = ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: ViewHolder, position: Int) {
                financeAdapter.onSwiped(viewHolder, position)
            }
        })
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onResume() {
        super.onResume()
        financeAdapter.refreshData(db.getFinances())
    }
}