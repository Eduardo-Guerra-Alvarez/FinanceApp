package com.finance.finance_app

import android.content.Intent
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Home : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var btnAdd: FloatingActionButton
    private lateinit var db: FinanceDatabaseHelper
    private lateinit var financeAdapter: FinanceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home,container, false)

        db = FinanceDatabaseHelper(requireContext())
        financeAdapter = FinanceAdapter(db.getFinances().toMutableList(), requireContext())
        initAddListener(view)
        initRecycleView(view)
        // Inflate the layout for this fragment
        return view
    }

    private fun initAddListener(view: View) {
        btnAdd = view.findViewById(R.id.fabAdd) // init button add new spend
        btnAdd.setOnClickListener{ // on click then show view add new spend
            val intent = Intent(requireActivity(), CreateFinance::class.java)
            startActivity(intent)
        }
    }

    private fun initRecycleView(view: View){

        // show recycleView
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycleFinance)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
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

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, position: Int) {
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
                    val icon = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_delete_24)
                    val iconMargin = (itemView.height - icon!!.intrinsicHeight) / 2
                    val iconTop = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
                    val iconButton = iconTop + icon.intrinsicHeight

                    if (dX < 0) {
                        val iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
                        val iconRight = itemView.right - iconMargin
                        icon.setBounds(iconLeft, iconTop, iconRight, iconButton)
                        val background = ColorDrawable(ContextCompat.getColor(requireContext(), R.color.red_delete))
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

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Home().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}