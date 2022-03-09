package uz.gita.tasktodo

import uz.gita.tasktodo.model.db.TaskEntity

interface Contract {

    interface Model{
        fun getAll(): ArrayList<TaskEntity>

        fun addTask(data: TaskEntity)

        fun closeTask(isClosed: Boolean, position: Int)

        fun editTask(data: TaskEntity)

        fun deleteTask(data: TaskEntity)
    }

    interface Presenter{

        fun reloadAction()

        fun addAction(data: TaskEntity)

        fun onCloseAction(isClosed: Boolean, position: Int)

        fun editAction(data: TaskEntity)

        fun deleteAction(data: TaskEntity)

        fun saveUpdatedTaskAction()

        fun saveNewTaskAction()

        fun hide()
    }

    interface View{
        fun showList(list: List<TaskEntity>)

        fun countSetter()

        fun saveUpdatedTask()

        fun saveNewTask()

        fun hide()
    }

}