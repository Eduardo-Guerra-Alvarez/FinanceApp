package com.finance.finance_app

import android.content.Intent
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Locale


class Home : Fragment() {

    private lateinit var btnAdd: FloatingActionButton
    private lateinit var db: FinanceDatabaseHelper
    private lateinit var financeAdapter: FinanceAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var financeList: MutableList<Finance>
    private lateinit var searchView: SearchView
    private lateinit var searchList: ArrayList<Finance>

    // variables to control pagination
    private var isLoading = false
    private var currentPage = 1
    private val itemsPerPage = 10

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home,container, false)

        db = FinanceDatabaseHelper(requireContext())

        // Init finaceList, searchList and financeAdapter
        financeList = db.getFinances().toMutableList()//.take(10).toMutableList()
        searchList = ArrayList<Finance>()
        searchList.addAll(financeList)
        financeAdapter = FinanceAdapter(searchList, requireContext())


        initListener(view)
        initRecycleView()
        initSearchView()
        // Inflate the layout for this fragment
        return view
    }

    private fun initListener(view: View) {
        try {
            searchView = view.findViewById(R.id.searchView)
            recyclerView = view.findViewById<RecyclerView>(R.id.recycleFinance)
            btnAdd = view.findViewById(R.id.fabAdd) // init button add new spend
            btnAdd.setOnClickListener{ // on click then show view add new spend
                val intent = Intent(requireActivity(), CreateFinance::class.java)
                startActivity(intent)
            }
        } catch (e: Exception) {
            println("Error Listener: " +  e)
            e.printStackTrace()
        }

    }

    // Init searchView to search elements
    private fun initSearchView() {
        try {
            // Clear focus fist to init search view to avoid open keyboard
            searchView.clearFocus()

            // configure listener to manage changes and text
            searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    // Clean focus after to send
                    searchView.clearFocus()
                    return true // Return true to indicate that the event had been manage
                }

                // this function change each time that the text changes
                override fun onQueryTextChange(newText: String?): Boolean {

                    // change the text to lower case
                    val searchText = newText?.toLowerCase(Locale.getDefault()) ?: ""

                    // filter list that is not empty and
                    val filteredList = if (searchText.isNotEmpty()) {
                        financeList.filter {
                            it.description.toLowerCase(Locale.getDefault()).contains(searchText)
                        }
                    } else {
                        // if there is any text then show all list
                        financeList
                    }

                    // update the adapter with the filter list
                    (recyclerView.adapter as FinanceAdapter).updateList(filteredList.toMutableList())

                    return false
                }

            })
        } catch (e: Exception) {
            println("Error SearchView: " + e)
            e.printStackTrace()
        }
    }

    private fun initRecycleView(){
        try {
            // show recycleView
            val layoutManager = LinearLayoutManager(requireContext())
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = financeAdapter

            isLoading = false
            currentPage = 2  // We start in the page two

            // add OnScrollListener to check when user go to end of the list
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    // check if user go to end recycleView
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                    if (!isLoading && lastVisibleItemPosition == totalItemCount -1) {
                        loadMoreData()
                    }
                }
            })

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

                // to draw when swipe on left
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
                        val icon = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_delete_24) // icon trash color white
                        val iconMargin = (itemView.height - icon!!.intrinsicHeight) / 2
                        val iconTop = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
                        val iconButton = iconTop + icon.intrinsicHeight

                        if (dX < 0) {
                            val iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
                            val iconRight = itemView.right - iconMargin
                            icon.setBounds(iconLeft, iconTop, iconRight, iconButton)
                            val background = ColorDrawable(ContextCompat.getColor(requireContext(), R.color.red_delete)) // red color
                            background.setBounds(itemView.right +  dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                            background.draw(c)
                            icon.draw(c)
                        }
                    }
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                }
            })
            itemTouchHelper.attachToRecyclerView(recyclerView)
        } catch (e: Exception) {
            println(e)
        }
    }

    private fun loadMoreData() {
        isLoading = true

        // simulate recoil in data load
        recyclerView.post{
           val newFinances = db.getFinancesByPage(currentPage, itemsPerPage)

            financeAdapter.financeList.addAll(newFinances)
            financeAdapter.notifyDataSetChanged()

            currentPage++
            isLoading = false
        }
    }


    // load data when show view
    override fun onResume() {
        super.onResume()
        financeAdapter.refreshData(db.getFinances())
    }
}