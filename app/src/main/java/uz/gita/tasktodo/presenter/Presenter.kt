package uz.gita.tasktodo.presenter

import android.util.Log
import uz.gita.tasktodo.Contract
import uz.gita.tasktodo.model.Model
import uz.gita.tasktodo.model.db.TaskEntity

class Presenter(view: Contract.View): Contract.Presenter {
    private var model: Contract.Model = Model()
    private val view: Contract.View = view
    var isNew = false

    override fun reloadAction() {
        var list = model.getAll()
        view.showList(list)
        view.countSetter()
        Log.d("TAG", "presenter gets list " + list.size)
    }

    override fun editAction(data: TaskEntity) {
        model.editTask(data)
        reloadAction()
    }


    override fun onCloseAction(isClosed: Boolean, position: Int) {
        model.closeTask(isClosed, position)
        view.countSetter()

    }


    override fun addAction(data: TaskEntity) {
        model.addTask(data)
        reloadAction()
        Log.d("TAG", "addNewItem: $data")

    }

    override fun deleteAction(data: TaskEntity) {
        model.deleteTask(data)
        reloadAction()

    }

    override fun moveAction(from_pos: Int, to_pos: Int) {

        Log.d("TAG", "Moving from_pos - $from_pos to_pos - $to_pos")
        model.moveDB(from_pos, to_pos)
    }

    override fun openEditDialog(data: TaskEntity) {
        view.editDialog(data)
    }
}