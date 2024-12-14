package com.finance.finance_app

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.finance.finance_app.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    /*
    private lateinit var btnAdd:FloatingActionButton
    private lateinit var db: FinanceDatabaseHelper
    private lateinit var financeAdapter: FinanceAdapter
     */

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(Home())
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //db = FinanceDatabaseHelper(this)
        //financeAdapter = FinanceAdapter(db.getFinances().toMutableList(), this)
        //initRecycleView()
        //initListeners()
        buttonNavigation(binding)

    }

    // function to create buttonNavigation bar
    private fun buttonNavigation(binding: ActivityMainBinding) {
        // create onSelect
        binding.buttonNavigationView.setOnItemSelectedListener{ item ->
            when(item.itemId) {
                R.id.home -> { // when select pie then show pie fragment
                    replaceFragment(Home())
                    true
                }
                R.id.pie -> { // when select pie then show pie fragment
                    replaceFragment(PieChar())
                    true
                }
                R.id.barchar -> { // when select barChar then show BarChar fragment
                    replaceFragment(BarChar())
                    true
                }
                else -> false
            }
        }
    }

    // to replace fragment in layout
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, fragment)
            .commit()
    }

    // init listeners
    /*
    private fun initListeners() {
        btnAdd = findViewById(R.id.fabAdd) // init button add new spend
        btnAdd.setOnClickListener{ // on click then show view add new spend
            val intent = Intent(this, CreateFinance::class.java)
            startActivity(intent)
        }
    }

    private fun initRecycleView(){

        // show recycleView
        val recyclerView = findViewById<RecyclerView>(R.id.recycleFinance)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = financeAdapter

        // to swipe on left
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

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    val itemView = viewHolder.itemView
                    val icon = ContextCompat.getDrawable(applicationContext, R.drawable.baseline_delete_24)
                    val iconMargin = (itemView.height - icon!!.intrinsicHeight) / 2
                    val iconTop = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
                    val iconButton = iconTop + icon.intrinsicHeight

                    if (dX < 0) {
                        val iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
                        val iconRight = itemView.right - iconMargin
                        icon.setBounds(iconLeft, iconTop, iconRight, iconButton)

                        val background = ColorDrawable(getColor(R.color.red_delete))
                        background.setBounds(itemView.right +  dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                        background.draw(c)
                        icon.draw(c)
                    }
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

            }

        })
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onResume() {
        super.onResume()
        financeAdapter.refreshData(db.getFinances())
    }

     */
}