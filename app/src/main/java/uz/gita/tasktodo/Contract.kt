package uz.gita.tasktodo

import uz.gita.tasktodo.model.db.TaskEntity

interface Contract {

    interface Model{
        fun getAll(): ArrayList<TaskEntity>

        fun addTask(data: TaskEntity)

        fun closeTask(isClosed: Boolean, position: Int)

        fun editTask(data: TaskEntity)

        fun deleteTask(data: TaskEntity)

        fun moveDB(from_pos: Int, to_pos: Int)
    }

    interface Presenter{

        fun reloadAction()

        fun addAction(data: TaskEntity)

        fun onCloseAction(isClosed: Boolean, position: Int)

        fun editAction(data: TaskEntity)

        fun deleteAction(data: TaskEntity)

        fun moveAction(from_pos: Int, to_pos: Int)

        fun openEditDialog(data: TaskEntity)
    }

    interface View{
        fun showList(list: List<TaskEntity>)

        fun countSetter()

        fun editDialog(item: TaskEntity)

        fun setDialog()
    }

}