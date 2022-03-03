package uz.gita.tasktodo.model

import android.util.Log
import uz.gita.tasktodo.Contract
import uz.gita.tasktodo.model.db.TaskDataBase

class Model: Contract.Model {
    private val dao = TaskDataBase.getDB()!!.getDao()
    private var list = ArrayList<TaskEntity>()


    override fun loadList(): ArrayList<TaskEntity> {
        if (list.isEmpty()) { list = LocalStorage.getPref().getListFromShared() }
//        list = dao.getAll() as ArrayList<TaskEntity>
        Log.d("TAG", "model currentList: " + list.size)
        return list
    }

    override fun addNewItem(data: TaskEntity) {
        list.reverse()
        list.add(data)
        list.reverse()
        Log.d("TAG", "model addNewItem: added "  + list.size)
    }

    override fun leaveToPref() {
        LocalStorage.getPref().setListToShared(list)
    }

    override fun addMore() {

    }

    override fun isClosed(isClosed: Boolean, position: Int) {
        list[position].isClosed = isClosed
    }


    override fun clear() {

    }
}