package uz.gita.tasktodo

import uz.gita.tasktodo.model.TaskEntity

interface Contract {

    interface Model{
        fun loadList(): ArrayList<TaskEntity>

        fun addNewItem(data: TaskEntity)

        fun leaveToPref()

        fun addMore()

        fun isClosed(isClosed: Boolean, position: Int)

        fun clear()
    }

    interface Presenter{
        fun reload()

        fun onAddAction()

        fun leaveToPrefAction()

        fun onCheckboxAction(isClosed: Boolean, position: Int)

        fun addNewItemAction(data: TaskEntity)

        fun clearList()
    }

    interface View{
        fun showList(list: List<TaskEntity>)
    }

}