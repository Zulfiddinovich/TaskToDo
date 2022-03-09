package uz.gita.tasktodo.model.db

import androidx.room.*

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks")
    fun getAll(): List<TaskEntity>

    @Insert
    fun add(data: TaskEntity)

    @Delete
    fun delete(data: TaskEntity)

    @Update
    fun edit(data: TaskEntity)
}