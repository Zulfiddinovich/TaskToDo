package uz.gita.tasktodo.view

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.gita.tasktodo.R
import uz.gita.tasktodo.model.db.TaskEntity
import java.util.ArrayList

class TaskAdapter: ListAdapter<TaskEntity,RecyclerView.ViewHolder>(Diff()) {
    private lateinit var listener: (Boolean, Int)-> Unit
    private val list = ArrayList<TaskEntity>()
    private var removedItems = ArrayList<TaskEntity>()


    fun setListener(listener: (Boolean, Int)-> Unit){
        this.listener = listener
    }

    class Diff: DiffUtil.ItemCallback<TaskEntity>() {
        override fun areItemsTheSame(oldItem: TaskEntity, newItem: TaskEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TaskEntity, newItem: TaskEntity): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TaskViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.task_layout, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TaskViewHolder) holder.bind(currentList[position], position)
    }

    /*    ////*****////
    * since we use a ListAdapter it refreshes a data list asynchronously and it can easily lead to that
    * we will get the currentList that hasn’t updated yet but a view holder was already removed all this
    * leads to that we get wrong items count from getItemCount() method and sometimes incorrect items
    * placement on the screen, this case most likely if user swipes items quickly:
    *        Now the method can return an item if it was removed and null otherwise.*/
    fun delete(pos: Int): Any? {
        if (pos < itemCount){
            val item = currentList[pos]
            removedItems.add(item)
            val actualList = (currentList - removedItems) as ArrayList<TaskEntity>
            if (actualList.isEmpty()) removedItems.clear()
            newSubmit(actualList, false)
            return item
        } else {
            return null
        }
    }


    fun newSubmit(list: List<TaskEntity>, isNewList: Boolean) {
        if (isNewList) removedItems.clear()
        super.submitList(list)
    }
    ////*****////





    inner class TaskViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.item_title)
        private val deadline: TextView = view.findViewById(R.id.item_deadline)
        private val checkbox: CheckBox = view.findViewById(R.id.checkbox)


        fun bind(data: TaskEntity, position: Int){
            title.text = data.title
            deadline.text = data.deadline

            if (data.isClosed){
                title.paintFlags = title.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                deadline.paintFlags = deadline.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                checkbox.isChecked = true
            } else{
                title.paintFlags = 0
                deadline.paintFlags = 0
                checkbox.isChecked = false
            }


            checkbox.setOnClickListener {
                data.isClosed = !data.isClosed
                listener(data.isClosed, position)
                notifyItemChanged(position)
            }
        }
    }


}