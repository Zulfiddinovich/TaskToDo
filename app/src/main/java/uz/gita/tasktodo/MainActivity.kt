package uz.gita.tasktodo

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import uz.gita.tasktodo.model.LocalStorage
import uz.gita.tasktodo.model.TaskEntity
import uz.gita.tasktodo.presenter.Presenter
import uz.gita.tasktodo.util.showSnackbar
import uz.gita.tasktodo.view.TaskAdapter


class MainActivity : AppCompatActivity(), Contract.View {
    private lateinit var adapter: TaskAdapter
    private lateinit var presenter: Contract.Presenter
    private var activityList = ArrayList<TaskEntity>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar.title = ""
        setSupportActionBar(toolbar)
        presenter = Presenter(this)
        adapter = TaskAdapter()
        presenter.reload()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        adapter.setListener { isClosed, pos ->
            presenter.onCheckboxAction(isClosed, pos)

            var counter = 0
            activityList.forEach {
                if (it.isClosed)
                else counter++
            }
            active_task_size.text = counter.toString()
        }

        add_button.setOnClickListener {
            add_button.visibility = View.INVISIBLE
            dialog1.visibility = View.VISIBLE

        }

        save_button.setOnClickListener {
            if (edit_item_title.text.isNotEmpty() && edit_item_deadline.text.isNotEmpty()) {
                val newTask = TaskEntity(0,edit_item_title.text.toString(), edit_item_deadline.text.toString(), false)
                presenter.addNewItemAction(newTask)

                add_button.visibility = View.VISIBLE
                dialog1.visibility = View.INVISIBLE
                edit_item_title.setText("")
                edit_item_deadline.setText("")
                val view = this.currentFocus
                if (view != null) {
                    val imm: InputMethodManager = this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
            } else {
                showSnackbar(this.dialog1, "Kerakkli maydonni to`ldiring!")
            }
        }
        swipe_refresh.setOnRefreshListener {
            swipe_refresh.isRefreshing = false
        }

    }

    override fun showList(list: List<TaskEntity>) {
        adapter.setList(list)
        activityList.clear()
        activityList.addAll((list as ArrayList<TaskEntity>))

        var counter = 0
        activityList.forEach {
            if (it.isClosed)
            else counter++
        }
        active_task_size.text = counter.toString()
    }

    override fun onStop() {
        presenter.leaveToPrefAction()
        super.onStop()
    }


    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        if (menu is MenuBuilder) menu.setOptionalIconsVisible(true)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.share -> shareApp()
            R.id.rate_us -> rateApp()
            R.id.contact_us -> contactUs()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun rateApp() {
        val packageName =
            "developer?id=GITA+Dasturchilar+Akademiyasi" // "details?id=uz.gita.wooden15puzzleapp"
        val uri: Uri = Uri.parse("market://$packageName")
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(
            Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        )
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/$packageName")
                )
            )
        }
    }

    private fun shareApp() {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        val packageName =
            "developer?id=GITA+Dasturchilar+Akademiyasi" // "details?id=uz.gita.wooden15puzzleapp"

        val shareBody = "http://play.google.com/store/apps/$packageName"
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Our Apps")
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody)

//        sharingIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
        startActivity(Intent.createChooser(sharingIntent, "Share via"))
    }

    private fun contactUs() {
        val email = "kamolov2540@gmail.com"  //"ttymi.kamolov@gmail.com"
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.putExtra(android.content.Intent.EXTRA_EMAIL, arrayOf(email))
//        intent.type = "message/rfc822" // for multiple app
        intent.data = Uri.parse("mailto:")
        if (intent.resolveActivity(this.packageManager) != null) {
            startActivity(intent)
        } else {
            showSnackbar(this.toolbar, "You have no application to contact us via email")
        }
    }

}