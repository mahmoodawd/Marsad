package com.example.marsad.ui.weatheralerts.view

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.marsad.R
import com.example.marsad.data.model.AlertItem
import com.example.marsad.data.model.SavedLocation

class AlertItemTouchHelperCallback(
    context: Context,
    private val alertItemAdapter: AlertItemAdapter,
    private val onSwipedCallback: (item: AlertItem) -> Unit
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
    private val deleteIcon =
        ContextCompat.getDrawable(context, R.drawable.ic_delete)

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        val swipedItem: AlertItem = alertItemAdapter.alertItemList[position]
        onSwipedCallback(swipedItem)
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
        super.onChildDraw(
            c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val itemView = viewHolder.itemView
            val iconMargin = (itemView.height - deleteIcon!!.intrinsicHeight) / 2
            val iconTop = itemView.top + (itemView.height - deleteIcon.intrinsicHeight) / 2
            val iconBottom = iconTop + deleteIcon.intrinsicHeight
            if (dX > 0) { // Swiping to the right
                val iconLeft = itemView.left + iconMargin
                val iconRight = itemView.left + iconMargin + deleteIcon.intrinsicWidth
                deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            } else { // Swiping to the left
            }
            deleteIcon.draw(c)
        }
    }
}