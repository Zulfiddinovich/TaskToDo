package uz.gita.tasktodo.presenter

import android.util.Log
import uz.gita.tasktodo.Contract
import uz.gita.tasktodo.model.Model
import uz.gita.tasktodo.model.TaskEntity

class Presenter(view: Contract.View): Contract.Presenter {
    private var model: Contract.Model = Model()
    private val view: Contract.View = view

    override fun reload() {
        var list: ArrayList<TaskEntity> = model.loadList()
        view.showList(list)
        Log.d("TAG", "presenter gets list " + list.size)
    }

    override fun onAddAction() {

    }

    override fun leaveToPrefAction() {
        model.leaveToPref()
    }

    override fun onCheckboxAction(isClosed: Boolean, position: Int) {
        model.isClosed(isClosed, position)
    }


    override fun addNewItemAction(data: TaskEntity) {

        model.addNewItem(data)
        reload()
        Log.d("TAG", "addNewItem: $data")

    }

    override fun clearList() {

    }
}