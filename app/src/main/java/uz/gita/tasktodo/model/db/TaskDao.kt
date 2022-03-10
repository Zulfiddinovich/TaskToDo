package uz.gita.tasktodo.model.db

import androidx.room.*

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks order by sort_id")
    fun getAll(): List<TaskEntity>

    @Insert
    fun add(data: TaskEntity)

    @Delete
    fun delete(data: TaskEntity)

    @Update
    fun edit(data: TaskEntity)

    @Query("update tasks set sort_id = case when sort_id = :from_pos then :to_pos  when sort_id = :to_pos then :from_pos else 0 end where sort_id between min(:from_pos,:to_pos) and max (:from_pos,:to_pos)")
    fun moving(from_pos: Int, to_pos: Int)
}