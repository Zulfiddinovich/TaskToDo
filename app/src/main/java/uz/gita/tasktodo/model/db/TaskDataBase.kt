package uz.gita.tasktodo.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TaskEntity::class], version = 2)
abstract class TaskDataBase: RoomDatabase() {

    abstract fun getDao(): TaskDao

    companion object{
        private var dataBase: TaskDataBase? = null

        fun getDB() = dataBase

        fun init(context: Context){
            dataBase = Room.databaseBuilder(context, TaskDataBase::class.java, "task.db")
                .fallbackToDestructiveMigration() // for not providing migrating new version
                .allowMainThreadQueries()
                .build()
        }
    }

}