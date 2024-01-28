package com.example.marsad.ui.favorites.view

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.marsad.R
import com.example.marsad.domain.models.FavoriteLocation

class LocationItemTouchHelperCallback(
    context: Context,
    private val locationItemAdapter: LocationItemAdapter,
    private val onSwipedCallback: (item: FavoriteLocation) -> Unit,
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

    private val deleteIcon =
        ContextCompat.getDrawable(context, R.drawable.ic_delete)
    private val deleteIconColor =
        ContextCompat.getColor(context, R.color.md_theme_dark_surfaceTint)

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder,
    ): Boolean {
        //Up&Down move
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        val swipedItem = locationItemAdapter.locations[position]
        onSwipedCallback(swipedItem)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean,
    ) {


        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val itemView = viewHolder.itemView
            val iconMargin = (itemView.height - deleteIcon!!.intrinsicHeight) / 2
            val iconTop = itemView.top + (itemView.height - deleteIcon.intrinsicHeight) / 2
            val iconBottom = iconTop + deleteIcon.intrinsicHeight
            val iconLeft = itemView.left + iconMargin
            val iconRight = itemView.left + iconMargin + deleteIcon.intrinsicWidth

            if (dX > 0) { // Swiping to the right
                deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                c.clipRect(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
                deleteIcon.draw(c)
            } else { // Other Swiping directions
                c.clipRect(
                    itemView.right + dX.toInt(),
                    itemView.top,
                    itemView.right,
                    itemView.bottom
                )
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

}