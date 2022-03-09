package uz.gita.tasktodo

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.transition.TransitionManager
import android.util.Log.d
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import uz.gita.tasktodo.model.db.TaskEntity
import uz.gita.tasktodo.presenter.Presenter
import uz.gita.tasktodo.util.showSnackbar
import uz.gita.tasktodo.view.ItemGestures
import uz.gita.tasktodo.view.TaskAdapter


class MainActivity : AppCompatActivity(), Contract.View {
    private lateinit var adapter: TaskAdapter
    private lateinit var presenter: Contract.Presenter
    private var activityList = ArrayList<TaskEntity>()
    private val constrain1 = ConstraintSet()
    private val constrain2 = ConstraintSet()
    private lateinit var constraint: ConstraintLayout
    private var tempPos: Int? = null
    private var customItem = TaskEntity(0,"","",true)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        init()
        setAdapter()

        presenter = Presenter(this)
        presenter.reloadAction()

        adapter.setListener { isClosed, pos ->
            presenter.onCloseAction(isClosed, pos)
        }

        add_button.setOnClickListener {
            customItem.id = 0
            customItem.title = ""
            customItem.deadline = ""
            customItem.isClosed = false
            presenter.hide()
        }

        save_button.setOnClickListener {
            when (customItem.id) {
                0 -> presenter.saveNewTaskAction()
                else -> presenter.saveUpdatedTaskAction()
            }

        }

        swipe_refresh.setOnRefreshListener {
            presenter.reloadAction()
            swipe_refresh.isRefreshing = false
        }


        openOptionsMenu()
        menu_button.setOnClickListener {
//            closeOptionsMenu()
        }


        val swipeGestures = object: ItemGestures(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                tempPos = viewHolder.adapterPosition
                when(direction){
                    ItemTouchHelper.RIGHT -> {
                        customItem = adapter.currentList[tempPos!!]
                        setForEdit(customItem)
                    }
                    ItemTouchHelper.LEFT -> {
                        adapter.delete(tempPos!!)
                        (presenter as Presenter).deleteAction(adapter.currentList[tempPos!!])
                    }
                }
            }
        }
        val touchHelper = ItemTouchHelper(swipeGestures)
        touchHelper.attachToRecyclerView(recyclerView)
    }

    override fun openOptionsMenu() {
        (toolbar as androidx.appcompat.widget.Toolbar).showOverflowMenu();
    }

    private fun setForEdit(item: TaskEntity){
        if (save_button.isInvisible) presenter.hide()

        edit_item_title.setText(item.title)
        edit_item_deadline.setText(item.deadline)
    }

    override fun saveUpdatedTask() {
        if (edit_item_title.text.isNotEmpty() && edit_item_deadline.text.isNotEmpty()) {
            customItem.title = edit_item_title.text.toString()
            customItem.deadline = edit_item_deadline.text.toString()

            presenter.editAction(customItem)
            adapter.notifyItemChanged(tempPos!!)
            tempPos = null
            presenter.hide()
        } else {
            showSnackbar(this.dialog1, getString(R.string.maydon_tuldiring))
        }
    }

    override fun saveNewTask() {
        if (edit_item_title.text.isNotEmpty() && edit_item_deadline.text.isNotEmpty()) {

            customItem.title = edit_item_title.text.toString()
            customItem.deadline = edit_item_deadline.text.toString()
            presenter.addAction(customItem)
            d("TAG", customItem.title +" "+customItem.deadline)
            presenter.hide()
        } else {
            showSnackbar(this.dialog1, getString(R.string.maydon_tuldiring))
        }
    }

    override fun countSetter() {
        var counter = 0
        activityList.forEach {
            if (it.isClosed)
            else counter++
        }
        active_task_size.text = counter.toString()
    }

    private fun setAdapter() {
        adapter = TaskAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun init() {
        /*  Edit text automatic ochilib qolayotgan edi  */
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        /*******/
        /*  EditTextlar animatsiya qilish uchun recyclerni orqasida qolishga majbur edi  */
        /*super_layout.requestLayout()
        dialog1.bringToFront()
        dialog1.invalidate()

//        dialog1.setTranslationZ(180f);

        if (Build.VERSION.SDK_INT >= 21) {
            dialog1.setTranslationZ(Integer.MAX_VALUE.toFloat());
        }
//        super_layout.bringChildToFront(dialog1)*/


        constrain2.clone(this, R.layout.activity_main2)
        setContentView(R.layout.activity_main)
        constraint = findViewById<ConstraintLayout>(R.id.super_layout)
        constrain1.clone(constraint)
    }

    override fun hide() {
        TransitionManager.beginDelayedTransition(constraint)
        if (save_button.isVisible) {
            constrain1.applyTo(constraint)
            save_button.visibility = View.INVISIBLE
            add_button.setImageResource(R.drawable.ic_add_button)
            add_button.background.setTint(ContextCompat.getColor(this, R.color.blue))
            edit_item_title.setText("")
            edit_item_deadline.setText("")

            val view = this.currentFocus
            if (view != null) {
                val imm: InputMethodManager =
                    this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }

        } else {
            constrain2.applyTo(constraint)
            save_button.visibility = View.VISIBLE
            add_button.setImageResource(R.drawable.ic_x)
            add_button.background.setTint(ContextCompat.getColor(this, R.color.yellow))
        }
    }

    override fun showList(list: List<TaskEntity>) {
        adapter.newSubmit(list, false)
        activityList.clear()
        activityList.addAll((list as ArrayList<TaskEntity>))
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        if (menu is MenuBuilder) menu.setOptionalIconsVisible(true)
        return true
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
            showSnackbar(this.toolbar, getString(R.string.cannot_email))
        }
    }

}