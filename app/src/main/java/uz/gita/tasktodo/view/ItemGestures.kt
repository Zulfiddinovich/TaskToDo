package uz.gita.tasktodo.view

import android.content.Context
import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import uz.gita.tasktodo.R


abstract class ItemGestures(val context: Context) :
    ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {
    private val deleteColor = ContextCompat.getColor(context, R.color.red)
    private val deleteIcon = R.drawable.ic_delete
    private val editColor = ContextCompat.getColor(context, R.color.gray)
    private val editIcon = R.drawable.ic_edit


    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        RecyclerViewSwipeDecorator.Builder(
            c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
            .addSwipeLeftActionIcon(deleteIcon)
            .addSwipeLeftBackgroundColor(deleteColor)
            .addSwipeRightActionIcon(editIcon)
            .addSwipeRightBackgroundColor(editColor)
            .create()
            .decorate()

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}