package uz.gita.tasktodo.model

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LocalStorage private constructor(context: Context) {

    companion object{
        private lateinit var preference: LocalStorage

        fun init(context: Context){
            preference = LocalStorage(context)
        }

        fun getPref(): LocalStorage = preference
    }

    private val pref = context.getSharedPreferences("TASKS", Context.MODE_PRIVATE)
    private val editor = pref.edit()
    val gson = Gson()


    /*fun setToShared(task: TaskData){
        val json: String = gson.toJson(task)
        editor.remove("TASK").commit()
        editor.putString("TASK",json).commit()
    }

    fun getFromShared(): TaskData{
        val json: String? = pref.getString("TASK", "")
        val task: TaskData = gson.fromJson(json, TaskData::class.java)
        return task
    }*/

    fun setListToShared(tasks: ArrayList<TaskEntity>){

        val json: String = gson.toJson(tasks)
        editor.remove("TASK").commit()
        editor.putString("TASK",json).commit()
    }

    fun getListFromShared(): ArrayList<TaskEntity> {
        val json: String? = pref.getString("TASK", "")
        if (json == null) {
            return ArrayList<TaskEntity>()
        }
        else {
            val type = object : TypeToken<List<TaskEntity>>() {}.type;
            val list: ArrayList<TaskEntity> = gson.fromJson(json, type)
            Log.d("TAG", "getListFromShared:  " + list)
            return list
        }
    }





}