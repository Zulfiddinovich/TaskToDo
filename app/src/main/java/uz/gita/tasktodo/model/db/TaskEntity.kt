package uz.gita.tasktodo.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    var title: String,

    var deadline: String,

    @ColumnInfo(name = "is_closed")
    var isClosed: Boolean
)