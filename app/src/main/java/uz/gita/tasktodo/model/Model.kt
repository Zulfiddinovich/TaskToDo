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
        Log.d("TAG", "model currentList: " + list)
        return list
    }

    override fun addTask(data: TaskEntity) {
        if( data.sortId == 0 ) {
            if (list.size != 0 && list[list.lastIndex].sortId > list.size) data.sortId = list[list.lastIndex].sortId + 1
            else data.sortId = list.size + 1
        }

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

    override fun moveDB(from_pos: Int, to_pos: Int) {
        dao.moving(list[from_pos].sortId, list[to_pos].sortId)
    }
}