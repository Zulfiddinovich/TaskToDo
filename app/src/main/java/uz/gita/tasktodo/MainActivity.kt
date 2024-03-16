package uz.gita.tasktodo

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log.d
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import uz.gita.tasktodo.app.App
import uz.gita.tasktodo.databinding.ActivityMainBinding
import uz.gita.tasktodo.model.db.TaskEntity
import uz.gita.tasktodo.presenter.Presenter
import uz.gita.tasktodo.util.showSnackbar
import uz.gita.tasktodo.view.ItemGestures
import uz.gita.tasktodo.view.TaskAdapter
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), Contract.View {
    private lateinit var adapter: TaskAdapter
    private lateinit var presenter: Contract.Presenter
    private var activityList = ArrayList<TaskEntity>()
    private var tempPos: Int? = null
    private var customItem = TaskEntity(0, "", "", true, 0)
    private lateinit var dialog: BottomSheetDialog
    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = ""
        setSupportActionBar(binding.toolbar)
        beginLottie()

        setAdapter()
        setDialog()

        presenter = Presenter(this)
        presenter.reloadAction()

        adapter.setListener { isClosed, pos ->
            presenter.onCloseAction(isClosed, pos)
        }

        binding.addButton.setOnClickListener {
            customItem.id = 0
            customItem.title = ""
            customItem.deadline = ""
            customItem.isClosed = false
            customItem.sortId = 0
            presenter.openEditDialog(customItem)
        }

        binding.swipeRefresh.setOnRefreshListener {
            presenter.reloadAction()
            binding.swipeRefresh.isRefreshing = false
        }

        val swipeGestures = object : ItemGestures(this) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {


                val fromPos = viewHolder.adapterPosition
                val toPos = target.adapterPosition
                d("TAG", "fromPos - $fromPos, toPos - $toPos")

                Collections.swap(activityList, fromPos, toPos)
                adapter.notifyItemMoved(fromPos, toPos)
                presenter.moveAction(fromPos, toPos)
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                tempPos = viewHolder.adapterPosition
                val item = adapter.currentList[tempPos!!]
                when (direction) {
                    ItemTouchHelper.RIGHT -> {

                        customItem.id = item.id
                        customItem.title = item.title
                        customItem.deadline = item.deadline
                        customItem.isClosed = item.isClosed
                        customItem.sortId = item.sortId

                        presenter.openEditDialog(customItem)
                        adapter.notifyItemChanged(tempPos!!)
                    }
                    ItemTouchHelper.LEFT -> {
                        adapter.delete(tempPos!!)
                        (presenter as Presenter).deleteAction(adapter.currentList[tempPos!!])

                        Snackbar.make(binding.recyclerView, "Task deleted", Snackbar.LENGTH_LONG).setAction("Undo") {
                            presenter.addAction(item)
//                            adapter!!.notifyItemRemoved(tempPos!!)
                        }
                            .show()



                    }
                }
            }
        }
        val touchHelper = ItemTouchHelper(swipeGestures)
        touchHelper.attachToRecyclerView(binding.recyclerView)
    }


    override fun countSetter() {
        var counter = 0
        activityList.forEach {
            if (!it.isClosed) counter++
        }
        binding.activeTaskSize.text = counter.toString()
    }

    private fun setAdapter() {
        adapter = TaskAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun beginLottie() {
//        /*  Edit text automatic ochilib qolayotgan edi  */
//        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if (App.firstTimeLottie) {
            binding.lottieView.animate().translationY(-1500F).setDuration(1000).setStartDelay(2500)
            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                binding.lottieView.visibility = View.GONE
            }, 3700)
            App.firstTimeLottie = false
        } else binding.lottieView.visibility = View.GONE
    }

    override fun editDialog(item: TaskEntity) {
        val editView = layoutInflater.inflate(R.layout.bottom_edit, null)

        val title = editView.findViewById<EditText>(R.id.edit_item_title)
        val deadline = editView.findViewById<EditText>(R.id.edit_item_deadline)
        val button = editView.findViewById<ImageButton>(R.id.save_button)
        val exitButton = editView.findViewById<FloatingActionButton>(R.id.exit_button)
        title.setText(item.title)
        deadline.setText(item.deadline)
        dialog.setContentView(editView)
        dialog.show()

        /*val view = this.currentFocus
        if (view != null) {
            val imm: InputMethodManager =
                this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }*/

        exitButton.setOnClickListener {
            dialog.dismiss()
        }

        if (item.id != 0) {
            button.setOnClickListener {
                d("TAG", "edit: saved")
                if (title.text.isNotEmpty() && deadline.text.isNotEmpty()) {
                    item.title = title.text.toString()
                    item.deadline = deadline.text.toString()
                    presenter.editAction(item)
                    dialog.dismiss()
//                    adapter.notifyItemChanged(tempPos!!)
                    tempPos = null
                } else {
                    showSnackbar(editView, getString(R.string.maydon_tuldiring))
                }
            }
        } else {
            button.setOnClickListener {
                d("TAG", "new: saved")
                if (title.text.isNotEmpty() && deadline.text.isNotEmpty()) {
                    item.title = title.text.toString()
                    item.deadline = deadline.text.toString()
                    presenter.addAction(item)
                    dialog.dismiss()
                } else {
                    showSnackbar(editView, getString(R.string.maydon_tuldiring))
                }
            }
        }
    }


    override fun setDialog() {
        dialog = BottomSheetDialog(this, R.style.DialogCustomTheme)
        dialog.setCancelable(true)

    }

    override fun showList(list: List<TaskEntity>) {
        adapter.newSubmit(list, true)
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
            showSnackbar(binding.toolbar, getString(R.string.cannot_email))
        }
    }

}