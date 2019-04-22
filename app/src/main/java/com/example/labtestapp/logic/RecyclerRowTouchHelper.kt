package com.example.labtestapp.logic

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

 class RecyclerRowTouchHelper(
     private val listener : RecyclerItemTouchHelperListener
 ):
     ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

     override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
         listener.onSwiped(viewHolder, direction, viewHolder.adapterPosition)
     }

     override fun onMove(
         recyclerView: RecyclerView,
         viewHolder: RecyclerView.ViewHolder,
         target: RecyclerView.ViewHolder
     ): Boolean {
         return true
     }

     interface RecyclerItemTouchHelperListener {
         fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int)
         //fun onItemClick(viewHolder: RecyclerView.ViewHolder)
     }
}