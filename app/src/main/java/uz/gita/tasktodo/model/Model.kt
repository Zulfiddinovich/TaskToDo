package uz.gita.tasktodo.model

import android.util.Log
import uz.gita.tasktodo.Contract
import uz.gita.tasktodo.model.db.TaskDataBase
import uz.gita.tasktodo.model.db.TaskEntity

class Model: Contract.Model {
    private val dao = TaskDataBase.getDB()!!.getDao()
    private var list = ArrayList<TaskEntity>()


    override fun getAll(): ArrayList<TaskEntity> {
        list = dao.getAll() as ArrayList<TaskEntity>
        list.reverse()
        Log.d("TAG", "model currentList: " + list.size)
        return list
    }

    override fun addTask(data: TaskEntity) {
        dao.add(data)
        Log.d("TAG", "model addNewItem: added "  + list.size)
    }

    override fun closeTask(isClosed: Boolean, position: Int) {
        list[position].isClosed = isClosed
        dao.edit(list[position])
    }

    override fun editTask(data: TaskEntity) {
        dao.edit(data)
    }

    override fun deleteTask(data: TaskEntity) {
        dao.delete(data)
    }
}