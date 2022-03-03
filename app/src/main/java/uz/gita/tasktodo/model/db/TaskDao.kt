package uz.gita.tasktodo.model.db

import androidx.room.*

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks")
    fun getAll(): List<Task1Entity>

    @Insert
    fun addTask(data: Task1Entity)

    @Delete
    fun deleteTask(data: Task1Entity)

    @Update
    fun editTask(data: Task1Entity)
}