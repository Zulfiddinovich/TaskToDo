package uz.gita.tasktodo.app

import android.app.Application
import uz.gita.tasktodo.model.LocalStorage
import uz.gita.tasktodo.model.db.TaskDataBase

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        LocalStorage.init(this)
        TaskDataBase.init(this)

    }

    companion object{
        var firstTimeLottie = true
        lateinit var instance: App
    }
}