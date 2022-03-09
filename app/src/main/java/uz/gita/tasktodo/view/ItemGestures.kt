package uz.gita.tasktodo.view

import android.content.Context
import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import uz.gita.tasktodo.R


abstract class ItemGestures(val context: Context): ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
    val deleteColor = ContextCompat.getColor(context,R.color.red)
    val editColor = ContextCompat.getColor(context,R.color.gray)
    val deleteIcon = R.drawable.ic_edit
    val editIcon = R.drawable.ic_delete


    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
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
        RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            .addSwipeLeftActionIcon(deleteIcon)
            .addSwipeLeftBackgroundColor(deleteColor)
            .addSwipeRightActionIcon(editIcon)
            .addSwipeRightBackgroundColor(editColor)
            .create()
            .decorate()

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}