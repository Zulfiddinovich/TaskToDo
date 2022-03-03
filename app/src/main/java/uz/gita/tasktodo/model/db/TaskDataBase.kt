package uz.gita.tasktodo.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Task1Entity::class], version = 1)
abstract class TaskDataBase: RoomDatabase() {

    abstract fun getDao(): TaskDao

    companion object{
        private var dataBase: TaskDataBase? = null

        fun getDB() = dataBase

        fun init(context: Context){
            dataBase = Room.databaseBuilder(context, TaskDataBase::class.java, "task.db")
                .allowMainThreadQueries()
                .build()
        }
    }

}