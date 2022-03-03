package uz.gita.tasktodo.view

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.task_layout.view.*
import uz.gita.tasktodo.R
import uz.gita.tasktodo.model.TaskEntity

class TaskAdapter: RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    private val list: ArrayList<TaskEntity> = ArrayList<TaskEntity>()
    private lateinit var listener: (Boolean, Int)-> Unit

    fun setListener(listener: (Boolean, Int)-> Unit){
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.task_layout, parent, false))
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(list: List<TaskEntity>){
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }



    inner class TaskViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val title: TextView = view.item_title
        private val deadline: TextView = view.item_deadline
        private val checkbox: CheckBox = view.checkbox


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